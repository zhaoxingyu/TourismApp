
package com.buu.tourism.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

import com.buu.tourism.Constant;
import com.buu.tourism.TourismApplication;
import com.buu.tourism.conf.Setting;

/**
 * Re-package the Log util, now we can control all the logs' switch in this
 * file(a overall switch)
 */
public class LogUtil {

    private static final boolean IS_DEBUG = false;
    
    public static boolean isDebug() {
        boolean isDebug = Setting.isTestLogEnable(TourismApplication.getInstance());
        if (true == isDebug) {
            return true;
        }
        return IS_DEBUG;
    }
    public static final boolean IS_WTF = Constant.isDebug();//是否写入sd卡文件
    
    public static final boolean DEVELOPER_MODE = false;
    
    // set false when send to tester please
    public static final boolean TRACEVIEW_DBG = false;
    
    public static final boolean OUTPUT_MEMORY_INFO = false;
    private static final String TAG = "LogUtil";
    
    /**
     * Send a {@link #VERBOSE} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        if (isDebug()) {
            Log.v(tag, msg);
        }
    }
    
    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void v(String tag, String msg, Throwable tr) {
        if (isDebug()) {
            Log.v(tag, msg, tr);
        }
    }

    /**
     * Send a {@link #IS_DEBUG} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        if (isDebug()) {
//            Log.d(tag, String.valueOf(TimeUtil.getTimeString(System.currentTimeMillis())));
        	Log.d(tag, msg);
        	
        }
    }
    
    /**
     * Send a {@link #IS_DEBUG} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (isDebug()) {
        	Log.d(tag, msg, tr);
        }
    }
    
    public static void debug(String msg, Throwable tr) {
        if (isDebug()) {
            Log.d(TAG, msg, tr);
        }
    }
    
    public static void debug(String msg) {
        if (isDebug()) {
            Log.d(TAG, msg);
        }
    }

    /**
     * Send an {@link #INFO} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
    	if(isDebug()) {
//            Log.i(tag, String.valueOf(TimeUtil.getTimeString(System.currentTimeMillis())));
    		Log.i(tag, msg);
    	}
    }
    
    
    /**
     * Send a {@link #INFO} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void i(String tag, String msg, Throwable tr) {
        if(isDebug()) {
        	Log.i(tag, msg, tr);
        }
    }

    /**
     * Send a {@link #WARN} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
    	if(isDebug()) {
    		Log.w(tag, msg);
    	}
    }
    
    /**
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void w(String tag, String msg, Throwable tr) {
    	if(isDebug()) {
    		Log.w(tag, msg, tr);
    	}
    }
    
    /*
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    public static void w(String tag, Throwable tr) {
    	if(isDebug()) {
    		Log.w(tag, tr);
    	}
    }

    /**
     * Send an {@link #ERROR} log message.
     * 
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
    	if(isDebug()) {
    		Log.e(tag, msg);
    	}
    }
    
    /**
     * Send a {@link #ERROR} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void e(String tag, String msg, Throwable tr) {
        if(isDebug()) {
        	Log.e(tag, msg, tr);
        }
    }

    /**
     * What a Terrible Failure: Report a condition that should never happen.
     * The error will always be logged at level ASSERT with the call stack.
     * Depending on system configuration, a report may be added to the
     * {@link android.os.DropBoxManager} and/or the process may be terminated
     * immediately with an error dialog.
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     */
/*    public static void wtf(String tag, String msg) {
        Log.wtf(tag, msg);
    }
*/
    /**
     * What a Terrible Failure: Report an exception that should never happen.
     * Similar to {@link #wtf(String, String)}, with an exception to log.
     * @param tag Used to identify the source of a log message.
     * @param tr An exception to log.
     */
/*    public static void wtf(String tag, Throwable tr) {
        Log.wtf(tag, tr);
    }*/

    /**
     * What a Terrible Failure: Report an exception that should never happen.
     * Similar to {@link #wtf(String, Throwable)}, with a message as well.
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     * @param tr An exception to log.  May be null.
     */
/*    public static void wtf(String tag, String msg, Throwable tr) {
        Log.wtf(tag, msg, tr);
    }
    */
    /**
     * Low-level logging call.
     * @param priority The priority/type of this log message
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static void println(int priority, String tag, String msg) {
        Log.println(priority, tag, msg);
    }
    
    public static void x(String tag, String msg) {
    	if(isDebug()) {
    		LogUtil.d(tag, msg);
    		//out(tag, msg);
    	}
    }
    public static final String SDCARD_BAILIAN_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "bailian" + File.separator + "log";

    public static void writeToFile(String tag, String msg) {
        FileOutputStream output = null;
        File path = new File(SDCARD_BAILIAN_PATH);
        if (!path.exists() && !path.mkdirs()) {
            return;
        }
        Time time = new Time();
        time.set(System.currentTimeMillis());
        String dateStr = time.format("%y-%m-%d");
        String fileName = "xlog_" + dateStr + ".txt";
        File file = new File(path, fileName);
        try {
            output = new FileOutputStream(file, true);
            String xtime = time.format("%H:%M:%S");
            String msgdata = xtime + "  " + tag + ":    " + msg + "\r\n";
            output.write(msgdata.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }
    }
}
