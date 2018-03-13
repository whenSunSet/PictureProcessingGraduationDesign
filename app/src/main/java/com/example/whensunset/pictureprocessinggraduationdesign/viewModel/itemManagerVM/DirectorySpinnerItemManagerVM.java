package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.itemManagerVM;

import android.databinding.ObservableField;
import android.util.Log;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemManagerVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.SystemImageUriFetch;

import io.reactivex.Flowable;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.DirectorySpinnerItemManagerVM_directoryName;

/**
 * Created by whensunset on 2018/3/5.
 */

public class DirectorySpinnerItemManagerVM extends BaseItemManagerVM<DirectorySpinnerItemManagerVM.DirectorySpinnerItemVM> {
    public static final String TAG = "何时夕:DirectorySpinnerItemManagerVM";

    public DirectorySpinnerItemManagerVM() {
        Flowable.fromIterable(SystemImageUriFetch.getInstance().getAllTag())
                .map(o -> (String)o)
                .subscribe(o -> mDataItemList.add(new DirectorySpinnerItemVM(mClickedItemListener , (String)o)));
    }

    public void onItemSelected(int position) {
        if (position < 0) {
            mShowToast.set("被选择的目录position不能小于0");
            return;
        }

        if (mDataItemList == null || mDataItemList.size() <= position) {
            mShowToast.set("被选择的目录position不能大于等于目录总数");
            return;
        }

        DirectorySpinnerItemVM directorySpinnerItemVM = mDataItemList.get(position);
        if (directorySpinnerItemVM == null) {
            mShowToast.set("被选择的目录不可为null");
            return;
        }

        String directoryName = directorySpinnerItemVM.mDirectoryName.get();
        if (directoryName == null) {
            mShowToast.set("被选择的目录名字不可为null");
            return;
        }

        ObserverParamMap observerParamMap = ObserverParamMap.staticSet(DirectorySpinnerItemManagerVM_directoryName , directoryName);
        mClickedItemListener.set(observerParamMap);

        Log.d("何时夕:DirectorySpinner", ("position:" + position + ",observerParamMap:" + observerParamMap));
    }

    public static class DirectorySpinnerItemVM extends BaseItemVM {
        public final ObservableField<String> mDirectoryName = new ObservableField<>();
        public DirectorySpinnerItemVM(ObservableField<? super Object> clickedItemListener , String directoryName) {
            super(clickedItemListener);
            mDirectoryName.set(directoryName);
        }

        @Override
        public String toString() {
            return mDirectoryName.get().substring(mDirectoryName.get().lastIndexOf("/") + 1);
        }
    }
}
