package com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;

/**
 * Created by whensunset on 2018/3/22.
 */

public class ClickUIAction extends BaseUIAction {
    public static final String TAG = "何时夕:ClickUIAction";
    private UIActionListener<ClickUIAction> mOnClickListener = null;

    public void onClick(int eventListenerPosition) {
        mLastEventListenerPosition = eventListenerPosition;

        if (mOnClickListener != null) {
            mOnClickListener.onUIActionChanged(this);
        }
        MyLog.d(TAG, "onTextChanged", "状态:eventListenerPosition:", "触发了点击事件监听器", eventListenerPosition);
    }


    public UIActionListener<ClickUIAction> getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(UIActionListener<ClickUIAction> onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public boolean checkParams(Object[] params) {
        return true;
    }

    @Override
    public String toString() {
        return "ClickUIAction{" +
                "mLastEventListenerPosition=" + mLastEventListenerPosition +
                '}';
    }
}
