package com.buu.tourism.net.engine;


import java.io.FilterInputStream;
import java.io.InputStream;
import java.net.Proxy;
import java.util.Map;

import android.os.Bundle;

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
     * 
     * 是否自动处理重定向请求
     */
    public boolean autoRedirect = true;

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
     * 设置POST请求的参数
     */
    public Bundle postParams;

    /**
     * 设置请求体
     */

    public RequestBody requestBody;

    /**
     * 设置请求的类型。GET/POST
     */
    public RequestType type = RequestType.GET;
    
    /**
     * 设置请求的代理
     */
    public Proxy proxy;

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
    
    public void setAutoRedirect(boolean autoRedirect) {
        this.autoRedirect = autoRedirect;
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
    
    public void setPostParams(Bundle postParams) {
        this.postParams = postParams;
    }


    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public void setType(RequestType type) {
        this.type = type;
    }
    
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }


    /**
     * 以流的形式作为POST方式的请求体。
     * @author zhaoxingyu
     *
     */
    public static class RequestBody extends FilterInputStream{
        
        public RequestBody(InputStream in) {
            super(in);
        }

        Bundle postParams;
        /**
         * 设置原始的业务请求数据
         * @param postParams
         */
        public void setPostParams(Bundle postParams) {
            this.postParams = postParams;
        }
        /**
         * 获取原始的业务请求数据
         * @return
         */
        public Bundle getPostParams() {
            return postParams;
        }
    }
}
