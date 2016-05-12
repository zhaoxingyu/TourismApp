/**
 * 
 */
package com.buu.tourism.net.engine;

import java.io.InputStream;
import java.net.Proxy;
import java.util.HashMap;

import android.os.Bundle;

/**
 * 网络模块的接口定义
 * 
 * @author xingyu10
 *
 */
public interface INetworkRequests {

    public HttpResultEntity get(String url);

    public HttpResultEntity get(String url, Bundle getParams);

    public HttpResultEntity get(String url, HashMap<String, String> header, Bundle getParams);

    public HttpResultEntity post(String url, InputStream postStream);

    public HttpResultEntity post(String url, HashMap<String, String> header, InputStream postStream);

    public HttpResultEntity request(HttpRequestEntity httpRequest);
    
    public HttpResultEntity request(HttpRequestEntity httpRequest, IHttpResponseCallBack callBack);
    
    public HttpResultEntity request(HttpRequestEntity httpRequest, IHttpResponseCallBack callBack, boolean autoRedirect, Proxy proxy);

}
