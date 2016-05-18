package com.buu.tourism.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.buu.tourism.R;
import com.buu.tourism.fragment.PagerFragment1;
import com.buu.tourism.fragment.PagerFragment2;
import com.buu.tourism.fragment.PagerFragment3;
import com.buu.tourism.fragment.PagerFragment4;
import com.buu.tourism.loader.UriConfig;

/**
 * Demonstrates combining a TabHost with a ViewPager to implement a tab UI that
 * switches between tabs and also allows the user to perform horizontal flicks
 * to move between the tabs.
 */
public class MainActivity extends FragmentActivity implements OnClickListener {
    TabHost mTabHost;
    ViewPager mViewPager;
    TabsAdapter mTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        View topBarView = findViewById(R.id.main_top_title_bar);
        initTopBarView(topBarView);
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager) findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

        mTabsAdapter.addTab(mTabHost.newTabSpec("simple").setIndicator(getResources().getString(R.string.main_pager_recommend)),
                PagerFragment1.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("contacts").setIndicator(getResources().getString(R.string.main_pager_town)),
                PagerFragment2.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("custom").setIndicator(getResources().getString(R.string.main_pager_country)),
                PagerFragment3.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("throttle").setIndicator(getResources().getString(R.string.main_pager_city)),
                PagerFragment4.class, null);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }

    private void initTopBarView(View topBarView) {
        ImageView ivLeft = (ImageView) topBarView.findViewById(R.id.main_top_title_bar_left_imageview);
        ImageView ivRight = (ImageView) topBarView.findViewById(R.id.main_top_title_bar_right_imageview);
        ImageView ivTitle = (ImageView) topBarView.findViewById(R.id.main_top_title_bar_ivtitle);
        TextView tvTitle = (TextView) topBarView.findViewById(R.id.main_top_title_bar_title);
        
        ivLeft.setImageResource(R.drawable.main_topbar_setting_selector);
        ivRight.setImageResource(R.drawable.main_topbar_chat_selector);
        ivTitle.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
        
        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    /**
     * This is a helper class that implements the management of tabs and all
     * details of connecting a ViewPager with associated TabHost. It relies on a
     * trick. Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show. This is not sufficient for switching
     * between pages. So instead we make the content part of the tab host 0dp
     * high (it is not shown) and the TabsAdapter supplies its own dummy view to
     * show as the tab content. It listens to changes in tabs, and takes care of
     * switch to the correct paged in the ViewPager whenever the selected tab
     * changes.
     */
    public static class TabsAdapter extends FragmentPagerAdapter implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id) {
        case R.id.main_top_title_bar_left_imageview:
            intent = new Intent(Intent.ACTION_VIEW, UriConfig.getSettingUri());
            startActivity(intent);
            break;
        case R.id.main_top_title_bar_right_imageview:
            intent = new Intent(Intent.ACTION_VIEW, UriConfig.getChatUri());
            startActivity(intent);
            break;

        default:
            break;
        }
    }
}
