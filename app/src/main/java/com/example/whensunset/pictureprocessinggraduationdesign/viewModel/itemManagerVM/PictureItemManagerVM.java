package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.itemManagerVM;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessinggraduationdesign.BR;
import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemManagerVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.IImageUriFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.SystemImageUriFetch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by whensunset on 2018/3/5.
 */

public class PictureItemManagerVM extends BaseItemManagerVM<PictureItemManagerVM.PictureItemVM> {
    public static final int ITEM_PICTURE_RESIZE_WIDTH = 100;
    public static final int ITEM_PICTURE_RESIZE_HEIGHT = 100;

    public final IImageUriFetch mIImageUriFetch;

    public PictureItemManagerVM() {
        super(BR.viewModel , R.layout.activity_main_picture_item);
        mIImageUriFetch = SystemImageUriFetch.getInstance();
        freshPictureList(mIImageUriFetch.getAllImageUriList());
    }

    public void freshPictureList(String directoryName) {
        freshPictureList(mIImageUriFetch.getALlImageUriListFromTag(directoryName));
    }

    public void freshPictureList(List<String> imageUriList) {
        mDataItemList.clear();
        final int[] nowPosition = {0};
        Flowable.fromIterable(imageUriList)
                .map(imageUri -> new PictureItemVM(mClickedItemListener, imageUri , nowPosition[0]++))
                .subscribe(mDataItemList::add);
    }

    public static class PictureItemVM extends BaseItemVM {
        public final ObservableField<String> mImageUri=new ObservableField<>();
        public final Integer mPosition;
        public PictureItemVM(ObservableField<? super Object> clickedItemListener , String imageUri , Integer position) {
            super(clickedItemListener);
            mImageUri.set(imageUri);
            mPosition = position;
        }

        public void clickPicture() {
            Map<String , Object> valueMap = new HashMap<>();
            valueMap.put("mPosition" , mPosition);
            valueMap.put("mImageUri" , mImageUri.get());
            mClickedItemListener.set(valueMap);
        }
    }
}
