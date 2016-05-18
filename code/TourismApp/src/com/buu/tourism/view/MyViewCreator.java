package com.buu.tourism.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.buu.tourism.R;
import com.buu.tourism.util.DisplayUtil;
import com.buu.tourism.util.ResourceMapping;

public class MyViewCreator {

    public enum CinemaDetailCategory {
        NORMAL, SPECIAL, EXPAND
    }

    public static View createMsgView(Context context, int id, OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.setting_fragment_item, null);
        ImageView iconView = (ImageView) view.findViewById(R.id.tip_image);
        int resId = ResourceMapping.getIconResFromId(id);
        iconView.setImageResource(resId);
        iconView.setVisibility(View.VISIBLE);
        TextView textView = (TextView) view.findViewById(R.id.textview1);
        int msgId = ResourceMapping.getSettingStringFromId(id);
        textView.setText(msgId);
        view.setId(id);
        view.setOnClickListener(listener);
        return view;
    }

    public static View createSettingViewItem(Context context, int id, OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.setting_fragment_item, null);
        ImageView iconView = (ImageView) view.findViewById(R.id.tip_image);
        iconView.setVisibility(View.GONE);
        TextView textView = (TextView) view.findViewById(R.id.textview1);
        int msgId = ResourceMapping.getSettingStringFromId(id);
        textView.setText(msgId);
        view.setId(id);
        view.setOnClickListener(listener);
        return view;
    }

    public static View createDividerGapView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.divider_gap, null);
    }

    public static View createDividerGapView(Context context, int h) {
        View view = LayoutInflater.from(context).inflate(R.layout.divider_gap, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(context, h));
        view.setLayoutParams(params);
        return view;
    }

    /**
     * 一级分割线，全屏幕宽度
     * 
     * @param context
     * @return
     */
    public static View createDividerLineView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.divider_line, null);
    }

    /**
     * 二级分割线，距离屏幕左侧留有部分间距
     * 
     * @param context
     * @return
     */
    public static View createDividerLineMinorView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.divider_line_minor, null);
    }

}
