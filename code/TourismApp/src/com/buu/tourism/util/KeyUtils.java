package com.buu.tourism.util;

public class KeyUtils {
    
    static
    {
        System.loadLibrary("msk");
    }
    
    /**
     * 获得des算法的key
     * @return
     */
    public static native String getDesKey();
    
    /**
     * 获得MD5加密的key
     * @return
     */
    public static native String getMd5Key();
}
