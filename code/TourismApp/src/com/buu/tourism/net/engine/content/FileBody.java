package com.buu.tourism.net.engine.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileBody implements ContentBody {

    File file;
    String contentType;

    public FileBody(File file, String contentType) {
        this.contentType = contentType;
        this.file = file;
    }

    @Override
    public String getMimeType() {
        return contentType;
    }

    @Override
    public InputStream getPostStream() throws Exception {
        if (null != file) {
            return new FileInputStream(file);
        }
        return null;
    }

    @Override
    public String getCharset() {
        return null;
    }

    @Override
    public String getTransferEncoding() {
        return "binary";
    }

    @Override
    public long getContentLength() {
        if (null != file) {
            return file.length();
        }
        return 0;
    }

    @Override
    public String getFileName() {
        if (null != file) {
            return file.getName();
        }
        return null;
    }
}