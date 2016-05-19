package com.buu.tourism.loader;

import android.net.Uri;

import com.buu.tourism.R;
import com.buu.tourism.TourismApplication;

public class UriConfig {
    // SCHEME
    public static final String PRIMARY_SCHEME = TourismApplication.getInstance().getString(R.string.app_redirect_scheme);
    public static final String PRIMARY_SCHEME_PROTOCOL = PRIMARY_SCHEME + "://";
    // 登录页面
    public static final String HOST_LOGIN = "login";
    // webview展示
    public static final String HOST_WEBVIEW = "webview";
    // 推荐
    public static final String HOST_RECOMMEND = "recommend";
    // 古镇
    public static final String HOST_TOWN = "town";
    // 乡村
    public static final String HOST_COUNTRY = "country";
    // 城市页
    public static final String HOST_CITY = "city";

    // splash页面
    public static final String HOST_SPLASH = "splash";
    // 提示页
    public static final String HOST_TIPS = "tips";
    // 首页
    public static final String HOST_MAIN = "main";

    // 详情页
    public static final String HOST_DETAIL = "detail";
    // 设置页
    public static final String HOST_SETTING = "setting";
    // 选择头像页
    public static final String HOST_SELECT_PORTRAIT = "portrait";
    // 聊天室
    public static final String HOST_CHAT = "chat";
    // 关于页面
    public static final String HOST_SETTING_ABOUTUS = "setting_aboutus";
    // 测试页面
    public static final String HOST_TEST = "test";

    /**
     * 登录页URI
     * 
     * @return
     */
    public static Uri getLoginUri() {
        return Uri.parse(PRIMARY_SCHEME_PROTOCOL + HOST_LOGIN);
    }

    /**
     * weibview页URI
     * 
     * @return
     */
    public static Uri getWebView() {
        return Uri.parse(PRIMARY_SCHEME_PROTOCOL + HOST_WEBVIEW);
    }

    /**
     * splash页URI
     * 
     * @return
     */
    public static Uri getSplashPageUri() {
        return Uri.parse(PRIMARY_SCHEME_PROTOCOL + HOST_SPLASH);
    }
    /**
     * tips页URI
     * 
     * @return
     */
    public static Uri getTipsPageUri() {
        return Uri.parse(PRIMARY_SCHEME_PROTOCOL + HOST_TIPS);
    }
    /**
     * 首页URI
     * 
     * @return
     */
    public static Uri getHomePageUri() {
        return Uri.parse(PRIMARY_SCHEME_PROTOCOL + HOST_MAIN);
    }

    /**
     * 调试配置页URI
     * 
     * @return
     */
    public static Uri getTestUri() {
        return Uri.parse(PRIMARY_SCHEME_PROTOCOL + HOST_TEST);
    }

    /**
     * 详情页URI
     * 
     * @return
     */
    public static Uri getDetailUri() {
        return Uri.parse(PRIMARY_SCHEME_PROTOCOL + HOST_DETAIL);
    }
    
    /**
     * 设置页URI
     * 
     * @return
     */
    public static Uri getSettingUri() {
        return Uri.parse(PRIMARY_SCHEME_PROTOCOL + HOST_SETTING);
    }
    /**
     * 选择头像URI
     * 
     * @return
     */
    public static Uri getSelectPortraitUri() {
        return Uri.parse(PRIMARY_SCHEME_PROTOCOL + HOST_SELECT_PORTRAIT);
    }
    
    /**
     * 聊天室URI
     * 
     * @return
     */
    public static Uri getChatUri() {
        return Uri.parse(PRIMARY_SCHEME_PROTOCOL + HOST_CHAT);
    }
    /**
     * 关于我们URI
     * 
     * @return
     */
    public static Uri getSettingAboutusUri() {
        return Uri.parse(PRIMARY_SCHEME_PROTOCOL + HOST_SETTING_ABOUTUS);
    }
}
