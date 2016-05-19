package com.buu.tourism.loader;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.buu.tourism.activity.MainActivity;
import com.buu.tourism.activity.SplashScreenActivity;

public class MappingManager {

    private static final String TAG = MappingManager.class.getSimpleName();

    private final Context context;
    private MappingSpec mappingSpec;

    public MappingManager(Context ctx) {
        this.context = ctx;
    }

    public MappingSpec mappingSpec() {
        if (mappingSpec == null) {
            mappingSpec = read();
        }
        return mappingSpec;
    }

    public MappingSpec read() {
        List<PageSpec> pages = new ArrayList<PageSpec>();
        // 主界面
        pages.add(new PageSpec(UriConfig.HOST_SPLASH, null, SplashScreenActivity.class, false));
        pages.add(new PageSpec(UriConfig.HOST_MAIN, null, MainActivity.class, false));

        MappingSpec mapping = new MappingSpec(LoaderActivity.class, pages.toArray(new PageSpec[pages.size()]));
        return mapping;
    }

    public boolean isSupport(String host) {
        MappingSpec mSpec = mappingSpec();
        return mSpec.getPage(host) != null;
    }

}
