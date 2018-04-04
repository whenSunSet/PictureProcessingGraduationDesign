package com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction;

import android.annotation.SuppressLint;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.BaseVM;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

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

    private UIAction initUIAction(int type) {
        switch (type) {
            case CLICK_ACTION:
                return new ClickUIAction();
            case ITEM_SELECTED_ACTION:
                return new ItemSelectedUIAction();
            case PROGRESS_CHANGED_ACTION:
                return new ProgressChangedUIAction();
            case TEXT_CHANGED_ACTION:
                return new TextChangedUIAction();
        }
        return null;
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

    private void doUIAction(int eventListenerPosition , int uiActionFlag , Object... params) {
        UIAction uiAction = getUIAction(uiActionFlag);
        mBaseVM.checkEventListenerList(eventListenerPosition);
        if (!uiAction.checkParams(params)) {
            MyLog.d(TAG, "doUIAction", "状态:eventListenerPosition:uiActionFlag:params:",
                    "参数校验失败，不可继续执行" ,eventListenerPosition , uiActionFlag , params);
            throw new RuntimeException("参数校验失败，不可继续执行");
        }
        if (uiAction instanceof ClickUIAction) {
            ((ClickUIAction) uiAction).onClick(eventListenerPosition);
        } else if (uiAction instanceof ItemSelectedUIAction) {
            ((ItemSelectedUIAction) uiAction).onItemSelected(eventListenerPosition , (Integer) params[0] , mBaseVM);
        } else if (uiAction instanceof ProgressChangedUIAction) {
            ((ProgressChangedUIAction) uiAction).onProgressChanged(eventListenerPosition , (Integer) params[0]);
        } else if (uiAction instanceof TextChangedUIAction) {
            ((TextChangedUIAction) uiAction).onTextChanged(eventListenerPosition , (CharSequence) params[0]);
        }
    }

    public <T extends UIAction> Flowable<T> getDefaultThrottleFlowable(int uiActionFlag) {
        return getDefaultThrottleFlowable(DEFAULT_THROTTLE_MILLISECONDS , uiActionFlag);
    }

    public <T extends UIAction> Flowable<T> getDefaultThrottleFlowable(int throttleMilliseconds , int uiActionFlag) {
        return Flowable.create(new UIAction.ViewModelThrottleOnSubscribe<>(getUIAction(uiActionFlag)) , BackpressureStrategy.BUFFER)
                .throttleFirst(throttleMilliseconds , TimeUnit.MILLISECONDS)
                .filter(uiAction -> {
                    MyLog.d(TAG, "getDefaultThrottleFlowable", "状态:uiAction", "" , uiAction);
                    return uiAction != null;
                }).map(uiAction -> (T) uiAction);
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

    public Map<Integer , UIAction> getUIActionMap() {
        return mUIActionMap;
    }
}
