package com.nostra13.universalimageloader.cache.disc;

import android.text.TextUtils;

public enum DiskCacheFolder {

    /** 默认存储文件夹 */
    DEFAULT(".prenew", 1000),
    /** 预览图文件夹 */
    PRENEW(".prenew", 1000),
    /** 头像文件夹 */
    PORTRAIT(".portraitnew", 1000),
    /** 存放appMarket图片文件夹 */
    INTEREST(".interest", 1000),
    
    /** 大图文件夹 */
    ORIGIN(".weibo_pic_new"),
    /** 动态背景图文件夹 */
    LISTITEMBG(".listitembg"),
    /** composer文件夹 */
    COMPOSER(".composer");

    String folder;
    int capacity;
    static final int DEFAULT_CAPATICY = 400;

    DiskCacheFolder(String folder) {
        this(folder, DEFAULT_CAPATICY);
    }

    DiskCacheFolder(String folder, int limitedSize) {
        this.folder = folder;
        this.capacity = limitedSize;
    }

    /**
     * 获取缓存文件夹名称
     * 
     * @return
     */
    public String getFolderName() {
        return folder;
    }

    /**
     * 获取该目录下的最大文件个数
     * 
     * @return 文件个数
     */
    public int getCapacity() {
        return capacity;
    }

    public static DiskCacheFolder getCacheFolderByPath(String cacheDir) {
        DiskCacheFolder[] folders = values();
        DiskCacheFolder folder = DiskCacheFolder.DEFAULT;
        if (!TextUtils.isEmpty(cacheDir)) {
            for (DiskCacheFolder diskCacheFolder : folders) {
                String dir = diskCacheFolder.getFolderName();
                if (!TextUtils.isEmpty(dir) && cacheDir.indexOf(dir) > -1) {
                    folder = diskCacheFolder;
                    break;
                }
            }
        }
        return folder;
    }
}
