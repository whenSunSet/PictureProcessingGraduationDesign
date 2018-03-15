package com.example.whensunset.pictureprocessinggraduationdesign.base;

import java.util.ArrayList;

/**
 * Created by whensunset on 2018/3/14.
 */

public abstract class BaseViewModelContainerVM extends BaseVM{
    public static String TAG = "何时夕:BaseViewModelContainerVM";

    public ArrayList<BaseVM> mChildViewModelList = new ArrayList<>();
    private int mChildViewModelSize = 0;
    private int mNowChildViewModelPosition = -1;

    public BaseViewModelContainerVM() {
    }

    public BaseViewModelContainerVM(int listenerSize) {
        super(listenerSize);
    }

    public BaseViewModelContainerVM(int listenerSize, int childViewModelSize) {
        super(listenerSize);
        mChildViewModelSize = childViewModelSize;
    }

    public void addChildViewModel(BaseVM childViewModel) {
        mChildViewModelList.add(childViewModel);
        mChildViewModelSize = mChildViewModelList.size();
    }

    public void changeNowChildViewModel(int position) {
        mNowChildViewModelPosition = position;
    }

    @Override
    public void onClick(int position) {
        super.onClick(position);

    }
}
