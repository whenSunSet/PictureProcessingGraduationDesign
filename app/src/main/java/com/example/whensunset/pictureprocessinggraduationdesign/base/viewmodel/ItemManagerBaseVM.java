package com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

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

    @Override
    public void stop() {
        super.stop();
        if (mSelectedPosition.get() >= 0) {
            mDataItemList.get(mSelectedPosition.get()).isSelected.set(false);
            mSelectedPosition.set(-1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ItemBaseVM firstItemBaseVM = (mDataItemList.size() == 0 ? null : mDataItemList.get(0));
        if (firstItemBaseVM != null && firstItemBaseVM.isNeedDestroy()) {
            for (ItemBaseVM i : mDataItemList) {
                i.onDestroy();
            }
        }
    }
}
