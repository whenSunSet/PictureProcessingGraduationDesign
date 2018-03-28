package com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil.ItemViewArg;

import java.util.List;

/**
 * Created by whensunset on 2018/3/23.
 */

public abstract class ItemManagerBaseVM <T extends ItemBaseVM> extends ParentBaseVM {
    public static final String TAG = "何时夕:ItemManagerBaseVM";
    public static final int CLICK_ITEM = 0;

    public final ObservableList<T> mDataItemList = new ObservableArrayList<>();
    public final ObservableField<Integer> mSelectedPosition = new ObservableField<>(-1);
    public final ItemViewArg.ItemViewSelector<T> mViewSelector;

    public ItemManagerBaseVM(List<ObservableField<? super Object>> eventListenerList, ItemViewArg.ItemViewSelector<T> viewSelector) {
        super(eventListenerList);
        mViewSelector = viewSelector;
    }

    public ItemManagerBaseVM(int listenerSize, ItemViewArg.ItemViewSelector<T> viewSelector) {
        super(listenerSize);
        mViewSelector = viewSelector;
    }

    public ItemManagerBaseVM(ItemViewArg.ItemViewSelector<T> viewSelector) {
        mViewSelector = viewSelector;
    }

    public ItemManagerBaseVM(List<ObservableField<? super Object>> eventListenerList , int viewModelId , int viewItemLayoutId) {
        mViewSelector = selectItemView(viewModelId , viewItemLayoutId);
    }

    public ItemManagerBaseVM(int listenerSize, int viewModelId , int viewItemLayoutId) {
        super(listenerSize);
        mViewSelector = selectItemView(viewModelId , viewItemLayoutId);
    }

    public ItemManagerBaseVM(int viewModelId , int viewItemLayoutId) {
        mViewSelector = selectItemView(viewModelId , viewItemLayoutId);
    }

    public ItemManagerBaseVM() {
        mViewSelector = null;
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


}
