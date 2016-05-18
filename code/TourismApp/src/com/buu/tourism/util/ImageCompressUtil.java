package com.buu.tourism.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.util.ByteArrayBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class ImageCompressUtil {

    public static void compressFile(byte[] data, File dst) throws Exception {
        compressFile(data, dst, 720);
    }

    public static void compressFile(InputStream is, File dst) throws Exception {

        int len = -1;
        byte[] buffer = new byte[2048];
        ByteArrayBuffer arrayBuffer = new ByteArrayBuffer(buffer.length * 10);
        while ((len = is.read(buffer, 0, buffer.length)) != -1) {
            arrayBuffer.append(buffer, 0, len);
        }
        is.close();
        compressFile(arrayBuffer.buffer(), dst);
        arrayBuffer.clear();
    }

    public static void compressFile(InputStream is, File dst, int minWidth) throws Exception {

        int len = -1;
        byte[] buffer = new byte[2048];
        ByteArrayBuffer arrayBuffer = new ByteArrayBuffer(buffer.length * 10);
        while ((len = is.read(buffer, 0, buffer.length)) != -1) {
            arrayBuffer.append(buffer, 0, len);
        }
        is.close();
        compressFile(arrayBuffer.buffer(), dst, minWidth);
        arrayBuffer.clear();
    }

    public static void compressFile(byte[] data, File dst, int minWith) throws Exception {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        int realWidth = options.outWidth;
        int realHeight = options.outHeight;

        float reqWidth = realWidth > minWith ? minWith : realHeight;
        float rate = (float) (reqWidth / realWidth);
        float reqHeight = (int) (realHeight * rate);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, (int) reqWidth, (int) reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap dstBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        dstBitmap = Bitmap.createScaledBitmap(dstBitmap, (int) reqWidth, (int) reqHeight, true);
        OutputStream outputStream = new FileOutputStream(dst);
        dstBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        dstBitmap.recycle();
        outputStream.close();
    }

    public static void compressFile(File src, File dst) throws Exception {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(src.toString(), options);
        int realWidth = options.outWidth;
        int realHeight = options.outHeight;

        float reqWidth = realWidth > 720 ? 720 : realHeight;
        float rate = (float) (reqWidth / realWidth);
        float reqHeight = (int) (realHeight * rate);

        Bitmap dstBitmap = decodeSampledBitmapFromFile(src.toString(), (int) reqWidth, (int) reqHeight);

        // �ȱ�ѹ�� �Կ��720px Ϊ��׼
        dstBitmap = Bitmap.createScaledBitmap(dstBitmap, (int) reqWidth, (int) reqHeight, true);
        OutputStream outputStream = new FileOutputStream(dst);
        dstBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        dstBitmap.recycle();
        outputStream.close();
    }

    public static synchronized Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filename, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further.
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    public static Bitmap compressBitmap(Bitmap bm, int minWidth) {
        int realWidth = bm.getWidth();
        int realHeight = bm.getHeight();
        int realSize = realHeight > realWidth ? realWidth : realHeight;
        float reqWidth = realSize > minWidth ? minWidth : realSize;
        float rate = (float) (reqWidth / realWidth);
        Matrix matrix = new Matrix();  
        matrix.postScale(rate, rate);
        Bitmap dstBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);  
        return dstBitmap;
    }

    public static int calculateInSampleSize(int realWidth, int realHeight, int reqWidth, int reqHeight) {
        // Raw height and width of image
        int width = realWidth;
        int height = realHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further.
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        Bitmap bitmap = Bitmap.createBitmap(image);
        image.recycle();
        int options = 80;
        while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
            bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        }
        return bitmap;
    }
}
