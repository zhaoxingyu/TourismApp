package com.buu.tourism;

import android.app.Application;

import com.buu.tourism.cache.DBCacheManager;
import com.buu.tourism.cache.PushConfig;

/**
 * 程序主入口
 * @author fenglei
 *
 */
public class MainApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		
		
		User.init(this);
		
		ResConfig.getInstance().initRes(this);
		DBCacheManager.getInstance().initDB(this);
		
		PushConfig.init(this);
	}
}