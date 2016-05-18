package com.buu.tourism.cache;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.buu.tourism.GroupListActivity;
import com.buu.tourism.MessageActivity;
import com.buu.tourism.MessageModel;
import com.buu.tourism.R;
import com.buu.tourism.User;

public class DBCacheManager{

    /**
     * 数据库操作类
     */
    public DBDatabaseHelper db = null ; 
	
	// 单利
	private static DBCacheManager instance = null;

	public synchronized static DBCacheManager getInstance() {
		if (instance == null) {
			instance = new DBCacheManager();
		}
		return instance;
	}
	
	private DBCacheManager() { }
	
	public void initDB(Context context){
		db = new DBDatabaseHelper(context) ; 
	}
	
	
	public void insertMessage(Context context, MessageModel model ){

		// 新消息是当前群组消息， 需要刷新页面。 
		if(  ( MessageActivity.indexGroup != null && model.gid.equals( MessageActivity.indexGroup.id ) == true ) || model.uid.equals( User.id ) == true ){

			// 消息设置为已读
			model.read = "1" ;
			
			// 增加新消息
			MessageActivity.addMessage(model);
		} 
		// 未打开消当前消息页面，进行通知消息。
		else {
			
        	String service = Context.NOTIFICATION_SERVICE;  
        	nManager = (NotificationManager) context.getSystemService(service);  
		
			notification = new Notification();  
	        String tickerText = "测试Notifaction"; // 通知提示  
	        // 显示时间  
	        long when = System.currentTimeMillis();  
	  
	        notification.icon = R.drawable.ic_launcher;// 设置通知的图标  
	        notification.tickerText = tickerText; // 显示在状态栏中的文字  
	        notification.when = when; // 设置来通知时的时间  
	        notification.sound = Uri.parse("android.resource://com.sun.alex/raw/dida"); // 自定义声音  
//	        notification.flags = Notification.FLAG_NO_CLEAR; // 点击清除按钮时就会清除消息通知,但是点击通知栏的通知时不会消失  
//	        notification.flags = Notification.FLAG_ONGOING_EVENT; // 点击清除按钮不会清除消息通知,可以用来表示在正在运行  
//	        notification.flags |= Notification.FLAG_INSISTENT; // 一直进行，比如音乐一直播放，知道用户响应  
	        notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失  
	        notification.defaults = Notification.DEFAULT_SOUND; // 调用系统自带声音  
//	        notification.defaults = Notification.DEFAULT_SOUND;// 设置默认铃声  
//	        notification.defaults = Notification.DEFAULT_VIBRATE;// 设置默认震动  
//	        notification.defaults = Notification.DEFAULT_ALL; // 设置铃声震动  
//	        notification.defaults = Notification.DEFAULT_ALL; // 把所有的属性设置成默认  
	        
	        intent = new Intent( context,  MessageActivity.class);  
	        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//此处是为了只启动一个Activity
	        intent.addFlags(Intent.FILL_IN_DATA);
	        intent.putExtra("gid", model.gid) ; 
            // 获取PendingIntent,点击时发送该Intent  
            pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);  
            // 设置通知的标题和内容  
            notification.setLatestEventInfo( context, "您有新短消息~", model.name + ":" + model.mes, pIntent);  
            // 发出通知  
            nManager.notify(ID, notification);
		}
		
		db.addMessageModel(model);
		
		// 刷新数据，非标准写法
		if( GroupListActivity.instance != null )  GroupListActivity.instance.initData(); 
		
	}
	
	
	
	private Notification notification;  
    private NotificationManager nManager;  
    private Intent intent;  
    private PendingIntent pIntent;  
    private static final int ID = 1;  
	
    
    public void upreadMessageRead(String gid){
    	ArrayList<MessageModel> unreadMessageList = DBCacheManager.getInstance().db.getUnreadMessageList(gid) ;
    	for(MessageModel model : unreadMessageList){
    		model.read = "1";
    		db.updateMessageModel( model );
    	} 
    }
	
}
