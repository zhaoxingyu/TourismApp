package com.nostra13.universalimageloader.cache.memory.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import com.facebook.imagepipeline.memory.NativeMemoryChunk;
import com.nostra13.universalimageloader.cache.memory.NativeMemoryCache;

/**
 * A cache that holds strong references to a limited number of Bitmaps. Each
 * time a Bitmap is accessed, it is moved to the head of a queue. When a Bitmap
 * is added to a full cache, the Bitmap at the end of that queue is evicted and
 * may become eligible for garbage collection.<br />
 * <br />
 * <b>NOTE:</b> This cache uses only strong references for stored Bitmaps.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.1
 */
public class NativefLruMemoryCache implements NativeMemoryCache {

    private final LinkedHashMap<String, NativeMemoryChunk> map;

    private final int maxSize;
    /** Size of this cache in bytes */
    private int size;

    /**
     * @param maxSize
     *            Maximum sum of the sizes of the Bitmaps in this cache
     */
    public NativefLruMemoryCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<String, NativeMemoryChunk>(0, 0.75f, true);
    }

    /**
     * Returns the Bitmap for {@code key} if it exists in the cache. If a Bitmap
     * was returned, it is moved to the head of the queue. This returns null if
     * a Bitmap is not cached.
     */
    @Override
    public final InputStream get(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            NativeMemoryChunk nativeMemoryChunk = map.get(key);
            if (null != nativeMemoryChunk) {
                InputStream is = toInputStream(nativeMemoryChunk);
                return is;
            } else {
                return null;
            }
        }
    }

    /**
     * Caches {@code Bitmap} for {@code key}. The Bitmap is moved to the head of
     * the queue.
     */
    public boolean put(String key, InputStream is) {
        if (key == null || is == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            try {
                int totalSize = is.available();
                if (totalSize < 1) {
                    return false;
                }
                NativeMemoryChunk chunk = new NativeMemoryChunk(totalSize);
                byte[] buff = new byte[16 * 1024];
                int len = buff.length;
                int offset = 0;
                while ((len = is.read(buff)) > -1) {
                    chunk.write(offset, buff, 0, len);
                    offset += len;
                }
                size += sizeOf(key, chunk);
                NativeMemoryChunk previous = map.put(key, chunk);
                if (previous != null) {
                    size -= sizeOf(key, previous);
                }
                trimToSize(maxSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Remove the eldest entries until the total of remaining entries is at or
     * below the requested size.
     *
     * @param maxSize
     *            the maximum size of the cache before returning. May be -1 to
     *            evict even 0-sized elements.
     */
    private void trimToSize(int maxSize) {
        while (true) {
            String key;
            NativeMemoryChunk value;
            synchronized (this) {
                if (size < 0 || (map.isEmpty() && size != 0)) {
                    throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
                }

                if (size <= maxSize || map.isEmpty()) {
                    break;
                }

                Map.Entry<String, NativeMemoryChunk> toEvict = map.entrySet().iterator().next();
                if (toEvict == null) {
                    break;
                }
                key = toEvict.getKey();
                value = toEvict.getValue();
                map.remove(key);
                value.close();
                size -= sizeOf(key, value);
            }
        }
    }

    /** Removes the entry for {@code key} if it exists. <b>ALWAYS</b> return null. IF you want to retrieve a inputStream, use {@code get(key)} instead*/
    @Override
    public final InputStream remove(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            NativeMemoryChunk previous = map.remove(key);
            if (previous != null) {
                size -= sizeOf(key, previous);
                previous.close();
            }
            return null;
        }
    }

    @Override
    public Collection<String> keys() {
        synchronized (this) {
            return new HashSet<String>(map.keySet());
        }
    }

    @Override
    public void clear() {
        trimToSize(-1); // -1 will evict 0-sized elements
    }

    /**
     * Returns the size {@code Bitmap} in bytes.
     * <p/>
     * An entry's size must not change while it is in the cache.
     */
    private int sizeOf(String key, NativeMemoryChunk value) {
        return value.getSize();
    }

    @Override
    public synchronized final String toString() {
        return String.format("LruCache[maxSize=%d]", maxSize);
    }

    private InputStream toInputStream(NativeMemoryChunk chunk) {
        return new MemoryChunkInputStream(chunk);
    }

    class MemoryChunkInputStream extends InputStream {
        NativeMemoryChunk nativeMemoryChunk;
        int size = 0;
        int offset = 0;

        public MemoryChunkInputStream(NativeMemoryChunk chunk) {
            nativeMemoryChunk = chunk;
            size = chunk.getSize();
        }

        @Override
        public int read() throws IOException {
            if (offset < size) {
                byte b = nativeMemoryChunk.read(offset);
                offset++;
                int result = toUnsignedInt(b);
                return result;
            } else {
                return -1;
            }
        }

        private int toUnsignedInt(byte x) {
            return ((int) x) & 0xff;
        }

        @Override
        public synchronized void reset() throws IOException {
            offset = 0;
        }
    }
    
    
}