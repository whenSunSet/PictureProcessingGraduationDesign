package com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.ObserverParamMap;

import java.util.List;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.ItemBaseVM_mPosition;

/**
 * Created by whensunset on 2018/3/23.
 */

public abstract class ItemBaseVM extends ChildBaseVM{
    public static final String TAG = "何时夕:ItemBaseVM";

    protected Integer mPosition = -1;
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

    @Override
    public boolean isNeedDestroy() {
        return false;
    }

    protected ObserverParamMap getPositionParamMap() {
        return ObserverParamMap.staticSet(ItemBaseVM_mPosition , mPosition);
    }
}
