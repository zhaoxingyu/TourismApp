package com.buu.tourism.util;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;


public class WeakHandler extends Handler {
    private WeakReference<Activity> mActivity;

    public WeakHandler(Activity activity) {
        this.mActivity = new WeakReference<Activity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        if(mActivity.get()==null){
            return;
        }
    }
}
