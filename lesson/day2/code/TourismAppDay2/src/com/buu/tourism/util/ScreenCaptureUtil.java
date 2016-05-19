package com.buu.tourism.util;

import android.graphics.Bitmap;
import android.view.View;

import com.buu.tourism.Constant;

public class ScreenCaptureUtil {

    public static Bitmap getScreenBitmap(View v) {
        if (null == v || Constant.SCREEN_WIDTH > Constant.SCREEN_1080_WIDTH) {
            return null;
        }
        View view = v.getRootView();
        view.destroyDrawingCache();
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (null == bitmap) {
            return null;
        } else {
            int statusHeight = Constant.STATUS_BAR_HEIGHT;
            Bitmap noStatusBarScreenCapture =
                    Bitmap.createBitmap(bitmap, 0, statusHeight, bitmap.getWidth(), bitmap.getHeight() - statusHeight);
            Bitmap dst = ImageCompressUtil.compressBitmap(noStatusBarScreenCapture, 350);
            return dst;
        }
    }
}
