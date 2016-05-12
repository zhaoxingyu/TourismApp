package com.buu.tourism;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;

import com.buu.tourism.cache.DBCacheManager;
import com.buu.tourism.cache.PushConfig;
import com.buu.tourism.conf.Setting;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;

public class TourismApplication extends Application {

    private static TourismApplication mInstance;
    private static Handler mUIHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mUIHandler = new Handler(Looper.getMainLooper());
        Setting.init(this);
        initScreen();
        initImageLoader();
        initUmeng();

        initChatRoom();
    }

    private void initChatRoom() {
        User.init(this);
        ResConfig.getInstance().initRes(this);
        DBCacheManager.getInstance().initDB(this);
        PushConfig.init(this);
    }

    /**
     * 初始化umeng相关
     */
    private void initUmeng() {
        MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        // MobclickAgent.setAutoLocation(true);
        // MobclickAgent.setSessionContinueMillis(1000);
    }

    /**
     * 初始化imageloader
     */
    private void initImageLoader() {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).memoryCacheSize((int) Runtime.getRuntime().totalMemory()).build();
        ImageLoader.getInstance().init(configuration);
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
