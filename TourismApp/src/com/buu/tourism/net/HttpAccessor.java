package com.buu.tourism.net;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.buu.tourism.Constant;
import com.buu.tourism.conf.ApiConstant;
import com.buu.tourism.conf.Setting;
import com.buu.tourism.net.engine.HttpClientNetworkRequests;
import com.buu.tourism.net.engine.HttpRequestEntity;
import com.buu.tourism.net.engine.HttpResultEntity;
import com.buu.tourism.net.engine.IHttpResponseCallBack;
import com.buu.tourism.net.engine.INetworkRequests;
/**
 * 提供给业务成的数据访问类
 * @author xingyu10
 *
 */
public class HttpAccessor {
    private static final ThreadPoolExecutor mNetPoolexecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private static HttpAccessor mInstance;
    private static INetworkRequests mHttpClient = new HttpClientNetworkRequests();;
    private static final Object LOCK = new Object();

    public static HttpAccessor getInstance() {
        if (mInstance == null) {
            synchronized (LOCK) {
                if (null == mInstance) {
                    mInstance = new HttpAccessor();
                }
            }
        }
        return mInstance;
    }

    public Future<?> request(final HttpRequestEntity httpRequest, final IHttpResponseCallBack callBack) {
        if (httpRequest instanceof MockHttpReqeust) {
            MockHttpReqeust reqeust = (MockHttpReqeust) httpRequest;
            return test(reqeust.mockResponseStr, callBack);
        }
        return mNetPoolexecutor.submit(new Runnable() {
            @Override
            public void run() {
                mHttpClient.request(httpRequest,callBack);
            }
        });
    }

    Future<?> test(final String responseJson, final IHttpResponseCallBack callBack) {
        callBack.onStart();
        return mNetPoolexecutor.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    double random = Math.random();
                    Thread.sleep((long) (5000 * random));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        HttpResultEntity httpResult = new HttpResultEntity();
//                        if (random > 0.3) {
//                        } else {
//                            httpResult.responseStr = "{}";
//                        }
                        httpResult.responseStr = responseJson;
                        callBack.onResult(httpResult);
                    }
                });
            }
        });
    }

}
