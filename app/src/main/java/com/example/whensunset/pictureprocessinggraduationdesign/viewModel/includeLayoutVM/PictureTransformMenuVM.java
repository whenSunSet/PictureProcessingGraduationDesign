package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil.BindingUtils;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.FlipMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.RotateMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;

import org.opencv.core.Mat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by whensunset on 2018/3/6.
 */

public class PictureTransformMenuVM extends BaseVM {
    public static final int MENU_ITEM_MARGIN = 4;
    public static final int MENU_PADDING = 10;

    public static final int MENU_ITEM_WIDTH = (BindingUtils.getDisplayWidthDp() - 2 * MENU_PADDING - 4 * 2 * MENU_ITEM_MARGIN) / 4;
    public static final int MENU_HEIGHT = MENU_ITEM_WIDTH + 2 * MENU_PADDING + 2 * MENU_ITEM_MARGIN;

    public final ObservableField<? super Object> mClickPictureCutListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureRotateListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureHorizontalFlipListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureVerticalFlipListener = new ObservableField<>();

    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();

    public PictureTransformMenuVM() {

    }

    public void clickPictureCut() {
//        mClickPictureCutListener.notifyChange();
    }

    public void clickPictureRotate() {
        RotateMyConsumer rotateMyConsumer = new RotateMyConsumer(90.0);

        mStringConsumerChain
                .rxRunNextConvenient(rotateMyConsumer)
                .subscribe(mat -> {
                    mClickPictureRotateListener.set(getMap("mat" , mat));
                });
    }

    public void clickPictureHorizontalFlip() {
        FlipMyConsumer flipMyConsumer = new FlipMyConsumer(FlipMyConsumer.HORIZONTAL);
        mStringConsumerChain
                .rxRunNextConvenient(flipMyConsumer)
                .subscribe(mat -> {
                    mClickPictureHorizontalFlipListener.set(getMap("mat" , mat));
                });
    }

    public void clickPictureVerticalFlip() {
        FlipMyConsumer flipMyConsumer = new FlipMyConsumer(FlipMyConsumer.VERTICAL);
        mStringConsumerChain
                .rxRunNextConvenient(flipMyConsumer)
                .subscribe(mat -> {
                    mClickPictureVerticalFlipListener.set(getMap("mat" , mat));
                });
    }

    private <K , V> Map<K , V> getMap(K k , V v) {
        Map<K , V> map = new HashMap<>();
        map.put(k , v);
        return map
    }

}
