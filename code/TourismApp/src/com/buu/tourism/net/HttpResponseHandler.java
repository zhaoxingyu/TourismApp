package com.buu.tourism.net;

import org.json.JSONObject;

import com.buu.tourism.bean.Bean;
import com.buu.tourism.net.engine.HttpResultEntity;
import com.buu.tourism.net.engine.IHttpResponseCallBack;
import com.google.gson.Gson;

/**
 * 将返回的结果直接转换成对象实体的回调方式
 * 
 * @author xingyu10
 *
 * @param <T>
 */
public abstract class HttpResponseHandler<T extends Bean> implements IHttpResponseCallBack {
    public Class<T> dstClass;

    public HttpResponseHandler(Class<T> c) {
        dstClass = c;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate(byte[] buffer, int offset, int length, int totalLength) {

    }

    @Override
    public final void onResult(HttpResultEntity result) {
        onResultBack(result);
    }

    void onResultBack(HttpResultEntity result) {
        String content = result.responseStr;
        T bean = null;
        try {
            bean = toBean(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != bean) {
            onSuccess(bean);
        } else {
            onFailure(result);
        }
    }

    protected abstract void onFailure(HttpResultEntity result);

    protected abstract void onSuccess(T bean);

    T toBean(Object content) throws Exception {
        JSONObject json = null;
        T fromJson = null;
        try {
            if (content instanceof String) {
                json = new JSONObject((String) content);
            } else if (content instanceof JSONObject) {
                json = (JSONObject) content;
            }
            Gson gson = new Gson();
            fromJson = gson.fromJson(json.toString(), dstClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fromJson;
    }

}