package com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel;

import android.annotation.SuppressLint;
import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.UIActionManager;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by whensunset on 2018/3/23.
 */

public abstract class ParentBaseVM extends ChildBaseVM {
    public static final String TAG = "何时夕:ParentBaseVM";
    @SuppressLint("UseSparseArrays")
    protected Map<Integer , ChildBaseVM> mChildBaseVMMap = new HashMap<>();
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

    protected void initChildBaseVM(Class<? extends ChildBaseVM> clazz , int childBaseVMPosition) {
        try {
            ChildBaseVM childBaseVM = clazz.newInstance();
            mChildBaseVMMap.put(childBaseVMPosition , childBaseVM);
        } catch (Exception e) {
            throw new RuntimeException("该ChildBaseVM需要参数才能进行初始化，没有无参构造函数");
        }
    }

    protected void initChildBaseVM(ChildBaseVM childBaseVM , int childBaseVMPosition) {
        mChildBaseVMMap.put(childBaseVMPosition , childBaseVM);
    }

    public  <T extends ChildBaseVM> T getChildBaseVM(int childBaseVMPosition) {
        ChildBaseVM childBaseVM = mChildBaseVMMap.get(childBaseVMPosition);
        if (childBaseVM == null) {
            MyLog.d(TAG, "getChildBaseVM", "状态:", getRealClassName() + "没有这种:" + childBaseVMPosition);
            throw new RuntimeException("没有这种ChildBaseVM");
        }
        return (T) childBaseVM;
    }

    public void joinPreActionToMyAllVM(UIActionManager.PreEventAction preEventAction) {
        joinPreActionToAllChildVM(preEventAction);
        beJoinedPreActionToEvent(preEventAction);
    }

    public void joinAfterActionToMyAllVM(UIActionManager.AfterEventAction afterEventAction) {
        joinAfterActionToAllChildVM(afterEventAction);
        beJoinedAfterActionToEvent(afterEventAction);
    }

    public void joinPreActionToAllChildVM(UIActionManager.PreEventAction preEventAction) {
        for (Integer integer:mChildBaseVMMap.keySet()) {
            ChildBaseVM childBaseVM = mChildBaseVMMap.get(integer);

            if (childBaseVM instanceof ParentBaseVM) {
                ((ParentBaseVM) childBaseVM).joinPreActionToMyAllVM(preEventAction);
            }

            if (childBaseVM instanceof ItemManagerBaseVM) {
                ((ItemManagerBaseVM) childBaseVM).joinPreActionToMyAllVM(preEventAction);
            }
        }
    }

    public void joinAfterActionToAllChildVM(UIActionManager.AfterEventAction afterEventAction) {
        for (Integer integer:mChildBaseVMMap.keySet()) {
            ChildBaseVM childBaseVM = mChildBaseVMMap.get(integer);
            childBaseVM.beJoinedAfterActionToEvent(afterEventAction);

            if (childBaseVM instanceof ParentBaseVM) {
                ((ParentBaseVM) childBaseVM).joinAfterActionToMyAllVM(afterEventAction);
            }

            if (childBaseVM instanceof ItemManagerBaseVM) {
                ((ItemManagerBaseVM) childBaseVM).joinAfterActionToMyAllVM(afterEventAction);
            }
        }
    }

    @Override
    public void isEventEnable(boolean isEnable) {
        super.isEventEnable(isEnable);
        controlChildVMEventIsEnable(isEnable);
    }

    public void controlChildVMEventIsEnable(boolean isEnable) {
        for (Integer integer:mChildBaseVMMap.keySet()) {
            ChildBaseVM childBaseVM = mChildBaseVMMap.get(integer);

            if (childBaseVM instanceof ParentBaseVM) {
                ((ParentBaseVM) childBaseVM).isEventEnable(isEnable);
            }

            if (childBaseVM instanceof ItemManagerBaseVM) {
                ((ItemManagerBaseVM) childBaseVM).isEventEnable(isEnable);
            }
        }
    }

    @Override
    public void onCleared() {
        super.onCleared();
        for (Integer integer:mChildBaseVMMap.keySet()) {
            ChildBaseVM childBaseVM = mChildBaseVMMap.get(integer);
            if (childBaseVM.isNeedDestroy()) {
                childBaseVM.onCleared();
                MyLog.d(TAG, "onCleared", "状态:", childBaseVM.getRealClassName() + "被销毁了");
            }
        }
    }

}
