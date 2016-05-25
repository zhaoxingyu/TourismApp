package com.buu.tourism.loader;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.buu.tourism.util.LogUtil;

/**
 * 
 * 对外部URI Scheme请求做拦截，并重定向到指定页面。
 * 
 * 错误码：<br>
 * -402 重定向遇到异常exception<br>
 * -499 当前Application类型不符<br>
 * 
 * @author xingyu10
 *
 */
public class RedirectActivity extends Activity {

    private FrameLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = new FrameLayout(this);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        rootView.setId(android.R.id.primary);
        setContentView(rootView);

        doRedirect();
    }

    protected void doRedirect() {
        Intent orig = getIntent();
        Intent intent = new Intent(orig.getAction(), orig.getData());
        intent.putExtras(orig);
        intent = urlMap(intent);
        try {
            // 避免进入死循环
            List<ResolveInfo> l = getPackageManager().queryIntentActivities(intent, 0);
            if (l.size() == 1) {
                ResolveInfo ri = l.get(0);
                if (getPackageName().equals(ri.activityInfo.packageName)) {
                    if (getClass().getName().equals(ri.activityInfo.name)) {
                        throw new Exception("infinite loop");
                    }
                }
            } else if (l.size() > 1) {
                // should not happen, do we allow this?
            }
            startActivity(intent);
            LogUtil.debug("RedirectActivity : " + intent.toString());
            finish();
        } catch (Exception e) {
            setError(402, e);
            LogUtil.debug("RedirectActivity : unable to redirect " + getIntent(), e);
        }
    }

    private void setError(int errorCode, Exception e) {
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


    private MappingManager mappingManager;

    public MappingManager mappingManager() {
        if (mappingManager == null) {
            mappingManager = new MappingManager();
        }
        return mappingManager;
    }

    public Intent urlMap(Intent intent) {
        do {
            Uri uri = intent.getData();
            if (uri == null) {
                break;
            }
            if (uri.getScheme() == null || !UriConfig.PRIMARY_SCHEME.equals(uri.getScheme())) {
                break;
            }

            MappingManager mManager = mappingManager();
            if (mManager == null) {
                break;
            }

            MappingSpec mSpec = mManager.mappingSpec();
            if (mSpec == null) {
                break;
            }

            String host = uri.getHost();
            if (TextUtils.isEmpty(host))
                break;
            host = host.toLowerCase();

            PageSpec page = mSpec.getPage(host);
            if (page == null) {
                Log.w("loader", "host (" + host + ") Can't find the page in mapping.");
                break;
            }
            Class<?> fragment = page.fragment;

            intent.putExtra("_login", page.login);

            Class<?> defaultLoader = mSpec.loader;
            Class<?> loader = null;
            if (page.activity != null) {
                loader = page.activity;

            } else if (defaultLoader != null) {// defaultLoader is always null
                loader = defaultLoader;
            }

            if (loader != null) {
                intent.setClass(this, loader);

            } else {
                intent.setClass(this, LoaderActivity.class);
            }

            String query = uri.getQuery();

            uri =
                    Uri.parse(String.format("%s://%s?%s#%s", uri.getScheme(), host, query, fragment == null ? ""
                            : fragment.getName()));
            intent.setData(uri);

        } while (false);

        return intent;
    }

}
