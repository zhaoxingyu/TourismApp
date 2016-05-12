package com.buu.tourism.util;

import java.io.UnsupportedEncodingException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.text.TextUtils;
import android.util.Base64;

/**
 * 3DES加密工具类
 * 
 * @author liufeng
 * @date 2012-10-11
 */
public class DES3 {
    // 密钥
    private final static byte[] secretKeyBytes = { 98, 101, 97, 99, 111, 110, 95, 115, 100, 107, 95, 107, 101, 121, 64,
            98, 97, 105, 100, 117, 46, 99, 111, 109 };

    // 向量
    private final static byte[] ivBytes = { 50, 48, 49, 52, 48, 56, 49, 52 };

    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";

    /**
     * 3DES加密
     * 
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKeyBytes);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return Base64.encodeToString(encryptData, Base64.DEFAULT);
    }

    public static String encode(byte[] plainByte) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKeyBytes);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainByte);
        return Base64.encodeToString(encryptData, Base64.DEFAULT);
    }

    /**
     * 3DES解密
     * 
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText) throws Exception {
        if ((encryptText == null) || TextUtils.isEmpty(encryptText)) {
            return "";
        }
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKeyBytes);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText, Base64.DEFAULT));
        return new String(decryptData);
    }

    public static byte[] decodeToByte(String encryptText) throws Exception {
        if ((encryptText == null) || TextUtils.isEmpty(encryptText)) {
            return null;
        }
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKeyBytes);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        return cipher.doFinal(Base64.decode(encryptText, Base64.DEFAULT));
    }

    public byte[] String(String password) {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(password);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }

}
