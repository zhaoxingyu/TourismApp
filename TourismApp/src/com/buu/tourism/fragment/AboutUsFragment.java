package com.buu.tourism.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buu.tourism.R;

public class AboutUsFragment extends BaseFragment {

    @Override
    protected String getPageTitle() {
        return getActivity().getResources().getString(R.string.setting_item_aboutus);
    }

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_us, null);
        TextView tvVersion = (TextView) view.findViewById(R.id.my_about_us_version);
        CharSequence ver = view.getContext().getResources().getText(R.string.app_version_name);
        tvVersion.append(ver);
        return view;
    }
}
