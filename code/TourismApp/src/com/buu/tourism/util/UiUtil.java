package com.buu.tourism.util;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.buu.tourism.TourismApplication;

public class UiUtil {

    /** 正在显示的提示。 */
    private static Toast showingToast = null;

    /**
     * 显示提示。
     * 
     * @param text 提示的内容。如果为 null 或者空白则隐藏当前显示。
     * 
     * @since 1.0
     */
    public static void showToast(final CharSequence text) {
        TourismApplication.getInstance().getUIHandler().post(new Runnable() {
            @Override
            public void run() {
                /*
                 * 检查是否需要显示。
                 */
                if (TextUtils.isEmpty(text)) {
                    if (null != showingToast) {
                        showingToast.cancel();
                    }
                    return;
                }

                /*
                 * 根据文本长短决定显示的时间长度。
                 */
                final int duration;
                if (text.length() <= 15) {
                    duration = Toast.LENGTH_SHORT;
                } else {
                    duration = Toast.LENGTH_LONG;
                }

                /*
                 * 如果存在实例则更改文本继续显示，否则创建新的对象。
                 */
                if (null != showingToast) {
                    showingToast.setText(text);
                    showingToast.setDuration(duration);
                } else {
                    showingToast = Toast.makeText(TourismApplication.getInstance(), text, duration);
                }
                showingToast.show();
            }
        });
    }

    /**
     * 显示提示。
     * 
     * @param text 提示的内容。如果为 0 则隐藏当前显示。
     * 
     * @since 1.0
     */
    public static void showToast(int text) {
        if (0 == text) {
            showToast(null);
        } else {
            showToast(TourismApplication.getInstance().getText(text));
        }
    }

    /**
     * 检查 {@link Activity} 是否可用。
     * 
     * @param activity 带检查的对象。
     * @return 如果目标为 null 或者已经处于销毁过程中返回 false 。
     * @author 于华
     * @since 1.0
     */
    public static boolean checkActivity(Activity activity) {
        if ((null == activity) || activity.isFinishing() || activity.isRestricted()) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed()) {
                return false;
            }
        }

        return true;
    }
}
