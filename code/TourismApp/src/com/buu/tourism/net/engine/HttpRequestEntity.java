package com.buu.tourism.net.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.os.Bundle;

import com.buu.tourism.net.engine.content.ContentBody;

/**
 * 请求的实体封装
 * 
 * @author xingyu10
 *
 */
public class HttpRequestEntity {

    /**
     * 是否强制获取字符串结果
     */
    public boolean forceString;

    /**
     * 是否强制获取字节数组结果
     */
    public boolean forceByteArr;
    /**
     * 
     * 是否强制获取输出流
     */
    public boolean forceInputStream;

    /**
     * 设置请求的url
     */
    public String url;

    /**
     * 设置请求的header
     */
    public Map<String, String> header;

    /**
     * 设置GET请求的参数
     */
    public Bundle getParams;

    /**
     * 设置请求体
     */
    public RequestBody<?> requestBody;

    /**
     * 设置请求的类型。GET/POST
     */
    public RequestType type = RequestType.GET;

    public HttpRequestEntity(String url) {
        super();
        this.url = url;
    }

    
    public void setForceString(boolean forceString) {
        this.forceString = forceString;
    }

    public void setForceByteArr(boolean forceByteArr) {
        this.forceByteArr = forceByteArr;
    }

    public void setForceInputStream(boolean forceInputStream) {
        this.forceInputStream = forceInputStream;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public void setGetParams(Bundle getParams) {
        this.getParams = getParams;
    }

    public void setRequestBody(RequestBody<?> requestBody) {
        this.requestBody = requestBody;
    }

    public void setType(RequestType type) {
        this.type = type;
    }


    public static class RequestBody<E> {
        E e;

        public E getBody() {
            return e;
        };

        public void setBody(E body) {
            this.e = body;
        };
    }

    public static class MultiPartBody {
        Map<String, ContentBody> map = new HashMap<String, ContentBody>();

        public void addPart(String key, ContentBody body) {
            map.put(key, body);
        }

        public Set<Entry<String, ContentBody>> entrySet() {
            return map.entrySet();
        }
    }

}
