package com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by whensunset on 2018/3/22.
 */

public interface UIAction {
    String TAG = "何时夕:UIAction";

    boolean checkParams(Object[] params);

    static void setUIActionListener(UIAction uiAction , UIActionListener listener) {
        if (uiAction instanceof ClickUIAction) {
            ((ClickUIAction) uiAction).setOnClickListener(listener);
        } else if (uiAction instanceof ItemSelectedUIAction) {
            ((ItemSelectedUIAction) uiAction).setOnItemChangedListener(listener);
        } else if (uiAction instanceof  ProgressChangedUIAction) {
            ((ProgressChangedUIAction) uiAction).setOnProgressChangedListener(listener);
        } else if (uiAction instanceof  TextChangedUIAction) {
            ((TextChangedUIAction) uiAction).setTextChangedListener(listener);
        }
    }

    interface UIActionListener<T>{
        void onUIActionChanged(T uiAction);
    }

    class ViewModelThrottleOnSubscribe<T extends UIAction> implements FlowableOnSubscribe<T> {
        final T mUIAction;

        public ViewModelThrottleOnSubscribe(T uiAction) {
            this.mUIAction = uiAction;
        }

        @Override
        public void subscribe(FlowableEmitter<T> emitter) throws Exception {
            UIActionListener<T> listener = uiAction -> {
                if (!emitter.isCancelled()) {
                    emitter.onNext(uiAction);
                }
            };
            UIAction.setUIActionListener(mUIAction , listener);
        }
    }
}
