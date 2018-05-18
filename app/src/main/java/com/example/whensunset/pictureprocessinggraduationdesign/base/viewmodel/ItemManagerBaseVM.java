package com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.UIActionManager;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil.ItemViewArg;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.ItemBaseVM_mPosition;

/**
 * Created by whensunset on 2018/3/23.
 */

public abstract class ItemManagerBaseVM <T extends ItemBaseVM> extends ChildBaseVM {
    public static final String TAG = "何时夕:ItemManagerBaseVM";
    public static final int CLICK_ITEM = 0;

    public final ObservableList<T> mDataItemList = new ObservableArrayList<>();
    public final ObservableField<Integer> mSelectedPosition = new ObservableField<>(-1);
    public final ItemViewArg.ItemViewSelector<T> mViewSelector;

    public ItemManagerBaseVM(int listenerSize, ItemViewArg.ItemViewSelector<T> viewSelector) {
        super(listenerSize);
        mViewSelector = viewSelector;
        initItemChanged();
    }

    public ItemManagerBaseVM(int listenerSize, int viewModelId , int viewItemLayoutId) {
        super(listenerSize);
        mViewSelector = selectItemView(viewModelId , viewItemLayoutId);
        initItemChanged();
    }

    private void initItemChanged() {
        initListener(this, (observable, i) -> {
            Integer selectedItemPosition = ObserverParamMap.staticGetValue(observable , ItemBaseVM_mPosition);
            MyLog.d(TAG, "initItemChanged", "状态:selectedItemPosition:", "更改当前选择的position" , selectedItemPosition);

            if (selectedItemPosition == null) {
                return;
            }
            changeSelectPosition(selectedItemPosition);
        }, CLICK_ITEM);

    }

    protected void changeSelectPosition(int nowSelectPosition) {
        if (nowSelectPosition >= mDataItemList.size()) {
            MyLog.d(TAG, "changeSelectPosition", "状态:nowSelectPosition:size:", nowSelectPosition , mDataItemList.size());
            throw new RuntimeException("当前选择的item 超出了限制");
        }
        if (mSelectedPosition.get() > -1) {
            ItemBaseVM lastItemBaseVM = mDataItemList.get(mSelectedPosition.get());
            lastItemBaseVM.isSelected.set(false);
            lastItemBaseVM.stop();
        }

        MyLog.d(TAG, "changeSelectPosition", "状态:nowSelectPosition:mSelectedPosition:", "" , nowSelectPosition , mSelectedPosition.get());

        mSelectedPosition.set(nowSelectPosition);
        ItemBaseVM nowItemBaseVM = mDataItemList.get(nowSelectPosition);
        nowItemBaseVM.isSelected.set(true);
        nowItemBaseVM.resume();
    }

    protected abstract void initItemVM();

    protected ItemViewArg.ItemViewSelector<T> selectItemView(int viewModelId , int viewItemLayoutId) {
        return selectItemView(1 , viewModelId , viewItemLayoutId);
    }

    private ItemViewArg.ItemViewSelector<T> selectItemView(int viewCount , int viewModelId , int viewItemLayoutId) {
        return new ItemViewArg.ItemViewSelector<T>() {
            public void select(ItemViewArg.ItemView itemView, int position, T item) {
                itemView.set(viewModelId , viewItemLayoutId);
            }

            @Override
            public int viewTypeCount() {
                return viewCount;
            }
        };
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
        for (int i = 0; i < mDataItemList.size(); i++) {
            ItemBaseVM itemBaseVM = mDataItemList.get(i);
            itemBaseVM.beJoinedPreActionToEvent(preEventAction);
        }
    }

    public void joinAfterActionToAllChildVM(UIActionManager.AfterEventAction afterEventAction) {
        for (int i = 0; i < mDataItemList.size(); i++) {
            ItemBaseVM itemBaseVM = mDataItemList.get(i);
            itemBaseVM.beJoinedAfterActionToEvent(afterEventAction);
        }
    }

    @Override
    public void isEventEnable(boolean isEnable) {
        super.isEventEnable(isEnable);
        controlChildVMEventIsEnable(isEnable);
    }

    public void controlChildVMEventIsEnable(boolean isEnable) {
        for (int i = 0; i < mDataItemList.size(); i++) {
            ItemBaseVM itemBaseVM = mDataItemList.get(i);
            itemBaseVM.isEventEnable(isEnable);
        }
    }

    @Override
    public void stop() {
        super.stop();
        initSelectedPosition();
    }

    public void initSelectedPosition() {
        if (mSelectedPosition.get() >= 0) {
            mDataItemList.get(mSelectedPosition.get()).isSelected.set(false);
            mSelectedPosition.set(-1);
        }
    }

    @Override
    public void onCleared() {
        super.onCleared();
        ItemBaseVM firstItemBaseVM = (mDataItemList.size() == 0 ? null : mDataItemList.get(0));
        if (firstItemBaseVM != null && firstItemBaseVM.isNeedDestroy()) {
            for (ItemBaseVM i : mDataItemList) {
                i.onCleared();
            }
        }
    }
}
