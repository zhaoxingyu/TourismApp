package com.buu.tourism.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

import com.buu.tourism.R;

public class PackageInfoUtil {

    public static String getMetaDataValue(Context cotext, String name) {
        String value = null;
        PackageManager packageManager = cotext.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(cotext.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.getString(name);
            }
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not read the name in the manifest file.", e);
        }
        if (value == null) {
            throw new RuntimeException("The name '" + name + "' is not defined in the manifest file's meta data.");
        }
        return value;
    }

    public static String getVersionName(Context context) {
        String version = "";
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo =
                    packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            version = packInfo.versionName;
            // fix bug for android 2.1: 在AndroidMainfset.xml中versionName如果是引用的资源文件中的字符串,在android 2.1上面会获取不到.
            if (TextUtils.isEmpty(version)) {
                version = context.getResources().getString(R.string.app_version_name);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
}
