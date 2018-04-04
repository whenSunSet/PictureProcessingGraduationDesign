package com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;

/**
 * Created by whensunset on 2018/3/22.
 */

public class ProgressChangedUIAction extends BaseUIAction{
    public static final String TAG = "何时夕:ProgressChangedUIAction";
    private UIActionListener<ProgressChangedUIAction> mOnProgressChangedListener = null;
    private int mProgress = Integer.MIN_VALUE;

    public void onProgressChanged(int eventListenerPosition, int progress) {
        mProgress = progress;
        mLastEventListenerPosition = eventListenerPosition;

        if (mOnProgressChangedListener != null) {
            mOnProgressChangedListener.onUIActionChanged(this);
        }
        MyLog.d(TAG, "onColorChanged", "状态:eventListenerPosition:progress:", "触发了点击事件监听器", eventListenerPosition , progress);
    }

    public void setOnProgressChangedListener(UIActionListener<ProgressChangedUIAction> uiActionListener) {
        mOnProgressChangedListener = uiActionListener;
    }

    public int getProgress() {
        if (mProgress == Integer.MIN_VALUE) {
            throw new RuntimeException("还没有触发过该事件");
        }
        return mProgress;
    }

    @Override
    public boolean checkParams(Object[] params) {
        return (params != null && params.length >= 1 && params[0] != null);
    }

    @Override
    public String toString() {
        return "ProgressChangedUIAction{" +
                "mLastEventListenerPosition=" + mLastEventListenerPosition +
                ", mProgress=" + mProgress +
                '}';
    }
}
