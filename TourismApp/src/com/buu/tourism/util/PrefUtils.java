package com.buu.tourism.util;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Prefs Utils. 将基本类型存储到Prefs中,或者将对象转换成json数据,然后存储到prefs中.
 * 
 * @author zhaocongying zhaocongying@baidu.com
 */
public class PrefUtils {

    /**
     * 获取Prefs
     * 
     * @param context
     * @param name
     *            Preference的名字.
     * @param mode
     */
    public static SharedPreferences getPrefs(Context context, String name,
            int mode) {
        return context.getSharedPreferences(name, mode);
    }

    /**
     * 获取Prefs
     * 
     * @param context
     * @param name
     *            Preference的名字.
     */
    public static SharedPreferences getPrefs(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * 将obj对象转换成json数据后,保存到prefs中.
     * 
     * @author zhaocongying zhaocongying@baidu.com
     */
    public static boolean putObject(SharedPreferences sp, String key, Object obj) {
        if (obj == null) {
            return false;
        }

        String json = GsonUtils.toJson(obj);
        return sp.edit().putString(key, json).commit();
    }

    /**
     * 将obj对象转换成json数据后,保存到prefs中.
     * 
     * @param context
     * @param prefName
     *            prefs的名字.
     * @param key
     *            json数据对应的key.
     * @param value
     *            json数据.
     * @return true,保存成功; false, 保存失败.
     * @author zhaocongying zhaocongying@baidu.com
     */
    public static boolean putObject(Context context, String prefName,
            String key, Object obj) {
        if (obj == null) {
            return false;
        }

        String json = GsonUtils.toJson(obj);
        return putString(context, prefName, key, json);
    }

    /**
     * 根据对应的key,获取对象(不支持数组).
     * 
     * @author zhaocongying zhaocongying@baidu.com
     */
    public static <T> T getObject(SharedPreferences sp, String key,
            Class<T> valueClazz, T defValue) {
        String json = sp.getString(key, null);
        if (TextUtils.isEmpty(json)) {
            return defValue;
        }

        return GsonUtils.fromJson(json, valueClazz);
    }

    /**
     * 根据对应的key,获取List对象(只支持数组,列表).
     * 
     * @author zhaocongying zhaocongying@baidu.com
     */
    public static <T> List<T> getList(SharedPreferences sp, String key,
            Class<T[]> valueClazz) {
        String json = sp.getString(key, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        return GsonUtils.fromJsonArray(json, valueClazz);
    }

    /**
     * 根据对应的key,获取对象(不支持数组).
     * 
     * @param context
     * @param prefName
     * @param key
     * @param defValue
     *            默认值.
     * @return
     * @author zhaocongying zhaocongying@baidu.com
     */
    public static <T> T getObject(Context context, String prefName, String key,
            Class<T> valueClazz, T defValue) {
        String json = getString(context, prefName, key, null);
        if (TextUtils.isEmpty(json)) {
            return defValue;
        }

        return GsonUtils.fromJson(json, valueClazz);
    }

    /**
     * 根据对应的key,获取List对象(只支持数组,列表).
     * 
     * @param context
     * @param prefName
     * @param key
     *            对应的key.
     * @param valueClazz
     *            class类型, 只能时数组类型.
     * @return
     * @author zhaocongying zhaocongying@baidu.com
     */
    public static <T> List<T> getList(Context context, String prefName,
            String key, Class<T[]> valueClazz) {
        String json = getString(context, prefName, key, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        return GsonUtils.fromJsonArray(json, valueClazz);
    }

    public static String getString(Context context, String prefName,
            String key, String defValue) {
        SharedPreferences sp = getPrefs(context, prefName, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static boolean putString(Context context, String prefName,
            String key, String value) {
        SharedPreferences sp = getPrefs(context, prefName);
        if (sp == null) {
            return false;
        } else {
            return sp.edit().putString(key, value).commit();
        }
    }

    public static int getInt(Context context, String prefName, String key,
            int defValue) {
        SharedPreferences sp = getPrefs(context, prefName, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static boolean putInt(Context context, String prefName, String key,
            int value) {
        SharedPreferences sp = getPrefs(context, prefName, Context.MODE_PRIVATE);
        if (sp == null) {
            return false;
        } else {
            return sp.edit().putInt(key, value).commit();
        }
    }

    public static float getFloat(Context context, String prefName, String key,
            float defValue) {
        SharedPreferences sp = getPrefs(context, prefName, Context.MODE_PRIVATE);
        return sp.getFloat(key, defValue);
    }

    public static boolean putFloat(Context context, String prefName,
            String key, float value) {
        SharedPreferences sp = getPrefs(context, prefName, Context.MODE_PRIVATE);
        if (sp == null) {
            return false;
        } else {
            return sp.edit().putFloat(key, value).commit();
        }
    }

    public static boolean getBoolean(Context context, String prefName,
            String key, boolean defValue) {
        SharedPreferences sp = getPrefs(context, prefName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static boolean putBoolean(Context context, String prefName,
            String key, boolean value) {
        SharedPreferences sp = getPrefs(context, prefName, Context.MODE_PRIVATE);
        if (sp == null) {
            return false;
        } else {
            return sp.edit().putBoolean(key, value).commit();
        }
    }

    public static long getLong(Context context, String prefName, String key,
            long defValue) {
        SharedPreferences sp = getPrefs(context, prefName, Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }

    public static boolean putLong(Context context, String prefName, String key,
            long value) {
        SharedPreferences sp = getPrefs(context, prefName, Context.MODE_PRIVATE);
        if (sp == null) {
            return false;
        } else {
            return sp.edit().putLong(key, value).commit();
        }
    }

    /** 删除一个key */
    public static boolean remove(Context context, String prefName, String key) {
        SharedPreferences sp = getPrefs(context, prefName, Context.MODE_PRIVATE);
        if (sp == null) {
            return false;
        } else {
            return sp.edit().remove(key).commit();
        }
    }

}
