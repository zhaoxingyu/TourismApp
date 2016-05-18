package com.buu.tourism.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.buu.tourism.R;
import com.buu.tourism.conf.Setting;
import com.buu.tourism.loader.UriConfig;

public class TipsActivity extends Activity implements android.view.View.OnClickListener{
    private static final String KEY_SHOW_TIPS = "key_show_tips";
    List<View> listViews = new ArrayList<View>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        ViewPager pager = (ViewPager) findViewById(R.id.activity_tips_pager);
        View tipView1 = View.inflate(this, R.layout.layout_tip1, null);
        View tipView2 = View.inflate(this, R.layout.layout_tip2, null);
        View tipView3 = View.inflate(this, R.layout.layout_tip3, null);
        listViews.add(tipView1);
        listViews.add(tipView2);
        listViews.add(tipView3);
        tipView3.findViewById(R.id.layout_tip3_btn_pass_in).setOnClickListener(this);;
        pager.setAdapter(new PagerAdapter() {
            
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(listViews.get(position));
            }
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }
            
            @Override
            public int getCount() {
                return listViews.size();
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View item = listViews.get(position);
                container.addView(item);
                return item;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Setting.setPref(this, KEY_SHOW_TIPS, false);
        Intent intent = new Intent(Intent.ACTION_VIEW,UriConfig.getSplashPageUri());
        startActivity(intent);
        finish();
    }
}
