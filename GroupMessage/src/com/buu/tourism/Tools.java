package com.buu.tourism;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONStringer;

import android.annotation.SuppressLint;

/**
 *
 * 项目名称: DNSCache <br>
 * 类名称: Tools <br>
 * 类描述: 一些常用的小方法 <br>
 * 创建人: fenglei <br>
 * 创建时间: 2015-4-21 下午5:26:04 <br>
 * 
 * 修改人: <br>
 * 修改时间: <br>
 * 修改备注: <br>
 * 
 * @version V1.0
 */
public class Tools {

	
	@SuppressLint("SimpleDateFormat")
	public static String getStringDateShort(String format, long time) {
		Date currentTime = new Date(time);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	
	@SuppressLint("SimpleDateFormat")
	public static String getStringDateShort(long time) {
		return getStringDateShort( "yyyy-MM-dd HH:mm:ss" , time);
	}

	public static String getStringDateShort(String time) {
		return getStringDateShort(Long.valueOf(time));
	}

	public String generateJsonStrFromMap(HashMap<String, String> map) {
		JSONStringer jsonStringer = new JSONStringer();
		try {
			jsonStringer = jsonStringer.object();
			Set<Entry<String, String>> entrySet = map.entrySet();
			for (Entry<String, String> entry : entrySet) {
				jsonStringer = jsonStringer.key(entry.getKey()).value(
						entry.getValue());
			}
			jsonStringer = jsonStringer.endObject();
		} catch (JSONException e) {
			e.printStackTrace();
			return "{}";
		}
		return jsonStringer.toString();
	}

}
