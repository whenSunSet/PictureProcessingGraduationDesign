package com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction;

import android.annotation.SuppressLint;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.BaseVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by whensunset on 2018/3/24.
 */

public class UIActionManager {
    public static final String TAG = "何时夕:UIActionManager";
    public static final int DEFAULT_THROTTLE_MILLISECONDS = 400;
    public static final int CLICK_ACTION = 0;
    public static final int ITEM_SELECTED_ACTION = 1;
    public static final int PROGRESS_CHANGED_ACTION = 2;
    public static final int TEXT_CHANGED_ACTION = 3;

    private boolean isEnable = true;

    private List<PreEventAction> mPreEventActionList = new ArrayList<>();
    private List<AfterEventAction> mAfterEventActionList = new ArrayList<>();

    @SuppressLint("UseSparseArrays")
    private static final Map<Integer , Class<? extends UIAction>> UIActionClassMap = new HashMap<>();

    static {
        // 添加新的UIAction
        UIActionClassMap.put(CLICK_ACTION , ClickUIAction.class);
        UIActionClassMap.put(ITEM_SELECTED_ACTION , ItemSelectedUIAction.class);
        UIActionClassMap.put(PROGRESS_CHANGED_ACTION , ProgressChangedUIAction.class);
        UIActionClassMap.put(TEXT_CHANGED_ACTION , TextChangedUIAction.class);
    }

    private BaseVM mBaseVM;
    @SuppressLint("UseSparseArrays")
    private final Map<Integer , UIAction> mUIActionMap = new HashMap<>();

    public UIActionManager(BaseVM baseVM) {
        mBaseVM = baseVM;
    }

    public UIActionManager(BaseVM baseVM , Integer... uiActionTypes) {
        mBaseVM = baseVM;
        for (Integer type : uiActionTypes) {
            UIAction uiAction = initUIAction(type);
            if (uiAction == null) {
                throw new RuntimeException("暂时不支持这种uiAction");
            }
            mUIActionMap.put(type , uiAction);
            MyLog.d(TAG, "UIActionManager", "状态:uiAction:type", "" , uiAction , type);
        }
    }


    public void doClick(int eventListenerPosition , Object... params) {
        doUIAction(eventListenerPosition , CLICK_ACTION , params);
    }

    public void doItemSelected(int eventListenerPosition , Object... params) {
        doUIAction(eventListenerPosition , ITEM_SELECTED_ACTION , params);
    }

    public void doProgressChanged(int eventListenerPosition , Object... params) {
        doUIAction(eventListenerPosition , PROGRESS_CHANGED_ACTION , params);
    }

    public void doTextChanged(int eventListenerPosition , Object... params) {
        doUIAction(eventListenerPosition , TEXT_CHANGED_ACTION , params);
    }

    public void doUIAction(int eventListenerPosition , int uiActionFlag , Object... params) {
        if (!isEnable) {
            MyLog.d(TAG, "doUIAction", "状态:ViewModelName:", "本ViewModel的事件不允许被触发", mBaseVM.getRealClassName());
            return;
        }

        UIAction uiAction = getUIAction(uiActionFlag);
        mBaseVM.checkEventListenerList(eventListenerPosition);
        if (!uiAction.checkParams(params)) {
            MyLog.d(TAG, "doUIAction", "状态:eventListenerPosition:uiActionFlag:params:",
                    "参数校验失败，不可继续执行" ,eventListenerPosition , uiActionFlag , params);
            throw new RuntimeException("参数校验失败，不可继续执行");
        }

        uiAction.onTriggerListener(
                eventListenerPosition,
                mBaseVM,
                () -> callPreEventAction(eventListenerPosition, uiActionFlag, mBaseVM, params),
                () -> callAfterEventAction(eventListenerPosition, uiActionFlag, mBaseVM, params),
                params);
    }

    private void callPreEventAction(int eventListenerPosition, int uiActionFlag, BaseVM baseVM, Object... params) {
        for (int i = 0; i < mPreEventActionList.size(); i++) {
            mPreEventActionList.get(i).doPreAction(eventListenerPosition, uiActionFlag, baseVM, params);
        }
    }

    private void callAfterEventAction(int eventListenerPosition, int uiActionFlag, BaseVM baseVM, Object... params) {
        for (int i = 0; i < mAfterEventActionList.size(); i++) {
            mAfterEventActionList.get(i).doAfterAction(eventListenerPosition, uiActionFlag, baseVM, params);
        }
    }

    private UIAction initUIAction(int type) {
       Class<? extends UIAction> uiActionClass = UIActionClassMap.get(type);
       if (uiActionClass == null) {
           return null;
       }
        try {
            return uiActionClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("使用class 构建UIAction对象失败");
        }
    }

    public <T extends UIAction> Flowable<T> getDefaultThrottleFlowable(int uiActionFlag) {
        return getDefaultThrottleFlowable(DEFAULT_THROTTLE_MILLISECONDS , uiActionFlag);
    }

    public <T extends UIAction> Flowable<T> getDefaultThrottleFlowable(int throttleMilliseconds , int uiActionFlag) {
        return Flowable.create(new ViewModelThrottleOnSubscribe<>(getUIAction(uiActionFlag)) , BackpressureStrategy.BUFFER)
                .throttleFirst(throttleMilliseconds , TimeUnit.MILLISECONDS)
                .filter(uiAction -> {
                    MyLog.d(TAG, "getDefaultThrottleFlowable", "状态:uiAction", "" , uiAction);
                    return uiAction != null;
                }).map(uiAction -> {
                    uiAction.getCallAllPreEventAction().callAllPreEventAction();
                    return (T) uiAction;
                });
    }

    class ViewModelThrottleOnSubscribe<T extends UIAction> implements FlowableOnSubscribe<T> {
        final T mUIAction;

        public ViewModelThrottleOnSubscribe(T uiAction) {
            this.mUIAction = uiAction;
        }

        @Override
        public void subscribe(FlowableEmitter<T> emitter) throws Exception {
            UIAction.UIActionListener<T> listener = uiAction -> {
                if (!emitter.isCancelled()) {
                    emitter.onNext(uiAction);
                }
            };
            mUIAction.setListener(listener);
        }
    }

    public void addUIAction(Integer uiActionFlag , UIAction uiAction) {
        mUIActionMap.put(uiActionFlag , uiAction);
    }

    public UIAction getUIAction(Integer uiActionFlag) {
        if (uiActionFlag == null) {
            throw new RuntimeException("uiActionFlag不可为null");
        }
        if (mUIActionMap.size() == 0) {
            throw new RuntimeException("该VM没有添加UIAtion");
        }

        UIAction uiAction = mUIActionMap.get(uiActionFlag);
        if (uiAction == null) {
            throw new RuntimeException("没有这种UIAction");
        }

        return uiAction;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public interface PreEventAction{
        void doPreAction(int eventListenerPosition, int uiActionFlag, BaseVM baseVM, Object... params);
    }
    public interface AfterEventAction{
        void doAfterAction(int eventListenerPosition, int uiActionFlag, BaseVM baseVM, Object... params);
    }

    public interface CallAllAfterEventAction{
        void callAllAfterEventAction();
    }

    public interface CallAllPreEventAction{
        void callAllPreEventAction();
    }

    public void setPreEventAction(PreEventAction preEventAction) {
        mPreEventActionList.add(preEventAction);
    }

    public void setAfterEventAction(AfterEventAction afterEventAction) {
        mAfterEventActionList.add(afterEventAction);
    }
}
