package com.buu.tourism.util;

import com.buu.tourism.R;

public class ResourceMapping {

    public static int getSettingStringFromId(int id) {
        switch (id) {
        case R.id.setting_item_portrait:
            return R.string.setting_item_portrait;
        case R.id.setting_item_nickname:
            return R.string.setting_item_nickname;
        case R.id.setting_item_check_update:
            return R.string.setting_item_check_update;
        case R.id.setting_item_clean_cache:
            return R.string.setting_item_clean_cache;
        case R.id.setting_item_feedback:
            return R.string.setting_item_feedback;
        case R.id.setting_item_aboutus:
            return R.string.setting_item_aboutus;
        default:
            return R.string.action_settings;
        }
    }

    public static int getIconResFromId(int id) {
        switch (id) {
        case R.id.setting_item_portrait:
            return R.drawable.ic_avatar;
        default:
            return R.drawable.ic_avatar;
        }
    }

}