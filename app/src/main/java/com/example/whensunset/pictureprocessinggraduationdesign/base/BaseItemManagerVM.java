package com.example.whensunset.pictureprocessinggraduationdesign.base;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil.ItemViewArg;

/**
 * Created by whensunset on 2018/3/4.
 */

public abstract class BaseItemManagerVM<T extends BaseItemVM> extends BaseVM {
    public final ObservableField<? super Object> mClickedItemListener = new ObservableField<>();
    public final ObservableList<T> mDataItemList = new ObservableArrayList<>();
    public final ItemViewArg.ItemViewSelector<T> mViewSelector;

    public BaseItemManagerVM(int viewModelId , int viewItemLayoutId) {
        mViewSelector = selectItemView(viewModelId , viewItemLayoutId);
    }

    public BaseItemManagerVM() {
        mViewSelector = null;
    }

    protected ItemViewArg.ItemViewSelector<T> selectItemView(int viewCount , int viewModelId , int viewItemLayoutId) {
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

    protected ItemViewArg.ItemViewSelector<T> selectItemView(int viewModelId , int viewItemLayoutId) {
        return selectItemView(1 , viewModelId , viewItemLayoutId);
    }

}
