package com.buu.tourism.util;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

/**
 * Fragment中可以使用该Fragment, 防止内存泄露.
 * 
 * @author zhaocongying zhaocongying@baidu.com
 * 
 */
public class WeakFragmentHandler extends Handler {
    /** message what 是 1,可以公共使用. */
    public static final int MSG_WHAT_1 = 1;

    private WeakReference<Fragment> mWeakHandler;
    // private Map<Integer, WeakReference<Runnable>> mMessageTaskMap;
    /** message 与task绑定的集合 */
    private Map<Integer, Runnable> mMessageTaskMap;
    /** 被忽略的消息what集合 */
    private Map<Integer, Integer> mMessageIgnoredMap;

    public WeakFragmentHandler(Fragment fragment) {
        mWeakHandler = new WeakReference<Fragment>(fragment);
        // mMessageTaskMap = new HashMap<Integer, WeakReference<Runnable>>();
        mMessageTaskMap = new ConcurrentHashMap<Integer, Runnable>();
        mMessageIgnoredMap = new ConcurrentHashMap<Integer, Integer>();
    }

    /**
     * 添加task, 只适用于无参的方法.
     * 
     * @param what Message.what
     * @param task 对应的方法.
     * @author zhaocongying zhaocongying@baidu.com
     * @date 2015年1月6日 下午3:57:53
     */
    public void putTask(int what, Runnable task) {
        // mMessageTaskMap.put(what, new WeakReference<Runnable>(task));
        mMessageTaskMap.put(what, task);
    }

    /**
     * 删除对应的task
     * 
     * @param what
     * @author zhaocongying zhaocongying@baidu.com
     */
    public void removeTask(int what) {
        mMessageTaskMap.remove(what);
    }

    /**
     * 忽略某一种消息;
     * 
     * @param what 当Handler接收到的{@link Message#what}与what相等时,则忽略这条消息,不做处理.
     * @author zhaocongying zhaocongying@baidu.com
     */
    public void ignoreMessage(int what) {
        mMessageIgnoredMap.put(what, what);
    }

    /**
     * true,表示该消息已经被忽略了;false,表示该消息没有被忽略.
     * 
     * @param what
     * @return
     * @author zhaocongying zhaocongying@baidu.com
     */
    public boolean isMessageIgnored(int what) {
        return mMessageIgnoredMap.containsKey(what);
    }

    /**
     * 恢复先前被忽略的消息,使其可以重新处理消息.如果先前没有忽略过该消息,则不做任何处理. {@link #ignoreMessage(int)}
     * 
     * @param what
     * @author zhaocongying zhaocongying@baidu.com
     */
    public void restoreIgnoredMessage(int what) {
        mMessageIgnoredMap.remove(what);
    }

    /**
     * 恢复先前被忽略的消息,使其可以重新处理消息.如果先前没有忽略过任何消息,则不做任何处理.
     * 
     * @author zhaocongying zhaocongying@baidu.com
     */
    public void restoreIgnoredMessage() {
        mMessageIgnoredMap.clear();
    }

    /**
     * 清除全部的Task.
     * 
     * @author zhaocongying zhaocongying@baidu.com
     */
    public void clearTask() {
        mMessageTaskMap.clear();
    }

    /**
     * 判断 {@link #handleMessage(Message)}方法是否可以处理消息.
     * 
     * @return true, 表示 {@link #handleMessage(Message)}方法可以处理消息; false,则不可以处理消息.
     */
    private boolean canHandleMessage() {
        if (mWeakHandler != null) {
            Fragment fragment = mWeakHandler.get();
            // if (fragment != null && !fragment.isRemoving()
            // && !fragment.isDetached()) {
            // return true;
            // }
            if (fragment != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * 子类复写这一个方法用来处理消息.
     * 
     * @param msg
     */
    public void onHandleMessage(Message msg) {

    }

    @Override
    public final void handleMessage(Message msg) {
        if (msg != null && canHandleMessage()) {
            // WeakReference<Runnable> taskRef = mMessageTaskMap.get(msg.what);
            // if (taskRef == null) {
            // onHandleMessage(msg);
            // } else {
            // final Runnable task = taskRef.get();
            // if (task == null) {
            // onHandleMessage(msg);
            // } else {
            // task.run();
            // }
            // }

            final int what = msg.what;
            if (isMessageIgnored(what)) {
                // 不处理被忽略的消息.
                return;
            }

            Runnable task = mMessageTaskMap.get(what);
            if (task == null) {
                onHandleMessage(msg);
            } else {
                task.run();
            }
        }
    }
}
