package com.buu.tourism.net.engine.content;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


import android.text.TextUtils;
/**
 * multipart类型的post请求体
 * @author zhaoxingyu
 *
 */
public class MultiPartBody extends InputStream {
    Map<String, ContentBody> map = new HashMap<String, ContentBody>();
    private String boundary;
    private static final int BUFFER_SIZE = 16 * 1024;
    private PipedInputStream inputStream;

    public MultiPartBody(String boundary) {
        this.boundary = boundary;
    }

    public void addPart(String key, ContentBody body) {
        map.put(key, body);
    }


    Set<Entry<String, ContentBody>> entrySet() {
        return map.entrySet();
    }

    @Override
    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        if (preparedPipedInputStream()) {
            return readBuffer(buffer, byteOffset, byteCount);
        }
        return -1;
    }

    @Override
    public int read() throws IOException {
        if (preparedPipedInputStream()) {
            return readInt();
        }
        return -1;
    }

    /**
     * 准备输入流
     * @return 是否创建成功
     */
    boolean preparedPipedInputStream() {
        if (null == inputStream) {
            try {
                final PipedOutputStream out = new PipedOutputStream();
                inputStream = new PipedInputStream(out, BUFFER_SIZE);
                new Thread() {
                    public void run() {
                        try {
                            writeMultipartStream(new DataOutputStream(out));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                }.start();
                return true;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        }
        return true;
    }

    int readBuffer(byte[] buffer, int byteOffset, int byteCount) {
        try {
            return inputStream.read(buffer, byteOffset, byteCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    int readInt() {
        try {
            return inputStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;

    }

    /**
     * 将内容写到输出流
     * 
     * @param dos 管道输出流，待读
     * @return 流大小
     */
    long writeMultipartStream(DataOutputStream dos) throws Exception {
        long totalLen = 0;
        String end = "\r\n";
        String mark = "--";
        Set<Entry<String, ContentBody>> entrySet = entrySet();
        for (Entry<String, ContentBody> entry : entrySet) {
            String name = entry.getKey();
            ContentBody contentBody = entry.getValue();
            dos.writeBytes(mark + boundary + end);
            dos.writeBytes("Content-Disposition: form-data");
            dos.writeBytes("; name=" + "\"" + name + "\"");

            String fileName = contentBody.getFileName();
            if (!TextUtils.isEmpty(fileName)) {
                dos.writeBytes("; filename=" + "\"" + fileName + "\"");
            }
            dos.writeBytes(end);

            String contentType = contentBody.getMimeType();
            dos.writeBytes("Content-Type: " + contentType);

            String charset = contentBody.getCharset();
            if (!TextUtils.isEmpty(charset)) {
                dos.writeBytes(";charset:" + "\"" + charset + "\"");
            }
            dos.writeBytes(end);

            dos.writeBytes("Content-Transfer-Encoding: " + contentBody.getTransferEncoding());

            dos.writeBytes(end);
            dos.writeBytes(end);

            InputStream postStream = contentBody.getPostStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = -1;

            while ((len = postStream.read(buffer)) != -1) {
                dos.write(buffer, 0, len);
                dos.flush();
                totalLen += len;
            }
            postStream.close();
            dos.writeBytes(end);
        }

        dos.writeBytes(mark + boundary + mark + end);
        dos.flush();
        dos.close();
        return totalLen;
    }
    
    @Override
    public void close() throws IOException {
        if (null != inputStream) {
            inputStream.close();
        }
    }
}