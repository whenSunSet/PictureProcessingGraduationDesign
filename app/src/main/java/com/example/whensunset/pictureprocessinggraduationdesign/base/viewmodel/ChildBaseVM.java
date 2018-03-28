package com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;

import java.util.List;

/**
 * Created by whensunset on 2018/3/23.
 */

public abstract class ChildBaseVM extends BaseVM{
    public static final String TAG = "何时夕:ChildBaseVM";
    public static final int RESUME = 1;
    public static final int STOP = 2;

    protected int mState = STOP;

    public ChildBaseVM(List<ObservableField<? super Object>> eventListenerList) {
        super(eventListenerList);
    }

    public ChildBaseVM(int listenerSize) {
        super(listenerSize);
    }

    public ChildBaseVM() {
    }

    public void resume() {
        mState = RESUME;
        MyLog.d(TAG, "resume", "状态:", "resume " + getRealClassName());
    }

    public void stop() {
        mState = STOP;
        MyLog.d(TAG, "stop", "状态:", "stop " + getRealClassName());
    }


    public int getState() {
        return mState;
    }

    public boolean isResume() {
        return (mState == RESUME);
    }
}
