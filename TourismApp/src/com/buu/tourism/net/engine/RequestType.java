package com.buu.tourism.net.engine;

/**
 * 请求的类型。GET/POST
 * 
 * @author xingyu10
 *
 */
public enum RequestType {
    GET("GET"), POST("POST");

    private String method;

    RequestType(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}