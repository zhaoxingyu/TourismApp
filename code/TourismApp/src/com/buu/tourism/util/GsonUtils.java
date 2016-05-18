package com.buu.tourism.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

/**
 * Gson转换工具.
 * 
 * @author zhaocongying zhaocongying@baidu.com
 * 
 */
public class GsonUtils {
    /**
     * 将一个obj转换成json字符串.
     */
    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * 将json字符串转换成对象.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    /**
     * 将json数组字符串转换成list对像.
     * 
     * @param s json数组字符串
     */
    public static <T> List<T> fromJsonArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return new ArrayList(Arrays.asList(arr)); // or return Arrays.asList(new
        // Gson().fromJson(s, clazz)); for a
        // one-liner
    }
}
