package com.example.whensunset.pictureprocessinggraduationdesign.base;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

/**
 * Created by whensunset on 2018/3/4.
 */

public abstract class BaseItemManager<T extends BaseItemVM> {
    public final ObservableField<? super Object> mClickedItemListener = new ObservableField<>();
    public final ObservableList<T> dataItems = new ObservableArrayList<>();

}
