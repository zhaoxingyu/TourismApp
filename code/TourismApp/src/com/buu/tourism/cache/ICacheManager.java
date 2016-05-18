package com.buu.tourism.cache;

import java.io.File;

public interface ICacheManager {

    /**
     * 获取内部的缓存目录/data/data/pkg/
     * 
     * @return
     */
    public File getInternalCacheDir();

    /**
     * 获取外部的缓存目录 /sdcard/Android/data/pkg/cache
     * 
     * @return
     */
    public File getExternalCacheDir();

    /**
     * 清理外部的缓存目录/sdcard/Android/data/pkg/cache
     * 
     * @return
     */
    public boolean cleanExternalCacheDir();

    /**
     * 清理内部的缓存目录 /data/data/pkg/
     * 
     * @return
     */
    public boolean cleanInternalCacheDir();

    /**
     * 清除所有缓存数据
     * 
     * @param customDir
     * @return
     */
    public boolean cleanAllCacheDir(String... customDir);

}
