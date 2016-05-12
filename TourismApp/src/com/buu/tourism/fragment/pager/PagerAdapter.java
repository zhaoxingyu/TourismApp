package com.buu.tourism.fragment.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.buu.tourism.R;
import com.buu.tourism.bean.ScenicInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PagerAdapter extends BaseAdapter {
    List<ScenicInfo> mListDatas = new ArrayList<ScenicInfo>();
    Context mContext;
    DisplayImageOptions options = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).build();
    public PagerAdapter() {
        
    }

    public PagerAdapter(Context c, List<ScenicInfo> data) {
        mContext = c;
        mListDatas = data;
    }

    @Override
    public int getCount() {
        return mListDatas.size();
    }

    @Override
    public ScenicInfo getItem(int position) {
        return mListDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mListDatas.get(position).sId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_pager_list_item, null);
//            int gap = DisplayUtil.dip2px(mContext, 14);
//            int width = (Constant.SCREEN_WIDTH - (3 * gap)) / 2;
//            int height = width * 13 / 9;
//            AbsListView.LayoutParams params = new AbsListView.LayoutParams(width, height);
//            convertView.setLayoutParams(params);
//            convertView.invalidate();
            holder.ivPoster = (ImageView) convertView.findViewById(R.id.poster);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.distance);
            holder.tvName = (TextView) convertView.findViewById(R.id.name);
            holder.tvNameAlias = (TextView) convertView.findViewById(R.id.nameAlias);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String posterUrl = mListDatas.get(position).posterUrl;
        String name = mListDatas.get(position).scenicName;
        long distance = mListDatas.get(position).distance;
        String nameAlias = mListDatas.get(position).scenicNameAlias;
        double price = mListDatas.get(position).price;

        ImageLoader.getInstance().displayImage(posterUrl, holder.ivPoster, options);
        holder.tvName.setText(name);
        holder.tvNameAlias.setText(nameAlias);
        String mile = "";
        if (distance > 1000) {
            mile = (distance / 1000) + "KM";
        } else {
            mile = distance + "m";
        }
        holder.tvDistance.setText(mile);
        holder.tvPrice.setText(price + "");

        return convertView;
    }

    class ViewHolder {
        public TextView tvPrice;
        public TextView tvNameAlias;
        public TextView tvName;
        public ImageView ivPoster;
        public TextView tvDistance;
    }
}