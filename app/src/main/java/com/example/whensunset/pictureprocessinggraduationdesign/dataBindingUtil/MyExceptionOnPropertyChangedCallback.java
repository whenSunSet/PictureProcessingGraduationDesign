package com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil;

import android.databinding.Observable;

import com.example.whensunset.pictureprocessinggraduationdesign.base.MyLog;

/**
 * Created by whensunset on 2018/3/10.
 */

public class MyExceptionOnPropertyChangedCallback extends Observable.OnPropertyChangedCallback {
    public static String TAG = "何时夕:MyExceptionOnPropertyChangedCallback";

    private Observable.OnPropertyChangedCallback mOnPropertyChangedCallback;
    private FailedAction mFailedAction;

    public MyExceptionOnPropertyChangedCallback(Observable.OnPropertyChangedCallback onPropertyChangedCallback , FailedAction failedAction) {
        mOnPropertyChangedCallback = onPropertyChangedCallback;
        mFailedAction = failedAction;
    }

    @Override
    public void onPropertyChanged(Observable observable , int i) {
        try {
            mOnPropertyChangedCallback.onPropertyChanged(observable , i);
        } catch (RuntimeException e) {
            MyLog.d(TAG, "onPropertyChanged", "状态:e", "" , e);
            mFailedAction.onFailed(e);
        }
    }
}
