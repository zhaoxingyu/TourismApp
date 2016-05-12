package com.buu.tourism.net.engine;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

import android.os.Bundle;

public class PostStreamComposer {


    /**
     * 将String字符串构建成post内容流
     * 
     * @param postContent
     * @return
     */
    public static InputStream buildStringInputStream(String postContent) {
        if (null != postContent) {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(postContent.getBytes("UTF-8"));
                return byteArrayInputStream;
            } catch (Exception e) {
                e.printStackTrace();
                return new ByteArrayInputStream(postContent.getBytes());
            }
        } else {
            return null;
        }
    }

    /**
     * 将Bundle对象构建成post内容流
     * 
     * @param content
     * @return
     */
    public static InputStream buildUrlEncodedInputStream(Bundle postParams) {
        if (null != postParams) {
            StringBuilder sb = new StringBuilder();
            CharSequence result = "";
            Set<String> keys = postParams.keySet();
            try {
                for (String key : keys) {
                    Object value = postParams.get(key);
                    String valueStr = value.toString();
                    String encodeKey = URLEncoder.encode(key, "UTF-8");
                    String encodeValue = URLEncoder.encode(valueStr, "UTF-8");
                    sb.append(encodeKey)//
                    .append("=")//
                    .append(encodeValue)
                    .append("&");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                sb = new StringBuilder();
                for (String key : keys) {
                    Object value = postParams.get(key);
                    String valueStr = value.toString();
                    String encodeKey = URLEncoder.encode(key);
                    String encodeValue = URLEncoder.encode(valueStr);
                    sb.append(encodeKey)//
                    .append("=")//
                    .append(encodeValue)
                    .append("&");
                }
            }
            if (sb.length() > 1) {
                result = sb.subSequence(0, sb.length() - 1);
            }
            String finalResult = result.toString();
            return buildStringInputStream(finalResult);
        } else {
            return null;
        }
    }

    /**
     * 将字节数组构建成post内容流
     * 
     * @param byteArray
     * @return
     */
    public static InputStream buildByteArrayInputStream(byte[] byteArray) {
        return new ByteArrayInputStream(byteArray);
    }
}
