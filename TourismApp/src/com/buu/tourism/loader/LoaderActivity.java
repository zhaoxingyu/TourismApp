package com.buu.tourism.loader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.buu.tourism.conf.Setting;
import com.buu.tourism.fragment.BaseFragment;

/**
 * 负责装载Fragment的容器
 * 
 * <p>
 * 错误对照表: <br>
 * - 400 uri为空<br>
 * - 401 fragmentName为空<br>
 * - 402 遇到exception<br>
 * 
 * @author xingyu10
 * 
 */
public class LoaderActivity extends FragmentActivity implements OnClickListener{

    private FrameLayout rootView;
    private Fragment rootFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = new FrameLayout(this);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        rootView.setId(getRootViewID());
        setContentView(rootView);

        Uri uri = getIntent().getData();
        if (uri == null) {
            setError(400, null);
            return;
        }

        String fragmentName = uri.getFragment();
        if (fragmentName == null) {
            setError(401, null);
            return;
        }

        if (savedInstanceState != null) {
            return;
        }
        
        boolean needLogin = getIntent().getBooleanExtra("_login", true);
        boolean isLogined = Setting.isUserLogin();
        if (needLogin && !isLogined) {
            finish();
            
            return;
        }

        try {
            rootFragment = (Fragment) getClassLoader().loadClass(fragmentName).newInstance();
        } catch (Exception e) {
            setError(402, e);
            Log.e("loader", "load fragment failed", e);
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(getRootViewID(), rootFragment);
        // ft.add(getRootViewID(), rootFragment);
        ft.commit();

    }

    /**
     * 提供根布局的ID
     * 
     * @return
     */
    protected int getRootViewID() {
        return android.R.id.primary;
    }

    public Fragment getRootFragment() {
        return rootFragment;
    }

    /**
     * 加载页面遇到错误时候的处理
     * 
     * @param errorCode
     * @param e
     */
    protected void setError(int errorCode, Exception e) {
        rootView.removeAllViews();
        TextView text = new TextView(this);
        text.setText("载入页面失败 (" + (errorCode > 0 ? errorCode : -1) + ")");
        if (e != null) {
            text.append("\n");
            text.append(e.toString());
        }
        text.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        rootView.addView(text);
    }
    
    @Override
    public void onBackPressed() {
        if (rootFragment instanceof BaseFragment) {
            BaseFragment bf = (BaseFragment) rootFragment;
            bf.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        if (rootFragment instanceof BaseFragment) {
            BaseFragment bf = (BaseFragment) rootFragment;
            bf.onClick(v);
        }
    }
    
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
    }
}
