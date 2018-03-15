package com.example.whensunset.pictureprocessinggraduationdesign.base;

import android.databinding.ObservableField;

import java.util.ArrayList;

/**
 * Created by whensunset on 2018/3/2.
 */

public abstract class BaseVM {
    public static final String TAG = "何时夕:BaseVM";
    public static final int RESUME = 1;
    public static final int STOP = 2;

    public final ObservableField<? super Object> mShowToast = new ObservableField<>();

    private final ArrayList<ObservableField<? super Object>> mClickListenerList = new ArrayList<>();
    private int mListenerSize = 0;
    private int mState = STOP;

    public BaseVM() {
    }

    public BaseVM(int listenerSize) {
        mListenerSize = listenerSize;

        for (int i = 0; i < mListenerSize; i++) {
            mClickListenerList.add(new ObservableField<>());
        }
    }

    public void onClick(int position) {
        if (position >= mListenerSize || position < 0) {
            throw new RuntimeException("数组越界，没有那么多listener");
        }

        MyLog.d(TAG, "onClick", "状态:class:mListenerSize:position:", "点击了按钮" , this.getClass().getName()  , mListenerSize , position);
    }

   public ObservableField<? super Object> getListener(int position) {
        if (position >= mListenerSize || position < 0) {
            throw new RuntimeException("数组越界，没有那么多listener");
        }
        return mClickListenerList.get(position);
    }

    public void onResume() {
        mState = RESUME;
    }

    public void onStop() {
        mState = STOP;
    }

    public int getState() {
        return mState;
    }

    public int getListenerSize() {
        return mListenerSize;
    }

    protected void showToast(String message) {
        mShowToast.set(ObserverParamMap.setToastMessage(message));
    }
}
