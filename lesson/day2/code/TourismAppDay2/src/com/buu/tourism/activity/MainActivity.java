package com.buu.tourism.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.LinearLayout;

/**
 * Demonstrates combining a TabHost with a ViewPager to implement a tab UI that
 * switches between tabs and also allows the user to perform horizontal flicks
 * to move between the tabs.
 */
public class MainActivity extends FragmentActivity{
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundColor(Color.GRAY);
        setContentView(linearLayout);
    }
}
