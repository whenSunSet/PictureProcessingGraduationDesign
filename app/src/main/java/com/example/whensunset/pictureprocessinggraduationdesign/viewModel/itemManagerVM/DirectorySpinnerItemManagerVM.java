package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.itemManagerVM;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.ItemSelectedUIAction;
import com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.UIActionManager;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ItemBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ItemManagerBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.SystemImageUriFetch;

import io.reactivex.Flowable;

import static com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.UIActionManager.ITEM_SELECTED_ACTION;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.DirectorySpinnerItemManagerVM_directoryName;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.ItemBaseVM_mPosition;

/**
 * Created by whensunset on 2018/3/5.
 */

public class DirectorySpinnerItemManagerVM extends ItemManagerBaseVM<DirectorySpinnerItemManagerVM.DirectorySpinnerItemVM>{
    public static final String TAG = "何时夕:DirectorySpinnerItemManagerVM";

    public DirectorySpinnerItemManagerVM() {
        super(1 , null);
        initDefaultUIActionManager();

        initItemVM();
        initItemSelected();
    }

    @Override
    protected void initDefaultUIActionManager() {
        mUIActionManager = new UIActionManager(this , ITEM_SELECTED_ACTION);
    }

    @Override
    protected void initItemVM() {
        final int[] position = {0};
        Flowable.fromIterable(SystemImageUriFetch.getInstance().getAllTag())
                .map(o -> (String)o)
                .subscribe(o -> mDataItemList.add(new DirectorySpinnerItemVM((String)o , position[0]++)));
    }

    private void initItemSelected() {
        mUIActionManager
                .<ItemSelectedUIAction>getDefaultThrottleFlowable(ITEM_SELECTED_ACTION)
                .subscribe(itemSelectedUIAction -> {
                    Integer selectedPosition = itemSelectedUIAction.getSelectedItemPosition();
                    Integer eventListenerPosition = itemSelectedUIAction.getLastEventListenerPosition();
                    String directoryName = mDataItemList.get(selectedPosition).mDirectoryName;
                    ObserverParamMap observerParamMap = ObserverParamMap
                            .staticSet(DirectorySpinnerItemManagerVM_directoryName , directoryName)
                            .set(ItemBaseVM_mPosition , selectedPosition);
                    mEventListenerList.get(eventListenerPosition).set(observerParamMap);
                    MyLog.d(TAG, "onItemSelected", "状态:directoryName:observerParamMap:", "" , directoryName , observerParamMap);
                });
    }

    public static class DirectorySpinnerItemVM extends ItemBaseVM {
        private final String mDirectoryName;

        public DirectorySpinnerItemVM(String directoryName , int position) {
            super(position);
            mDirectoryName = directoryName;
        }

        @Override
        public String toString() {
            return mDirectoryName.substring(mDirectoryName.lastIndexOf("/") + 1);
        }
    }
}
