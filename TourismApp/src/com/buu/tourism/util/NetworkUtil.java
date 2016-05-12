package com.buu.tourism.util;

import java.io.BufferedReader;
import java.io.FileReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkUtil {

	/**
	 * 检查是否wifi连接
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 检查是否有网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		boolean hasNetwork = false;
		NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (networkInfo != null) {
			hasNetwork = networkInfo.isAvailable();
		}
		return hasNetwork;
	}
	
	/**
	 * 获取WIFI的Mac地址
	 * @param context
	 * @return Wifi的BSSID即mac地址
	 */
	public static String getWifiBSSID(Context context) {
		if (context == null) {
			return null;
		}
		
		String mac = null;
		WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiMan.getConnectionInfo();
		if (info != null) {
			mac = info.getBSSID();// 获得本机的MAC地址
		}
		
		return mac;
	}
	
	/**
	 * 获取WIFI状态下的内网IP
	 * @param context
	 * @return IP地址字符串。如192.168.1.1。
	 */
	public static String getIpAddress(Context context) {
		if (context == null) {
			return null;
		}
		
		String ip = null;
		WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiMan.getConnectionInfo();
		if (info != null) {
			final int ipAddress = info.getIpAddress();

			// 获得IP地址的方法一：
			if (ipAddress != 0) {
			       ip = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." 
					+ (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
			}
		}
		
		return ip;
	}
	
	/**
	 * 从ARP Cache中获取曾经连接过的WIFI的Mac地址
	 * @return
	 */
	public static String getWifiMacFromARP() {
	    BufferedReader br = null;
	    try {
	        br = new BufferedReader(new FileReader("/proc/net/arp"));
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] splitted = line.split(" +");
	            if (splitted != null && splitted.length >= 4) {
	                String mac = splitted[3];
	                if (mac.matches("..:..:..:..:..:..")) {
	                    return mac;
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            br.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return null;
	}
}
