package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.itemManagerVM;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessinggraduationdesign.BR;
import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemManagerVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemVM;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by whensunset on 2018/3/6.
 */

public class PictureFilterItemManagerVM extends BaseItemManagerVM<PictureFilterItemManagerVM.PictureFilterItemVM> {
    public static final String TAG = "何时夕:PictureFilterItemManagerVM";

    public static final int ITEM_PICTURE_RESIZE_WIDTH = 100;
    public static final int ITEM_PICTURE_RESIZE_HEIGHT = 100;

    public PictureFilterItemManagerVM() {
        super(BR.viewModel , R.layout.activity_main_picture_item);
    }

    public static class PictureFilterItemVM extends BaseItemVM {
        public final ObservableField<String> mImageUri=new ObservableField<>();
        public final Integer mPosition;
        public PictureFilterItemVM(ObservableField<? super Object> clickedItemListener , String imageUri , Integer position) {
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
