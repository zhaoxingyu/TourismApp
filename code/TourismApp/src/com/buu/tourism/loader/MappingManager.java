package com.buu.tourism.loader;

import java.util.ArrayList;
import java.util.List;

import com.buu.tourism.GroupListActivity;
import com.buu.tourism.activity.MainActivity;
import com.buu.tourism.activity.SplashScreenActivity;
import com.buu.tourism.activity.TipsActivity;
import com.buu.tourism.fragment.AboutUsFragment;
import com.buu.tourism.fragment.DetailFragment;
import com.buu.tourism.fragment.ProgressWebViewFragment;
import com.buu.tourism.fragment.SelectPortaitFragment;
import com.buu.tourism.fragment.SettingFragment;
import com.buu.tourism.fragment.TestFragment;

public class MappingManager {

    private static final String TAG = MappingManager.class.getSimpleName();

    private MappingSpec mappingSpec;

    public MappingManager() {
    }

    public MappingSpec mappingSpec() {
        if (mappingSpec == null) {
            mappingSpec = read();
        }
        return mappingSpec;
    }

    public MappingSpec read() {
        List<PageSpec> pages = new ArrayList<PageSpec>();
        // webview页面
        pages.add(new PageSpec(UriConfig.HOST_WEBVIEW, ProgressWebViewFragment.class, null, false));
        // 主界面
        pages.add(new PageSpec(UriConfig.HOST_SPLASH, null, SplashScreenActivity.class, false));
        pages.add(new PageSpec(UriConfig.HOST_TIPS, null, TipsActivity.class, false));
        pages.add(new PageSpec(UriConfig.HOST_MAIN, null, MainActivity.class, false));
        pages.add(new PageSpec(UriConfig.HOST_TEST, TestFragment.class, null, false));
        pages.add(new PageSpec(UriConfig.HOST_DETAIL, DetailFragment.class, null, false));
        pages.add(new PageSpec(UriConfig.HOST_SETTING, SettingFragment.class, null, false));
        pages.add(new PageSpec(UriConfig.HOST_SELECT_PORTRAIT, SelectPortaitFragment.class, null, false));
        pages.add(new PageSpec(UriConfig.HOST_CHAT, null, GroupListActivity.class, false));
        
        pages.add(new PageSpec(UriConfig.HOST_SETTING_ABOUTUS, AboutUsFragment.class, null, false));

        MappingSpec mapping = new MappingSpec(LoaderActivity.class, pages.toArray(new PageSpec[pages.size()]));
        return mapping;
    }

    public boolean isSupport(String host) {
        MappingSpec mSpec = mappingSpec();
        return mSpec.getPage(host) != null;
    }

}
