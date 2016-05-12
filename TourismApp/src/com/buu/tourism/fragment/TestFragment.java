package com.buu.tourism.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class TestFragment extends BaseFragment {

    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new LinearLayout(getContext());
    }

    @Override
    protected String getPageTitle() {
        return "测试页面";
    }

}