package com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction;

import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.BaseVM;

/**
 * Created by whensunset on 2018/3/24.
 */

public abstract class BaseUIAction implements UIAction {
    protected int mLastEventListenerPosition = -1;
    protected UIActionManager.CallAllAfterEventAction mCallAllAfterEventAction;
    protected UIActionManager.CallAllPreEventAction mCallAllPreEventAction;

    @Override
    public void onTriggerListener(int eventListenerPosition , BaseVM baseVM , UIActionManager.CallAllPreEventAction callAllPreEventAction, UIActionManager.CallAllAfterEventAction callAllAfterEventAction, Object... params) {
        mCallAllAfterEventAction = callAllAfterEventAction;
        mCallAllPreEventAction = callAllPreEventAction;

    }

    public int getLastEventListenerPosition() {
        if (mLastEventListenerPosition == -1) {
            throw new RuntimeException("还没有触发过该事件");
        }
        return mLastEventListenerPosition;
    }

    @Override
    public UIActionManager.CallAllAfterEventAction getCallAllAfterEventAction() {
        return mCallAllAfterEventAction;
    }

    @Override
    public UIActionManager.CallAllPreEventAction getCallAllPreEventAction() {
        return mCallAllPreEventAction;
    }
}
