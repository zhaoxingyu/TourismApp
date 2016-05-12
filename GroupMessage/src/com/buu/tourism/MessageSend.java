package com.buu.tourism;

import android.util.Log;
import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

import com.buu.tourism.cache.PushConfig;

/**
 * 发送消息
 * @author fenglei
 *
 */
public class MessageSend {

	// 单利

	private static MessageSend instance = null;

	private MessageSend() {

	}

	public synchronized static MessageSend getInstance() {
		if (instance == null) {
			instance = new MessageSend();
		}
		return instance;
	}

	public void request(MessageModel model) {
		model.time = Long.toString(System.currentTimeMillis());
		request(model.toJsonStr());
	}

	public void request(final String json) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				JPushClient jpushClient = new JPushClient(PushConfig.getMasterSecretKey(), PushConfig.getAppKey(),
						3);

				// For push, all you need do is to build PushPayload object.
				PushPayload payload = PushPayload
						.newBuilder()
						.setPlatform(Platform.android_ios())
						.setAudience(Audience.all())
						.setMessage(
								Message.newBuilder().setMsgContent(MSG_CONTENT)
										.addExtra("data", json).build())
						.build();

				try {
					PushResult result = jpushClient.sendPush(payload);
					Log.d("SEDN", "Got result - " + result);

				} catch (APIConnectionException e) {
					// Connection error, should retry later
					Log.d("SEDN",
							"Connection error, should retry later"
									+ e.toString());

				} catch (APIRequestException e) {
					// Should review the error, and fix the request
					Log.d("SEDN",
							"Should review the error, and fix the request"
									+ e.toString());
					Log.d("SEDN", "HTTP Status: " + e.getStatus());
					Log.d("SEDN", "Error Code: " + e.getErrorCode());
					Log.d("SEDN", "Error Message: " + e.getErrorMessage());
				}
			}

		}).start();
		;
	}

	private static final String appKey = "c18460c7e7e13a3ce2ad2bc7";
	private static final String masterSecret = "6e02f7b6eb4223d256c4bfab";

	private final String MSG_CONTENT = "您有一条新短消息";

	// private final String ALERT = "这个是标题";
	// private PushPayload buildPushObject_all_all_alert() {
	// return PushPayload.alertAll(ALERT);
	// }
}