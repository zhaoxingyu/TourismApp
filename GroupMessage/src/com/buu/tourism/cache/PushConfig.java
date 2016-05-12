package com.buu.tourism.cache;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import cn.jpush.android.api.JPushInterface;

public class PushConfig {
    private static String appkey;
    private static String masterSecret;
    private static Context context;

    public static void init(Context ctx) {
        if (null == ctx) {
            return;
        }
        context = ctx;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(ctx);
        try {
            ApplicationInfo appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            appkey = appInfo.metaData.getString("JPUSH_APPKEY");
            masterSecret = appInfo.metaData.getString("JPUSH_MASTERSECRE_TKEY");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getAppKey() {
        if (null == appkey) {
            init(context);
        }
        return appkey;
    }

    public static String getMasterSecretKey() {
        if (null == masterSecret) {
            init(context);
        }
        return masterSecret;
    }

}
