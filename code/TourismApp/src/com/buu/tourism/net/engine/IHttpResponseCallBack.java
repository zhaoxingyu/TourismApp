package com.buu.tourism.net.engine;
/**
 * 请求的回调接口
 * @author xingyu10
 *
 */
public interface IHttpResponseCallBack {

        public void onStart();

        public void onUpdate(byte[] buffer, int offset, int length, int totalLength);

        public void onResult(HttpResultEntity result);
    }