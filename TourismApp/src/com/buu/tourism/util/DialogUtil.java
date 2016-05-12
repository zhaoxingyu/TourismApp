package com.buu.tourism.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import com.buu.tourism.R;

public class DialogUtil {
    public static Dialog creatFullScreenDialog(Context c, View customView) {
        return createDialog(c, customView);
    }

    static Dialog createDialog(Context c, View view) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        Dialog dialog = new Dialog(c, R.style.full_screen_dialog);
        dialog.addContentView(view, params);
        enableDialogFullWidth(dialog);
        return dialog;
    }

    static void enableDialogFullWidth(Dialog dialog) {
        WindowManager windowManager = dialog.getWindow().getWindowManager();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        lp.width = width;
        dialog.getWindow().setAttributes(lp);
    }
    
    public static Dialog creatDefaultMsgDialog(Context c, String msg ,DialogInterface.OnClickListener l) {
        return new AlertDialog.Builder(c).setMessage(msg)
                .setPositiveButton(c.getString(R.string.confirm),l)
                .setNegativeButton(c.getString(R.string.cancel), null)
                .create();
    }
    
    public static Dialog creatDefaultDialog(Context c, View view ,DialogInterface.OnClickListener l) {
        return new AlertDialog.Builder(c).setView(view)
                .setPositiveButton(c.getString(R.string.confirm),l)
                .setNegativeButton(c.getString(R.string.cancel), null)
                .create();
    }
    
    public static Dialog creatDefaultMsgDialog(Context c, String msg) {
        return new AlertDialog.Builder(c).setMessage(msg)
                .setPositiveButton(c.getString(R.string.confirm),null).create();
    }
    
    public static ProgressDialog createProgressDialog(Context c, String msg) {
        return createProgressDialog(c, msg, false);
    }

    public static ProgressDialog createProgressDialog(Context c, String msg, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(c);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(cancelable);
        return progressDialog;
    }
}
