package com.buu.tourism.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buu.tourism.R;

public class ProgressWebViewFragment extends BaseFragment {

    public static final String ACTION_EXTRA_URL = "action_extra_url";
    public static final String ACTION_EXTRA_TITLE = "action_extra_title";
    private WebView mWebview;
    private String url;
    private String title;

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progress_webview, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        url = getActivity().getIntent().getStringExtra(ACTION_EXTRA_URL);
        title = getActivity().getIntent().getStringExtra(ACTION_EXTRA_TITLE);
        TextView tvTitle = (TextView) view.findViewById(R.id.main_top_title_bar_title);
        tvTitle.setText(title);
        
        final ProgressBar progressBar = (ProgressBar) view
                .findViewById(R.id.progressBar1);
        mWebview = (WebView) view.findViewById(R.id.webview1);
        mWebview.setWebChromeClient(new WebChromeClient() {
            int lastp = 0;
            final int REFRESH_FREQ = 4;

            // 将进度条的100份 等分，线性刷新
            public void onProgressChanged(WebView view, int progress) {
//                System.out.println("onProgressChanged: " + progress);
                progressBar.setVisibility(View.VISIBLE);
                
                // fix bug: handler NullPointerException
                Handler handler = view.getHandler();
                if (handler == null) {
                    return;
                }
                
                for (int i = lastp; i <= progress; i++) {
                    final int c = i;
                    
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(c);
                            if (c == 100) {
                                progressBar.setVisibility(View.GONE);
                            }
//                            System.out.println("progressBar.setProgress : " + c);
                        }
                    }, REFRESH_FREQ * i);
                }
                lastp = progress;
            }
        });
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
              super.onPageFinished(view, url);
//              System.out.println("onPageFinished :" + url);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
              super.onPageStarted(view, url, favicon);
//              System.out.println("onPageStarted :" + url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 设置点击网页里面的链接还是在当前的webview里跳转
                if (!TextUtils.isEmpty(url)) {
                    if (url.startsWith("tel")) {
                        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse(url)); 
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
                        startActivity(intent);
                    } else {
                        view.loadUrl(url);
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view,
                    SslErrorHandler handler, android.net.http.SslError error) {
                // 设置webview处理https请求
                handler.proceed();
            }

            public void onReceivedError(WebView view, int errorCode,
                    String description, String failingUrl) {
                // 加载页面报错时的处理
              if (null != mWebview) {
                getWebView().loadDataWithBaseURL(null, "加载失败...", "text/html", "uft-8", null);
              }
            }
        });

        doWebViewSetting();
        if (!TextUtils.isEmpty(url)) {
            mWebview.loadUrl(url);
        }
    }

    protected void doWebViewSetting() {
        // 解决图片无法显示
        mWebview.getSettings().setJavaScriptEnabled(true);
        // 加上以下设置，防止以后出现页面需要定位而无法显示
        mWebview.getSettings().setGeolocationEnabled(true);
        mWebview.getSettings().setDomStorageEnabled(true);
//        // 设置可以支持缩放
        mWebview.getSettings().setSupportZoom(true);
//        // 设置默认缩放方式尺寸是far
        mWebview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        // 设置出现缩放工具
        mWebview.getSettings().setBuiltInZoomControls(true);
        // 让网页自适应屏幕宽度
        mWebview.getSettings()
                .setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
    }

    protected WebView getWebView() {
        return mWebview;
    }

    @Override
    protected String getPageTitle() {
        return "";
    }
    @Override
    public void onBackPressed() {
        if (null != mWebview && mWebview.canGoBack()) {
            mWebview.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
