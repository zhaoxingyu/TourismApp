package com.buu.tourism.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buu.tourism.net.engine.HttpRequestEntity;

/**
 * 基础网络请求页面
 * 
 * @author zhaoxingyu
 * 
 */
public abstract class BaseAccessorFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        
    }

    protected void beginRefresh() {
    }

    /**
     * 当前的Activity是否存活
     * 
     * @return
     */
    private boolean isGone() {
        return null == getCheckedActivity();
    }

    /**
     * 是否启用提示功能。如：加载中、加载失败的页面显示
     * 
     * @return
     */
    protected boolean enableBasePageTips() {
        return true;
    }

    /**
     * 是否启用数据缓存，缓存的有效时间为3天
     * 
     * @return
     */
    protected boolean enableCache() {
        return true;
    }

    protected HttpRequestEntity getRequestObj() {
        return null;
    };

}
