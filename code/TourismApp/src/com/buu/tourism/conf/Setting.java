package com.buu.tourism.conf;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.buu.tourism.TourismApplication;
import com.buu.tourism.util.Util;

public class Setting {

    private static SharedPreferences pref;

    /**
     * 在程序启动时初始化
     *
     * @param context
     */
    public static void init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null, must init at NuomiApplication.java");
        }
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setPref(Context context, String key, String value) {
        Editor edit = pref.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void setPref(Context context, String key, boolean value) {
        Editor edit = pref.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static void setPref(Context context, String key, int value) {
        Editor edit = pref.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void setPref(Context context, String key, long value) {
        Editor edit = pref.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static String getPref(Context context, String key) {
        return pref.getString(key, null);
    }

    public static boolean getBooleanPref(Context context, String key) {
        return pref.getBoolean(key, false);
    }

    public static boolean getBooleanPref(Context context, String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    public static int getIntPref(Context context, String key) {
        return pref.getInt(key, 0);
    }

    public static long getLongPref(Context context, String key) {
        return pref.getLong(key, 0);
    }

    public static void setTestHost(String host) {
        setPref(TourismApplication.getInstance(), "test_host", host);
    }

    public static void setTestDebugEnabled(boolean enable) {
        setPref(TourismApplication.getInstance(), "test_debug", enable);
    }

    public static void setTestLogEnabled(boolean enable) {
        setPref(TourismApplication.getInstance(), "test_log", enable);
    }

    public static String getTestHost() {
        return getPref(TourismApplication.getInstance(), "test_host");
    }

    public static boolean isTestDebugEnable(Context c) {
        return getBooleanPref(c, "test_debug", false);
    }

    public static boolean isTestLogEnable(Context c) {
        return getBooleanPref(c, "test_log", false);
    }

    public static boolean isUserLogin() {
        //TODO
        return true;
    }

    public static boolean enableMockData(String filePath) {
        return Util.enableTrick(filePath);
    }
}
