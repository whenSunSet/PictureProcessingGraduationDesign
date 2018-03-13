package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.PictureProcessingActivityVM;


/**
 * Created by whensunset on 2018/3/6.
 */

public class PictureParamMenuVM extends BaseVM {
    public static final String TAG = "何时夕:PictureParamMenuVM";

    public static final int MENU_HEIGHT = PictureProcessingActivityVM.MENU_MAX_HEIGHT;
    public static final int MENU_ITEM_MARGIN = 2;

    public static final int MENU_PADDING = (5 * MENU_HEIGHT - MyUtil.getDisplayWidthDp()) / 8;
    public static final int MENU_ITEM_WIDTH = (MyUtil.getDisplayWidthDp() - MENU_HEIGHT) / 4 - 2 * MENU_ITEM_MARGIN;

    public final ObservableField<? super Object> mClickPictureWhiteBalanceListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureBrightnessListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureContrastListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureSaturationListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureTonalListener = new ObservableField<>();

    public PictureParamMenuVM() {

    }

    public void clickPictureWhiteBalance() {

    }

    public void clickPictureBrightness() {

    }

    public void clickPictureContrast() {

    }

    public void clickPictureSaturation() {

    }

    public void clickPictureTonal() {

    }

}
