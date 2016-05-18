package com.buu.tourism.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    public static double getDirSize(File file) {
        // 判断文件是否存在
        if (null != file && file.exists()) {
            // 如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {// 如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024;
                return size;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }

    public static String getFileDesContent(File cacheFile) {
        String result = null;
        StringBuilder sb = new StringBuilder();
        try {
            FileReader f_reader = new FileReader(cacheFile);
            BufferedReader reader = new BufferedReader(f_reader);
            String str = null;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            f_reader.close();
            result = sb.toString();
            try {
                DesUtils des = new DesUtils(KeyUtils.getDesKey());
                result = des.decrypt(result);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean writeFileDesContent(String content, File cacheFile) {
        boolean success = false;
        try {
            DesUtils des = new DesUtils(KeyUtils.getDesKey());
            content = des.encrypt(content);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            FileWriter fileWriter = new FileWriter(cacheFile);
            fileWriter.write(content);
            fileWriter.close();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 删除文件(夹及内容)
     * @param dir
     * @param self
     * @return
     */
    public static boolean deleteDir(File dir ,boolean self) {
        boolean yes = false;;
        try {
            yes = deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e){
            e.printStackTrace();
        }
        if (self) {
            return yes;
        } else{
            boolean succ = dir.mkdirs();
            return succ && yes;
        }
    }

    /**
     * 计算文件夹大小
     * @param dirs
     * @return
     */
    public static double calculateDirSize(File... dirs) {
        double totalSize = 0;
        if (null != dirs) {
            for (File dir : dirs) {
                totalSize +=FileUtil.getDirSize(dir);
            }
        }
        return totalSize;
    }

    private static boolean deleteDir(File dir) {
        if (null == dir) {
            return false;
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
