package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.itemManagerVM;

import android.databinding.ObservableField;
import android.view.ViewGroup;

import com.example.whensunset.pictureprocessinggraduationdesign.BR;
import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemManagerVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.IImageUriFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.SystemImageUriFetch;

import java.util.List;

import io.reactivex.Flowable;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureItemManagerVM_mImageUri;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureItemManagerVM_mPosition;

/**
 * Created by whensunset on 2018/3/5.
 */

public class PictureItemManagerVM extends BaseItemManagerVM<PictureItemManagerVM.PictureItemVM> {
    public static final String TAG = "何时夕:PictureItemManagerVM";

    public static final int ITEM_PICTURE_RESIZE_WIDTH = 100;
    public static final int ITEM_PICTURE_RESIZE_HEIGHT = 100;

    public final int mItemHeight = MyUtil.getDisplayWidth() / 3;
    public final int mItemWidth = ViewGroup.LayoutParams.MATCH_PARENT;

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
            ObserverParamMap observerParamMap = ObserverParamMap
                    .staticSet(PictureItemManagerVM_mPosition , mPosition)
                    .set(PictureItemManagerVM_mImageUri , mImageUri.get());
            mClickedItemListener.set(observerParamMap);
        }
    }
}
