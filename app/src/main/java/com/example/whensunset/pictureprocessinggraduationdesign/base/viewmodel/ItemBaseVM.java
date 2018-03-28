package com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel;

import android.databinding.ObservableField;

import java.util.List;

/**
 * Created by whensunset on 2018/3/23.
 */

public abstract class ItemBaseVM extends ChildBaseVM{
    public static final String TAG = "何时夕:ItemBaseVM";

    protected Integer mPosition;
    public final ObservableField<Boolean> isSelected = new ObservableField<>(false);

    public ItemBaseVM(List<ObservableField<? super Object>> eventListenerList, Integer position) {
        super(eventListenerList);
        mPosition = position;
    }

    public ItemBaseVM(int listenerSize, Integer position) {
        super(listenerSize);
        mPosition = position;
    }

    @Override
    public void resume() {
        super.resume();
        isSelected.set(true);
    }

    @Override
    public void stop() {
        super.stop();
        isSelected.set(false);
    }

    public ItemBaseVM(Integer position) {
        mPosition = position;
    }

    public Integer getPosition() {
        return mPosition;
    }

    public void setPosition(Integer position) {
        mPosition = position;
    }
}
