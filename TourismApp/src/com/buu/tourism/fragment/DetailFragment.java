package com.buu.tourism.fragment;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buu.tourism.R;
import com.buu.tourism.bean.ScenicInfo;
import com.buu.tourism.util.DialogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.fb.FeedbackAgent;

public class DetailFragment extends BaseFragment implements OnClickListener{

    private ScenicInfo info;

    @Override
    protected String getPageTitle() {
        return getActivity().getResources().getString(R.string.detail);
    }

    @Override
    protected View createTopBarView(LayoutInflater inflater, ViewGroup container) {
        View topBarView = super.createTopBarView(inflater, container);
        ImageView ivRight = (ImageView) topBarView.findViewById(R.id.main_top_title_bar_right_imageview);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.scenic_detail_share_selector);
        ivRight.setOnClickListener(this);
        return topBarView;
    }
    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, null);
        info = (ScenicInfo) getActivity().getIntent().getSerializableExtra("key");
        if (info == null) {
            getActivity().finish();
            return null;
        }
        ImageView ivPoster = (ImageView) view.findViewById(R.id.detail_fragment_poster);
        TextView tvName = (TextView) view.findViewById(R.id.detail_fragment_name);
        TextView tvPrice= (TextView) view.findViewById(R.id.detail_fragment_price);
        TextView tvAddr = (TextView) view.findViewById(R.id.detail_fragment_addr);
        TextView tvDesc = (TextView) view.findViewById(R.id.detail_fragment_desc);
        TextView tvIntro = (TextView) view.findViewById(R.id.detail_fragment_intro);
        TextView tvExpiry = (TextView) view.findViewById(R.id.detail_fragment_expiry);
        
        if (null != info) {
            ImageLoader.getInstance().displayImage(info.posterUrl, ivPoster);
            tvName.setText(info.scenicName);
            tvPrice.setText(info.price + "元");
            tvAddr.setText(info.scenicAddr);
            tvIntro.setText("简介："+info.intro);
            String begin = toShortDateString(info.promotionBegin);
            String end = toShortDateString(info.promotionEnd);
            tvExpiry.setText("有效期：" + begin + " - " + end);
            
        }
        
        view.findViewById(R.id.detail_fragment_bottom_dail).setOnClickListener(this);;
        view.findViewById(R.id.detail_fragment_bottom_map).setOnClickListener(this);;
        view.findViewById(R.id.detail_fragment_bottom_fav).setOnClickListener(this);;
        view.findViewById(R.id.detail_fragment_bottom_feedback).setOnClickListener(this);;
        return view;
    }
    
    public static String toShortDateString(long milliseconds) {
        Date date = new Date(milliseconds);
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd");
        return myFmt.format(date);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id) {
        case R.id.detail_fragment_bottom_dail:
            DialogUtil.creatDefaultMsgDialog(getActivity(), "是否拨打" + info.phoneNumber, new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String number = info.phoneNumber;
                    Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + number));
                    startActivity(intent); 
                }
            }).show();;
            break;
        case R.id.detail_fragment_bottom_map:
            String location = info.lat + "," + info.lng;
            String title = "目标位置";
            String content = info.scenicName;
            String url = "intent://map/marker?location=" + location + "&title=" + title + "&content=" + content
                    + "&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
            try{
                //移动APP调起Android百度地图方式举例
                intent = Intent.parseUri(url, 0);
                startActivity(intent); //启动调用 
            } catch (Exception e) {
                e.printStackTrace();
                //调用APP失败，在尝试调用浏览器
                url = "http://api.map.baidu.com/marker?location=" + location + "&title=" + title + "&content=" + content
                        + "&output=html&src=yourComponyName|yourAppName";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent); //启动调用 
            }
            break;
        case R.id.detail_fragment_bottom_fav:
            Toast.makeText(getContext(), "您赞了这条信息", Toast.LENGTH_SHORT).show();
            break;
        case R.id.detail_fragment_bottom_feedback:
            FeedbackAgent agent = new FeedbackAgent(getContext());
            agent.startFeedbackActivity();
            break;
            
        case R.id.main_top_title_bar_right_imageview:
            Intent sendIntent = new Intent();  
            sendIntent.setAction(Intent.ACTION_SEND);  
            sendIntent.putExtra(Intent.EXTRA_TEXT, info.posterUrl);  
            sendIntent.setType("text/plain");  
            startActivity(sendIntent);  
            break;

        default:
            break;
        }
        
    }
}
