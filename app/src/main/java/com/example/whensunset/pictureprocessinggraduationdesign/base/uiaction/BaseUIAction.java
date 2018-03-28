package com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction;

/**
 * Created by whensunset on 2018/3/24.
 */

public abstract class BaseUIAction implements UIAction {
    protected int mLastEventListenerPosition = -1;

    public int getLastEventListenerPosition() {
        if (mLastEventListenerPosition == -1) {
            throw new RuntimeException("还没有触发过该事件");
        }
        return mLastEventListenerPosition;
    }

}
