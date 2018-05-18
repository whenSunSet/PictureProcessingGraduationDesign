package com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.BaseVM;

/**
 * Created by whensunset on 2018/3/22.
 */

public class ClickUIAction extends BaseUIAction {
    public static final String TAG = "何时夕:ClickUIAction";
    private UIActionListener<ClickUIAction> mOnClickListener = null;

    @Override
    public void onTriggerListener(int eventListenerPosition , BaseVM baseVM , UIActionManager.CallAllPreEventAction callAllPreEventAction, UIActionManager.CallAllAfterEventAction callAllAfterEventAction, Object... params) {
        super.onTriggerListener(eventListenerPosition , baseVM , callAllPreEventAction, callAllAfterEventAction, params);

        mLastEventListenerPosition = eventListenerPosition;
        mCallAllAfterEventAction = callAllAfterEventAction;

        if (mOnClickListener != null) {
            mOnClickListener.onUIActionChanged(this);
        }
        MyLog.d(TAG, "onTriggerListener", "状态:eventListenerPosition:", "触发了点击事件监听器", eventListenerPosition);
    }

    @Override
    public void setListener(UIActionListener<? extends UIAction> listener) {
        mOnClickListener = (UIActionListener<ClickUIAction>) listener;
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
