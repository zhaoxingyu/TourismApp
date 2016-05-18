package com.buu.tourism;

import com.buu.tourism.conf.Setting;

public class Constant {
    // 正式环境控制的总开关 1:修改此处的 Is_debug
    private static final boolean IS_DEBUG = false;
    
    public static boolean isDebug(){
        boolean isDebug = Setting.isTestDebugEnable(TourismApplication.getInstance());
        if (true == isDebug) {
            return true;
        }
        return IS_DEBUG;
    }

    public static final int RESULT_CODE_BDUSS_LOGIN = 0;
    public static int PAGESIZE = 10;
    public static final int SCREEN_1080_WIDTH = 1080;
    // 屏幕长宽
    public static double DENSITY = 1;
    public static double DENSITYDPI = 1;
    public static float SCALEDDENSITY = 0;
    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;

    public static int CUSTOMID_MOVIE = 170;

    public static int STATUS_BAR_HEIGHT = 0;
}
