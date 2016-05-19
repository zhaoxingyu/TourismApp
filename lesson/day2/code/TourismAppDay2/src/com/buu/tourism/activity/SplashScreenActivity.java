package com.buu.tourism.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.buu.tourism.R;
import com.buu.tourism.conf.Setting;
import com.buu.tourism.loader.UriConfig;

public class SplashScreenActivity extends Activity {
    private static final String KEY_SHOW_TIPS = "key_show_tips";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全屏.
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);

        boolean showTips = Setting.getBooleanPref(this, KEY_SHOW_TIPS, true);
        if (showTips) {
            Intent intent = new Intent(Intent.ACTION_VIEW, UriConfig.getTipsPageUri());
            SplashScreenActivity.this.startActivity(intent);
            SplashScreenActivity.this.finish();
        } else {
            setContentView(R.layout.activity_splash);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(Intent.ACTION_VIEW, UriConfig.getHomePageUri());
                    SplashScreenActivity.this.startActivity(intent);
                    SplashScreenActivity.this.finish();
                }
            }, 1500);
        }
    }

}
