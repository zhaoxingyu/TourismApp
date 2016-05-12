package com.buu.tourism.net.engine.content;

import java.io.InputStream;

public interface ContentBody {

    public String getMimeType();

    public InputStream getPostStream() throws Exception;

    String getCharset();

    String getTransferEncoding();

    long getContentLength();

    String getFileName();
}