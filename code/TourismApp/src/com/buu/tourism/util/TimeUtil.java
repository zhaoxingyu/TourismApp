package com.buu.tourism.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.format.Time;

public class TimeUtil {
    public static String getTimeString(long t) {
        Time time = new Time();
        time.set(t);
        return time.format("%T");
    }

    public static String getDayAfterYMD(String specifiedDay,int nAfter) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + nAfter);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }
    
    public static String getDayAfterMD(String specifiedDay,int nAfter) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + nAfter);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        int size =dayAfter.length();
        return dayAfter.substring(size-5,size);
    }
    
    public static String getYMDString(long t){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(t));
    }
    public static String getHMString(long t){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sTime = sdf.format(new Date(t));
        int size =sTime.length();
        return sTime.substring(size-5,size);
    }
    /**
     * 根据年月日格式获取星期几
     * @param sMyd
     * @return
     */
    public static String getWeekofDate(String sMyd){
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(sMyd);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getWeekOfDate(date);
    }
    
    public static String getWeekOfDate(Date date) { 
        if( date==null )
            return null;
        String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" }; 
       // String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" }; 
        Calendar calendar = Calendar.getInstance(); 
        calendar.setTime(date); 
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; 
        return weekDaysName[intWeek]; 
      }
    
    public static long string2Time(String sTime){
        SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Date date = null;
        long time = 0;
        try {
            date = format.parse(sTime);
            time=date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;
    }
    public static long string2Date(String sTime){
        SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd" );
        Date date = null;
        long time = 0;
        try {
            date = format.parse(sTime);
            time=date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;
    }
    
}