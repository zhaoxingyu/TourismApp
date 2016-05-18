package com.buu.tourism.util;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;

public class DisplayUtil {

    public static int getStatusHeight(Activity act) { 
        if (null == act) {
            return 0;
        }
        //方式一
        Rect frame = new Rect();
        act.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        
        //方式二
        try {
            Class c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int h = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = act.getResources().getDimensionPixelSize(h);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    } 
	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */ 
	public static int px2dip(Context context, float pxValue) { 
		final float scale = context.getResources().getDisplayMetrics().density; 
		return (int) (pxValue / scale + 0.5f); 
	} 

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */ 
	public static int dip2px(Context context, float dipValue) { 
		final float scale = context.getResources().getDisplayMetrics().density; 
		return (int) (dipValue * scale + 0.5f); 
	} 

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */ 
	public static int px2sp(Context context, float pxValue) { 
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
		return (int) (pxValue / fontScale + 0.5f); 
	} 

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */ 
	public static int sp2px(Context context, float spValue) { 
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
		return (int) (spValue * fontScale + 0.5f); 
	} 
}
