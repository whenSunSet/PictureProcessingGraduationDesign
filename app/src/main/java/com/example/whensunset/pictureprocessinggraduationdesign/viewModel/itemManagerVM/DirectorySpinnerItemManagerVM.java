package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.itemManagerVM;

import android.databinding.ObservableField;
import android.util.Log;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemManagerVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemVM;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.SystemImageUriFetch;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by whensunset on 2018/3/5.
 */

public class DirectorySpinnerItemManagerVM extends BaseItemManagerVM<DirectorySpinnerItemManagerVM.DirectorySpinnerItemVM> {
    public DirectorySpinnerItemManagerVM() {
        Flowable.fromIterable(SystemImageUriFetch.getInstance().getAllTag())
                .map(o -> (String)o)
                .subscribe(o -> mDataItemList.add(new DirectorySpinnerItemVM(mClickedItemListener , (String)o)));
    }

    public void onItemSelected(int position) {
        Map<String , Object> valueMap = new HashMap<>();
        valueMap.put("mDirectoryName" , mDataItemList.get(position).mDirectoryName.get());
        mClickedItemListener.set(valueMap);
        Log.d("何时夕:DirectorySpinner", ("position:" + position + ",valueMap:" + valueMap));
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
