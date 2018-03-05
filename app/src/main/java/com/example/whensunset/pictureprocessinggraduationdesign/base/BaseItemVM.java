package com.example.whensunset.pictureprocessinggraduationdesign.base;

import android.databinding.ObservableField;

/**
 * Created by whensunset on 2018/3/4.
 */

public abstract class BaseItemVM extends BaseVM {
    public final ObservableField<? super Object> mClickedItemListener;

    public BaseItemVM(ObservableField<? super Object> clickedItemListener) {
        mClickedItemListener = clickedItemListener;
    }
}
