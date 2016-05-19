package com.buu.tourism.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buu.tourism.R;

/**
 * 基础页面类，其他的页面都需要至少集成此类
 * 
 * @author zhaoxingyu
 *
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.base_fragment, container, false);
        ViewGroup topBarContanier = (ViewGroup) rootView.findViewById(R.id.base_fragment_topbar);
        ViewGroup contnetContainer = (ViewGroup) rootView.findViewById(R.id.base_fragment_container);
        ViewGroup tipsContainer = (ViewGroup) rootView.findViewById(R.id.base_fragment_tips);
        createTopBarView(inflater, topBarContanier);
        View contentView = doCreateView(inflater, contnetContainer, savedInstanceState);
        if (null != contentView) {
            contnetContainer.addView(contentView);
        }
        return rootView;
    }

    protected View createTopBarView(LayoutInflater inflater, ViewGroup container) {
        View topBarView = inflater.inflate(R.layout.global_titlebar_layout, container);
        ImageView ivLeft = (ImageView) topBarView.findViewById(R.id.main_top_title_bar_left_imageview);
        ImageView ivRight = (ImageView) topBarView.findViewById(R.id.main_top_title_bar_right_imageview);
        ImageView ivTitle = (ImageView) topBarView.findViewById(R.id.main_top_title_bar_ivtitle);
        TextView tvTitle = (TextView) topBarView.findViewById(R.id.main_top_title_bar_title);

        ivLeft.setImageResource(R.drawable.topbar_back_selector);
        ivRight.setVisibility(View.GONE);
        ivTitle.setVisibility(View.GONE);

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getPageTitle());

        ivLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return topBarView;
    }

    /**
     * 获取标题
     * 
     * @return
     */
    protected abstract String getPageTitle();

    protected abstract View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public void onBackPressed() {
        getActivity().finish();
    }

    /**
     * 获取Activity, 有可能为null.
     * 
     * @return
     * @author zhaocongying zhaocongying@baidu.com
     * @date 2015年1月4日 下午1:52:25
     */
    public FragmentActivity getCheckedActivity() {
        // check activity is valid.
        FragmentActivity activity = getActivity();
        if (activity == null || activity.isFinishing() || activity.isRestricted()) {
            return null;
        }

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed()) {
                return null;
            }
        }

        return activity;
    }

    @Override
    public void onClick(View v) {
        // deliver to child
    }
}
