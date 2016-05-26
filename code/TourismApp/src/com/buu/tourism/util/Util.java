package com.buu.tourism.util;

import java.io.File;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class Util {

    /**
     * os version check. If apiVersion < 18, return false, else return true.
     */
    public static boolean osVersionCheck() {
        int apiVersion = android.os.Build.VERSION.SDK_INT;
        if (apiVersion < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return false;
        }
        return true;
    }

    /**
     * get client time
     * 
     * @return
     */
    public static String getClientTime() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * get phone mac address
     * 
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String macAddress = "";
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return macAddress;
        }
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info != null) {
            macAddress = info.getMacAddress();
        }
        return macAddress;
    }

    public static String getNetworkType(Context context) {
        String netTypeName = "";
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {
            netTypeName = info.getTypeName();
        }
        return netTypeName;
    }

    /**
     * get IMEI of GSM or MEID of CDMA
     * 
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String deviceId = "";
        TelephonyManager telephoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = telephoneManager.getDeviceId();
        return deviceId;
    }

    /**
     * get UUID
     * 
     * @param context
     * @return
     */
    public static String getUUid(Context context) {
        String uniqueId = "";
        TelephonyManager telephoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephoneManager.getDeviceId();
        if (deviceId == null) {
            deviceId = "0";
        }
        String simSerialNumber = telephoneManager.getSimSerialNumber();
        if (simSerialNumber == null) {
            simSerialNumber = "0";
        }
        String androidId =
                android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
        if (androidId == null) {
            androidId = "0";
        }
        UUID uuid =
                new UUID(androidId.hashCode(), (deviceId.hashCode() << 32)
                        | (simSerialNumber == null ? 0 : simSerialNumber.hashCode()));
        if (uuid != null) {
            uniqueId = uuid.toString();
        }
        return uniqueId;
    }

    public static boolean isCarrierOperatorWifi(String name) {
        Pattern sCarrierMobilePattern = Pattern.compile("CMCC|ChinaUnicom|ChinaNet", Pattern.CASE_INSENSITIVE);
        boolean result = false;
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        String m = trimAll(name.toString());
        Matcher matcher1 = sCarrierMobilePattern.matcher(m);
        if (matcher1.find()) {
            String lowerName = name.toLowerCase();
            if (lowerName.startsWith("cmcc") || lowerName.startsWith("chinaunicom") || lowerName.startsWith("chinanet")) {
                result = true;
            }
        }
        return result;
    }

    public static String trimAll(String s) {
        final int length = s.length();
        StringBuffer buffer = new StringBuffer();
        int position = 0;
        char currentChar;
        while (position < length) {
            currentChar = s.charAt(position++);
            if (currentChar > ' ') {
                buffer.append(currentChar);
            }
        }
        return buffer.toString();
    }

    /**
     * 手机号验证
     * 
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
    
    /**
     * 判断GPS是否打开
     * @param context
     * @return
     */
    public static boolean isGPSEnable(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    
    /**
     * 判断SDCard上是否存在改文件
     * @param filePath
     * @return
     */
    public static boolean enableTrick(String filePath) {
        boolean yes = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        if (yes) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            return new File(externalStorageDirectory, filePath).exists();
        } else {
            return false;
        }
        
    }
}
