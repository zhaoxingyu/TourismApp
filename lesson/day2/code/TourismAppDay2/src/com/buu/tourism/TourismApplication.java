package com.buu.tourism;

import com.buu.tourism.conf.Setting;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;

public class TourismApplication extends Application {

    private static TourismApplication mInstance;
    private static Handler mUIHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mUIHandler = new Handler(Looper.getMainLooper());
        Setting.init(this);
    }


    /**
     * 获取全局的Context实例
     * 
     * @return
     */
    public static TourismApplication getInstance() {
        return mInstance;
    }

    /**
     * 初始化设备屏幕信息
     */
    private void initScreen() {
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        Constant.SCREEN_WIDTH = metrics.widthPixels;
        Constant.SCREEN_HEIGHT = metrics.heightPixels;
        Constant.DENSITY = metrics.density;
        Constant.DENSITYDPI = metrics.densityDpi;
        Constant.SCALEDDENSITY = metrics.scaledDensity;
    }

    /**
     * 获取主线程handler
     * 
     * @return
     */
    public Handler getUIHandler() {
        return mUIHandler;
    }

}
