package com.nostra13.universalimageloader.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FileCleanUtil {
    private static final ThreadPoolExecutor mSingleThreadExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());
    private static Map<String, Long> mCleanKeeper = new ConcurrentHashMap<String, Long>();
    private static final float FACTOR = 0.3f;
    private static final int CLEAN_INTERVAL = 5 * 60 * 1000;

    /**
     * 若文件夹下文件超过最大值，则根据最后使用时间删除最早的1/3文件
     * 
     * @param dir
     * @param maxFileNum
     */
    public static void adjustFolderSize(final File dir, final int maxFileNum) {
        if (dir == null) {
            return;
        }
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        String key = dir.getAbsolutePath();
        long now = System.currentTimeMillis();
        Long lastCleanTime = (mCleanKeeper.get(key) == null) ? 0 : mCleanKeeper.get(key);
        if (now - lastCleanTime > CLEAN_INTERVAL) {
            mCleanKeeper.put(key, now);
            mSingleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    trySharkFolderSize(dir, maxFileNum, FACTOR);
                }
            });
        }
    }

    /**
     * clean folder。
     * call in async thread
     * @param dir dst
     * @param maxFileNum
     * @param factor
     */
    public static void trySharkFolderSize(File dir, int maxFileNum, float factor) {
        try {
            int folderSize = dir.list().length;
            boolean overSize = folderSize > maxFileNum;
            if (overSize) {
                File[] list = dir.listFiles();
                int fileNum = list.length;
                int sizeToDel = (int) (fileNum * factor);
                if (fileNum > sizeToDel) {
                    Arrays.sort(list, new Comparator<File>() {
                        @Override
                        public int compare(File lhs, File rhs) {
                            long left = lhs.lastModified();
                            long right = rhs.lastModified();
                            if (left < right) {
                                return -1;
                            } else if (left > right) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    for (int i = 0; i < sizeToDel; i++) {
                        if (list[i].exists() && list[i].canWrite()) {
                            list[i].delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            try {
                Runtime.getRuntime().exec("rm -r " + dir.getAbsolutePath());
            } catch (IOException e1) {
                // do nothing
            }
        } catch (OutOfMemoryError e) {// 文件太多会oom，因为数组会很大。
            try {
                Runtime.getRuntime().exec("rm -r " + dir.getAbsolutePath());
            } catch (IOException e1) {
                // do nothing
            }
        }
    }
}
