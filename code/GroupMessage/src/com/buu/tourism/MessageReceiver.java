package com.buu.tourism;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.buu.tourism.cache.DBCacheManager;

/**
 * 接收消息
 * @author fenglei
 *
 */
public class MessageReceiver extends BroadcastReceiver {

	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Bundle bundle = intent.getExtras();
		
		// 无效 字符串则返回
		String jsonStr = bundle.getString( "cn.jpush.android.EXTRA" ) ; 
		if( jsonStr == null || jsonStr.equals("") ) return ; 
		
		// 无效 json则返回
		MessageModel model = MessageModel.createModel(jsonStr) ; 
		if( model == null ) return ; 
		
		// 插入数据库
		DBCacheManager.getInstance().insertMessage(context, model);
		
		Log.d(TAG, "jsonStr = " +jsonStr) ; 
		Log.d(TAG, "model = " + model.toJsonStr()) ; 
		Log.d(TAG, "[MessageReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

}
