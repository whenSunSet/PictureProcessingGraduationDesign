package com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;

import java.util.List;

/**
 * Created by whensunset on 2018/3/23.
 */

public abstract class ParentBaseVM extends ChildBaseVM {
    public static final String TAG = "何时夕:ParentBaseVM";
    protected ChildBaseVM mNowChildBaseVM = null;

    public ParentBaseVM(List<ObservableField<? super Object>> eventListenerList) {
        super(eventListenerList);
    }

    public ParentBaseVM(int listenerSize) {
        super(listenerSize);
    }

    public ParentBaseVM() {
    }

    protected void changeNowChildVM(ChildBaseVM nowChildBaseVM) {
        MyLog.d(TAG, "changeNowChildVM", "状态:", "resume " + nowChildBaseVM.getRealClassName());
        nowChildBaseVM.resume();
        if (mNowChildBaseVM != null) {
            mNowChildBaseVM.stop();
            MyLog.d(TAG, "changeNowChildVM", "状态:", "stop " + mNowChildBaseVM.getRealClassName());
        }
        mNowChildBaseVM = nowChildBaseVM;
    }

    public ChildBaseVM getNowChildBaseVM() {
        return mNowChildBaseVM;
    }
}
