package com.buu.tourism.net;

import com.buu.tourism.net.engine.HttpRequestEntity;

public class MockHttpReqeust extends HttpRequestEntity {

    public String mockResponseStr = "";

    public MockHttpReqeust(String mockResponseStr) {
        super(mockResponseStr);
        this.mockResponseStr = mockResponseStr;
    }
}
