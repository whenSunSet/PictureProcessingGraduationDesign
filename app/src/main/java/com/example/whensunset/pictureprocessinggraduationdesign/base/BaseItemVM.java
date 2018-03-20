package com.example.whensunset.pictureprocessinggraduationdesign.base;

import android.databinding.ObservableField;

import java.util.List;

/**
 * Created by whensunset on 2018/3/4.
 */

public abstract class BaseItemVM extends BaseVM {
    public ObservableField<? super Object> mClickedItemListener;

    public BaseItemVM(ObservableField<? super Object> clickedItemListener) {
        mClickedItemListener = clickedItemListener;
    }

    public BaseItemVM(List<ObservableField<? super Object>> clickListenerList) {
        super(clickListenerList);
    }
}
