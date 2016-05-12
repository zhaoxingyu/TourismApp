package com.buu.tourism.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.buu.tourism.R;
import com.buu.tourism.ResConfig;
import com.buu.tourism.User;

public class SelectPortaitFragment extends BaseFragment {

    @Override
    protected String getPageTitle() {
        return getActivity().getResources().getString(R.string.setting_item_select_portrait);
    }

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_portrait_fragment, null);
        GridView gridView = (GridView) view.findViewById(R.id.select_portrait_fragment_gridview);
        gridView.setAdapter(new MyGridViewAdapter());
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent();
                newIntent.putExtra("id", PortraitDataSource.ids.get(position));
                getActivity().setResult(Activity.RESULT_OK, newIntent);
                //FIX BUG 
                User.hid = ResConfig.getInstance().getKey(PortraitDataSource.ids.get(position));
                
                getActivity().finish();
            }
        });
        return view;
    }
    
    class MyGridViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return PortraitDataSource.ids.size();
        }

        @Override
        public Object getItem(int position) {
            return PortraitDataSource.ids.get(position);
        }

        @Override
        public long getItemId(int position) {
            return PortraitDataSource.ids.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(getContext(), R.layout.select_portrait_gridview_item, null);
                ImageView iv = (ImageView) convertView.findViewById(R.id.select_portrait_gridview_item_iv);
                viewHolder.iv = iv;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.iv.setImageResource(PortraitDataSource.ids.get(position));
            return convertView;
        }
    }
    
    static class ViewHolder{
        ImageView iv;
    }
    
    static class PortraitDataSource{
        public static List<Integer> ids = new ArrayList<Integer>();
        
        static{
            try {
                Class<?> RClass = Class.forName("com.buu.tourism.R$drawable");
                Field[] fs = RClass.getFields();
                for (int i = 0; i < fs.length; i++) {
                    Field f = fs[i];
                    String name = f.getName();
                    if (null != name && name.startsWith("h_")) {
                        int value = (Integer) f.get(RClass);
                        ids.add(value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
