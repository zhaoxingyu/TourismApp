package com.buu.tourism;

import android.content.Context;
import android.util.Log;


public class User {

	public static String id = "-1" ;
	
	public static String hid = "0010" ;
	
	public static String name = "小磊" ;
	
	
	public static void init(Context context){
		new DeviceUuidFactory( context ) ; 
		id = DeviceUuidFactory.uuid.toString() ; 
		Log.d("UUID", id) ;
	}
	
}
