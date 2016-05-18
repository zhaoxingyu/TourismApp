package com.buu.tourism.net.engine.content;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ByteArrayBody implements ContentBody {

    byte[] byteArray;

    public ByteArrayBody(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    @Override
    public String getMimeType() {
        return "application/octet-stream";
    }

    @Override
    public InputStream getPostStream() throws Exception {
        return new ByteArrayInputStream(byteArray);
    }

    @Override
    public String getCharset() {
        return null;
    }

    @Override
    public String getTransferEncoding() {
        return "8bit";
    }

    @Override
    public long getContentLength() {
        if (null != byteArray) {
            return byteArray.length;
        }
        return 0;
    }

    @Override
    public String getFileName() {
        return null;
    }

}