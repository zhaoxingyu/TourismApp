package com.buu.tourism.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PackageUtil {
    public static List<Class<?>> getClassesByPackageName(String packageName) throws IOException, ClassNotFoundException {
//        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//        String path = packageName.replace('.', '/');
//        Enumeration<URL> resources = classLoader.getResources(path);
//        List<File> dirs = new ArrayList<File>();
//        while (resources.hasMoreElements()) {
//            URL resource = resources.nextElement();
//            dirs.add(new File(resource.getFile()));
//        }
//        ArrayList<Class> classes = new ArrayList<Class>();
//        for (File directory : dirs) {
//            classes.addAll(findClasses(directory, packageName));
//        }
//        return classes;
        try {
            return scan(packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                // 递归查找文件夹【即对应的包】下面的所有文件
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + '.' + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    public static void main(String[] args) {
        try {
            List<Class<?>> list = getClassesByPackageName("com.buu.tourism");
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i).getName());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    
    
    /**
     * 遍历包 并遍历子包
     */
    public static List<Class<?>> scan(String packageName) throws Exception {
        List<Class<?>> list = new ArrayList<Class<?>>();
        String path = getSrcPath() + packageToDir(packageName);
        new File("src\\com\\buu\\tourism\\fragment\\").list();
        File dir = new File(path);
        File[] files = dir.listFiles();
        Class<?> cla = null;
        for (File f : files) {
            if (!f.isDirectory()) {
                cla = Class.forName(packageName + "." + f.getName().split("\\.")[0]);
                list.add(cla);
            }
        }
        return list;
    }

    /**
     * 获取当前路径
     */
    public static String getSrcPath() throws IOException {
        File file = new File("");
        String path = file.getCanonicalPath() + File.separator + "src";
        return path;
    }

    /**
     * package转换为路径格式
     */
    public static String packageToDir(String packageName) {
        String[] array = packageName.split("\\.");
        StringBuffer sb = new StringBuffer();
        for (String str : array) {
            sb.append(File.separator).append(str);
        }
        return sb.toString();
    }
}