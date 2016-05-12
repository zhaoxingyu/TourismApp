package com.buu.tourism.cache;

import java.io.File;

import com.buu.tourism.TourismApplication;
import com.buu.tourism.util.FileUtil;

public class CacheManager implements ICacheManager{

    private static final Object lock = new Object();
    private static CacheManager minstance = new CacheManager();
    
    public static CacheManager getInstance(){
        return minstance;
    }
    @Override
    public File getInternalCacheDir() {
        return TourismApplication.getInstance().getCacheDir();
    }

    @Override
    public File getExternalCacheDir() {
        return TourismApplication.getInstance().getExternalCacheDir();
    }

    @Override
    public boolean cleanExternalCacheDir() {
        return FileUtil.deleteDir(getExternalCacheDir(),false);
    }

    @Override
    public boolean cleanInternalCacheDir() {
        return FileUtil.deleteDir(getInternalCacheDir(),false);
    }

    @Override
    public boolean cleanAllCacheDir(String... customDir) {
        return cleanExternalCacheDir() & cleanInternalCacheDir();
    }

}
