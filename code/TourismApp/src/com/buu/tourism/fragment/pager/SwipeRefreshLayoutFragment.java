package com.buu.tourism.fragment.pager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.buu.tourism.Constant;
import com.buu.tourism.R;
import com.buu.tourism.TourismApplication;
import com.buu.tourism.bean.ScenicInfo;
import com.buu.tourism.bean.ScenicInfoBean;
import com.buu.tourism.conf.ApiConstant;
import com.buu.tourism.conf.Setting;
import com.buu.tourism.loader.UriConfig;
import com.buu.tourism.net.HttpAccessor;
import com.buu.tourism.net.HttpResponseHandler;
import com.buu.tourism.net.engine.HttpRequestEntity;
import com.buu.tourism.net.engine.HttpResultEntity;
import com.buu.tourism.net.engine.IHttpResponseCallBack;
import com.buu.tourism.util.AssetsUtil;
import com.buu.tourism.util.UiUtil;

/**
 * Example of using the SwipeRefreshLayout.
 */
public abstract class SwipeRefreshLayoutFragment extends Fragment implements OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private ListView mList;
    private Handler mHandler = new Handler();

    private final Runnable mRefreshDone = new Runnable() {

        @Override
        public void run() {
            mSwipeRefreshWidget.setRefreshing(false);
        }
    };
    List<ScenicInfo> mData = new ArrayList<ScenicInfo>();

    public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
        View refreshView = container.inflate(getContext(), R.layout.main_pager_fragment, null);
        mSwipeRefreshWidget = (SwipeRefreshLayout) refreshView.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshWidget.setColorScheme(R.color.color1, R.color.color2, R.color.color3, R.color.color4);
        mList = (ListView) refreshView.findViewById(R.id.content);
        mList.setDividerHeight(0);
        mList.setAdapter(new PagerAdapter(getContext(), mData));
        mList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PagerAdapter adapter = (PagerAdapter) mList.getAdapter();
                ScenicInfo item = adapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, UriConfig.getDetailUri());
                intent.putExtra("key", item);
                startActivity(intent);
            }
        });
        mSwipeRefreshWidget.setOnRefreshListener(this);
        return refreshView;

    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mData.size() < 1) {
            mSwipeRefreshWidget.post(new Runnable() {

                @Override
                public void run() {
                    mSwipeRefreshWidget.setRefreshing(true);
                }
            });
            refresh();
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    private String getServerHost() {
        String host = ApiConstant.HOST_ONLINE;
        String testHost = Setting.getTestHost();
        if (!TextUtils.isEmpty(testHost)) {
            return testHost;
        }
        if (Constant.isDebug()) {
            host = ApiConstant.HOST_TEST;
        } else {
            host = ApiConstant.HOST_ONLINE;
        }
        return host;
    }

    private void refresh() {
        // mHandler.removeCallbacks(mRefreshDone);
        // Future<?> request = HttpAccessor.getInstance().request(new
        // MockHttpReqeust(getmockData()), responseHandler);
        mHandler.removeCallbacks(mRefreshDone);
        String host = getServerHost();
        String url = host + "pager" + getPagerId();
        HttpRequestEntity httpResultEntity = new HttpRequestEntity(url);
        httpResultEntity.setForceString(true);
        Future<?> request = HttpAccessor.getInstance().request(httpResultEntity, responseHandler);
    }

    IHttpResponseCallBack responseHandler = new HttpResponseHandler<ScenicInfoBean>(ScenicInfoBean.class) {

        @Override
        protected void onSuccess(ScenicInfoBean bean) {
            mData.clear();
            if (null != bean && null != bean.data && null != bean.data.result) {
                ScenicInfo[] infos = bean.data.result;
                List<ScenicInfo> data = Arrays.asList(infos);
                mData.addAll(data);
            } else {
                UiUtil.showToast("没有数据");
            }
            BaseAdapter adapter = (BaseAdapter) mList.getAdapter();
            adapter.notifyDataSetChanged();
            mHandler.postDelayed(mRefreshDone, 0);
        }

        @Override
        protected void onFailure(HttpResultEntity result) {
            UiUtil.showToast("加载数据失败");
            mHandler.postDelayed(mRefreshDone, 0);
            mData.clear();
        }
    };

    protected String getmockData() {
        String assetsFileContent = AssetsUtil.getAssetsFileContent(TourismApplication.getInstance(), "scenic.json");
        return assetsFileContent;
    }

    protected abstract int getPagerId();
}