package com.buu.tourism.net.engine.content;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.text.TextUtils;

public class StringBody implements ContentBody {

    String content;
    String charset;

    public StringBody(String content, String charset) {
        this.content = content;
        this.charset = charset;
    }

    @Override
    public String getMimeType() {
        return "text/plain";
    }

    @Override
    public InputStream getPostStream() throws Exception {
        return new ByteArrayInputStream(content.getBytes(getCharset()));
    }

    @Override
    public String getCharset() {
        if (!TextUtils.isEmpty(charset)) {
            return charset;
        }
        return null;
    }

    @Override
    public String getTransferEncoding() {
        return "8bit";
    }

    @Override
    public long getContentLength() {
        if (!TextUtils.isEmpty(content)) {
            return content.length();
        }
        return 0;
    }

    @Override
    public String getFileName() {
        return null;
    }

}