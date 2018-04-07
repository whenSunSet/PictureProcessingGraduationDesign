package com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.BaseVM;

/**
 * Created by whensunset on 2018/3/22.
 */

public class TextChangedUIAction extends BaseUIAction {
    public static final String TAG = "何时夕:TextChangedUIAction";
    private UIActionListener<TextChangedUIAction> mTextChangedListener= null;
    private CharSequence mNowText;

    @Override
    public void onTriggerListener(int eventListenerPosition , BaseVM baseVM , Object... params) {
        CharSequence changedText = (CharSequence) params[0];
        mLastEventListenerPosition = eventListenerPosition;
        mNowText = changedText;
        if (mTextChangedListener != null) {
            mTextChangedListener.onUIActionChanged(this);
        }
        MyLog.d(TAG, "onTriggerListener", "状态:eventListenerPosition:changedText:", "触发了输入框文字变化事件", eventListenerPosition , changedText);
    }

    @Override
    public void setListener(UIActionListener<? extends UIAction> listener) {
        mTextChangedListener = (UIActionListener<TextChangedUIAction>) listener;
    }

    public CharSequence getNowText() {
        if (mNowText == null) {
            throw new RuntimeException("还没有触发过该事件");
        }
        return mNowText;
    }

    @Override
    public boolean checkParams(Object[] params) {
        return (params != null && params.length >= 1 && params[0] != null);
    }

    @Override
    public String toString() {
        return "ClickUIAction{" +
                "mLastEventListenerPosition=" + mLastEventListenerPosition +
                '}';
    }
}
