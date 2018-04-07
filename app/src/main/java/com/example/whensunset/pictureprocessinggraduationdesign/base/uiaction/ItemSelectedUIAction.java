package com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction;

import android.databinding.ObservableList;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ItemBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ItemManagerBaseVM;

/**
 * Created by whensunset on 2018/3/23.
 */

public class ItemSelectedUIAction extends BaseUIAction{
    String TAG = "何时夕:ItemSelectedUIAction";

    private UIActionListener<ItemSelectedUIAction> mOnItemChangedListener = null;
    private int mSelectedItemPosition = -1;

    @Override
    public void onTriggerListener(int eventListenerPosition , BaseVM baseVM , Object... params) {
        int position = (int) params[0];
        ItemManagerBaseVM<ItemBaseVM> itemManagerBaseVM = (ItemManagerBaseVM<ItemBaseVM>) baseVM;
        ObservableList<ItemBaseVM> dataItemList = itemManagerBaseVM.mDataItemList;
        if (dataItemList.size() <= position) {
            MyLog.d(TAG, "onItemSelected", "状态:position:dataItemList:", "" , position , dataItemList);
            throw new RuntimeException("被选择的目录position不能大于等于目录总数");
        }

        ItemBaseVM itemBaseVM = dataItemList.get(position);
        if (itemBaseVM == null) {
            throw new RuntimeException("被选择的目录不可为null");
        }

        mSelectedItemPosition = position;
        mLastEventListenerPosition = eventListenerPosition;

        if (mOnItemChangedListener != null) {
            mOnItemChangedListener.onUIActionChanged(this);
        }
        MyLog.d(TAG, "onTriggerListener", "状态:eventListenerPosition:position:", "触发了点击事件监听器", eventListenerPosition , position);
    }

    @Override
    public void setListener(UIActionListener<? extends UIAction> listener) {
        mOnItemChangedListener = (UIActionListener<ItemSelectedUIAction>) listener;
    }

    public int getSelectedItemPosition() {
        if (mSelectedItemPosition == -1) {
            throw new RuntimeException("还没有触发过该事件");
        }
        return mSelectedItemPosition;
    }

    @Override
    public boolean checkParams(Object[] params) {
        return (params != null && params.length >= 1 && params[0] != null && (Integer)params[0] >= 0);
    }

    @Override
    public String toString() {
        return "ItemSelectedUIAction{" +
                "mLastEventListenerPosition=" + mLastEventListenerPosition +
                ", mSelectedItemPosition=" + mSelectedItemPosition +
                '}';
    }
}
