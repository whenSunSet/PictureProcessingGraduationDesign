package com.example.whensunset.pictureprocessinggraduationdesign.base;

import android.databinding.ObservableField;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by whensunset on 2018/3/2.
 */

public abstract class BaseVM {
    public static final String TAG = "何时夕:BaseVM";
    public static final int DEFAULT_THROTTLE_MILLISECONDS = 400;
    public static final int RESUME = 1;
    public static final int STOP = 2;

    public final ObservableField<? super Object> mShowToast = new ObservableField<>();

    private final List<ObservableField<? super Object>> mClickListenerList = new ArrayList<>();
    private int mState = STOP;

    private OnClickListener mOnClickListener = null;
    private OnProgressChangedListener mOnProgressChangedListener = null;

    private int mClickPosition = 0;
    private int mProgress = 50;

    public BaseVM() {
    }

    public BaseVM(int listenerSize) {
        for (int i = 0; i < listenerSize; i++) {
            mClickListenerList.add(new ObservableField<>());
        }
    }

    public BaseVM(List<ObservableField<? super Object>> clickListenerList) {
        if (clickListenerList == null || clickListenerList.size() == 0) {
            return;
        }
        mClickListenerList.addAll(clickListenerList);
    }

    public void onClick(int position) {
        if (position >= mClickListenerList.size() || position < 0) {
            throw new RuntimeException("数组越界，没有那么多listener");
        }
        mClickPosition = position;
        if (mOnClickListener != null) {
            mOnClickListener.onClick(this);
        }

        MyLog.d(TAG, "onClick", "状态:class:mListenerSize:position:", "点击了按钮" , this.getClass().getName()  , mClickListenerList.size() , position);
    }



    public void onProgressChanged(int progress) {
        mProgress = progress;
        if (mOnProgressChangedListener != null) {
            mOnProgressChangedListener.onProgressChanged(this);
        }

        MyLog.d(TAG, "onProgressChanged", "状态:class:progress:", "滑动了滑块" , this.getClass().getName()  , progress);
    }

    public ObservableField<? super Object> getListener(int position) {
        if (position >= mClickListenerList.size() || position < 0) {
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

    public boolean isResume(){
        return (mState == RESUME);
    }

    public int getState() {
        return mState;
    }

    public int getListenerSize() {
        return mClickListenerList.size();
    }

    public OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        mOnProgressChangedListener = onProgressChangedListener;
    }

    public int getClickPosition() {
        return mClickPosition;
    }

    public int getProgress() {
        return mProgress;
    }

    public List<ObservableField<? super Object>> getClickListenerList() {
        return mClickListenerList;
    }

    protected void showToast(String message) {
        mShowToast.set(ObserverParamMap.setToastMessage(message));
    }

    protected void initClickAction() {}

    protected Flowable<Integer> getDefaultClickFlowable() {
        return getDefaultClickFlowable(DEFAULT_THROTTLE_MILLISECONDS);
    }

    protected Flowable<Integer> getDefaultClickFlowable(int throttleMilliseconds) {
        return Flowable.create(new ViewModelClickOnSubscribe(this) , BackpressureStrategy.BUFFER)
                .throttleFirst(throttleMilliseconds , TimeUnit.MILLISECONDS)
                .filter(baseVM -> {
                    MyLog.d(TAG, "test", "状态:baseVM", "" , baseVM);
                    return baseVM != null;
                }).map(baseVM -> {
                    MyLog.d(TAG, "apply", "状态:position", "" , baseVM.getClickPosition());
                    return baseVM.getClickPosition();
                });
    }

    public interface OnClickListener{
        void onClick(BaseVM baseVM);
    }

    public interface OnProgressChangedListener{
        void onProgressChanged(BaseVM baseVM);
    }

    @Override
    public String toString() {
        return "BaseVM{" +
                "mShowToast=" + mShowToast +
                ", mOnClickListener=" + mOnClickListener +
                ", mClickListenerList=" + mClickListenerList +
                ", mListenerSize=" + mClickListenerList.size() +
                ", mState=" + mState +
                ", mClickPosition=" + mClickPosition +
                '}';
    }
}
