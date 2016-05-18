package com.buu.tourism.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;

public class AssetsUtil {

    public static String getAssetsFileContent(Context context, String fileName) {
        String result = "";
        try {
            InputStream is = context.getResources().getAssets().open(fileName);
            BufferedInputStream inputStream = new BufferedInputStream(is);
            ByteArrayBuffer byteBuffer = new ByteArrayBuffer(1024);
            int len = -1;
            byte[] buffer = new byte[2048];
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.append(buffer, 0, len);
            }
            inputStream.close();
            byte[] byteArray = byteBuffer.toByteArray();
            result = new String(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
