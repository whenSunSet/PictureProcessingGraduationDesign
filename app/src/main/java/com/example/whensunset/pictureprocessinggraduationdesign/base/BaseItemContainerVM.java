package com.example.whensunset.pictureprocessinggraduationdesign.base;

/**
 * Created by whensunset on 2018/3/4.
 */

public abstract class BaseItemContainerVM<T extends BaseItemVM , M extends BaseItemManager<T>> extends BaseVM {
    public M mListItemManager;

    public BaseItemContainerVM() {

    }
}
