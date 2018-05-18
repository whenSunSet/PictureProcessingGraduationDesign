package com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction;

import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.BaseVM;

/**
 * Created by whensunset on 2018/3/22.
 */

public interface UIAction {
    String TAG = "何时夕:UIAction";

    boolean checkParams(Object[] params);


    interface UIActionListener<T>{
        void onUIActionChanged(T uiAction);
    }

    void setListener(UIActionListener<? extends UIAction> listener);

    void onTriggerListener(int eventListenerPosition , BaseVM baseVM , UIActionManager.CallAllPreEventAction callAllPreEventAction, UIActionManager.CallAllAfterEventAction callAllAfterEventAction, Object... params);

    UIActionManager.CallAllAfterEventAction getCallAllAfterEventAction();
    UIActionManager.CallAllPreEventAction getCallAllPreEventAction();

}
