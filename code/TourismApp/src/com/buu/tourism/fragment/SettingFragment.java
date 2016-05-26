package com.buu.tourism.fragment;

import java.io.File;
import java.text.DecimalFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buu.tourism.R;
import com.buu.tourism.ResConfig;
import com.buu.tourism.TourismApplication;
import com.buu.tourism.User;
import com.buu.tourism.cache.CacheManager;
import com.buu.tourism.loader.UriConfig;
import com.buu.tourism.util.DialogUtil;
import com.buu.tourism.util.FileUtil;
import com.buu.tourism.util.UiUtil;
import com.buu.tourism.view.MyViewCreator;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

public class SettingFragment extends BaseFragment implements OnClickListener {

    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = getActivity();
        View myFragment = inflater.inflate(R.layout.setting_fragment_layout, container, false);
        ViewGroup mContainer = (ViewGroup) myFragment.findViewById(R.id.setting_layout_list);

        mContainer.addView(MyViewCreator.createDividerLineView(context));
        mContainer.addView(MyViewCreator.createDividerGapView(context, 10));
        mContainer.addView(MyViewCreator.createDividerLineView(context));

        mContainer.addView(MyViewCreator.createMsgView(context, R.id.setting_item_portrait, this));
        mContainer.addView(MyViewCreator.createDividerLineMinorView(context));

        mContainer.addView(MyViewCreator.createSettingViewItem(context, R.id.setting_item_nickname, this));

        mContainer.addView(MyViewCreator.createDividerLineView(context));
        mContainer.addView(MyViewCreator.createDividerGapView(context, 15));
        mContainer.addView(MyViewCreator.createDividerLineView(context));

        mContainer.addView(MyViewCreator.createSettingViewItem(context, R.id.setting_item_check_update, this));
        mContainer.addView(MyViewCreator.createDividerLineMinorView(context));

        mContainer.addView(MyViewCreator.createSettingViewItem(context, R.id.setting_item_clean_cache, this));
        mContainer.addView(MyViewCreator.createDividerLineMinorView(context));

        mContainer.addView(MyViewCreator.createSettingViewItem(context, R.id.setting_item_feedback, this));
        mContainer.addView(MyViewCreator.createDividerLineMinorView(context));

        mContainer.addView(MyViewCreator.createSettingViewItem(context, R.id.setting_item_aboutus, this));
        mContainer.addView(MyViewCreator.createDividerLineView(context));
        
        mContainer.addView(MyViewCreator.createDividerGapView(context, 10));
        mContainer.addView(MyViewCreator.createDividerLineView(context));
        
        View qcode = MyViewCreator.createSettingViewItem(context, R.id.setting_item_qcode, this);
        ((TextView)qcode.findViewById(R.id.textview1)).setText(R.string.setting_item_unknowed);;
        mContainer.addView(qcode);
        mContainer.addView(MyViewCreator.createDividerLineView(context));

        return myFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calculateDirSize();
        initValues();
    }

    private void initValues() {
        View nickView = getView().findViewById(R.id.setting_item_nickname);
        TextView tvName = (TextView) nickView.findViewById(R.id.tip_txt);
        tvName.setVisibility(View.VISIBLE);
        tvName.setText(User.name);

        ImageView portraitView = (ImageView) getView().findViewById(R.id.tip_image);
        int drawableId = ResConfig.getInstance().getDrawableId(User.hid);
        portraitView.setImageResource(drawableId);
    }

    @Override
    protected String getPageTitle() {
        return getActivity().getString(R.string.action_settings);
    }

    private void calculateDirSize() {
        View cacheView = getView().findViewById(R.id.setting_item_clean_cache);
        final TextView tvCacheTips = (TextView) cacheView.findViewById(R.id.tip_txt);
        tvCacheTips.setVisibility(View.VISIBLE);
        tvCacheTips.setText("计算中...");
        new Thread() {
            public void run() {
                File internalCacheDir = CacheManager.getInstance().getInternalCacheDir();
                File externalCacheDir = CacheManager.getInstance().getExternalCacheDir();
                double dirSize = FileUtil.calculateDirSize(internalCacheDir, externalCacheDir);
                DecimalFormat df = new DecimalFormat("#0.00");
                String size = df.format(dirSize) + "KB";
                if (dirSize > 1000) {
                    dirSize = dirSize / 1000;
                    size = df.format(dirSize) + "MB";
                }
                final String dirTotalSize = size;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        tvCacheTips.setText(dirTotalSize);
                    }
                });
            };
        }.start();
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();

        switch (id) {
        case R.id.setting_item_portrait:
            Intent intent1 = new Intent(Intent.ACTION_VIEW, UriConfig.getSelectPortraitUri());
            startActivityForResult(intent1, 1001);
            break;
        case R.id.setting_item_nickname:
            View view = View.inflate(getContext(), R.layout.dialog_edittext, null);
            final EditText editText = (EditText) view.findViewById(R.id.dialog_edittext);
            DialogUtil.creatDefaultDialog(getContext(), view, new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String string = editText.getText().toString();
                    if (string.length() > 0) {
                        User.name = string;
                        initValues();
                    } else {
                        UiUtil.showToast("昵称不能为空!");
                        v.performClick();
                    }
                }
            }).show();;
            break;
        case R.id.setting_item_check_update:
            UmengUpdateAgent.update(getContext());
            break;
        case R.id.setting_item_clean_cache:
            DialogUtil.creatDefaultMsgDialog(getActivity(), getActivity().getString(R.string.setting_item_clean_cache),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread() {
                                public void run() {
                                    CacheManager.getInstance().cleanAllCacheDir();
                                    TourismApplication.getInstance().getUIHandler().post(new Runnable() {

                                        @Override
                                        public void run() {
                                            calculateDirSize();
                                            UiUtil.showToast("清理完毕");
                                        }
                                    });
                                };
                            }.start();
                        }
                    }).show();
            break;
        case R.id.setting_item_feedback:
            FeedbackAgent agent = new FeedbackAgent(getContext());
            agent.startFeedbackActivity();
            break;
        case R.id.setting_item_aboutus:
            Intent intent = new Intent(Intent.ACTION_VIEW, UriConfig.getSettingAboutusUri());
            getActivity().startActivity(intent);
            break;
        case R.id.setting_item_qcode:
            colorEggIndex++;
            if (colorEggIndex > 2) {
                ((TextView)v.findViewById(R.id.textview1)).setText(R.string.setting_item_qcode);;
                //TODO
                Toast.makeText(getContext(), "打开扫一扫功能", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "再点击"+ (3 - colorEggIndex) +"次，可激活未知功能", Toast.LENGTH_SHORT).show();
            }
            
            break;

        default:
            break;
        }
    }

    static int colorEggIndex = 0;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1001) {
            initValues();
        }
    }
}