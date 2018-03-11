package com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil;

import android.databinding.Observable;

/**
 * Created by whensunset on 2018/3/10.
 */

public class MyExceptionOnPropertyChangedCallback extends Observable.OnPropertyChangedCallback {
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
            mFailedAction.onFailed(e);
        }
    }
}
