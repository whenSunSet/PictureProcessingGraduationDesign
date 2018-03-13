package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.FlipMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.RotateMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;

import org.opencv.core.Mat;

import io.reactivex.functions.Consumer;

import static com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.RotateMyConsumer.ROTATE_ANGLE_90;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTransformMenuVM_mat;

/**
 * Created by whensunset on 2018/3/6.
 */

public class PictureTransformMenuVM extends BaseVM {
    public static final String TAG = "何时夕:PictureTransformMenuVM";

    public static final int MENU_ITEM_MARGIN = 4;
    public static final int MENU_PADDING = 10;
    public static final int MENU_ITEM_WIDTH = (MyUtil.getDisplayWidthDp() - 2 * MENU_PADDING - 4 * 2 * MENU_ITEM_MARGIN) / 4;
    public static final int MENU_HEIGHT = MENU_ITEM_WIDTH + 2 * MENU_PADDING + 2 * MENU_ITEM_MARGIN;


    public final ObservableField<? super Object> mClickPictureRotateListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureHorizontalFlipListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureVerticalFlipListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureCutListener = new ObservableField<>();


    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();

    public PictureTransformMenuVM() {
    }

    public void clickPictureRotate() {
        RotateMyConsumer rotateMyConsumer = new RotateMyConsumer(ROTATE_ANGLE_90);

        mStringConsumerChain
                .rxRunNextConvenient(rotateMyConsumer)
                .subscribe(subscribeAction(mClickPictureRotateListener));
    }

    public void clickPictureHorizontalFlip() {
        FlipMyConsumer flipMyConsumer = new FlipMyConsumer(FlipMyConsumer.HORIZONTAL);

        mStringConsumerChain
                .rxRunNextConvenient(flipMyConsumer)
                .subscribe(subscribeAction(mClickPictureHorizontalFlipListener));
    }

    public void clickPictureVerticalFlip() {
        FlipMyConsumer flipMyConsumer = new FlipMyConsumer(FlipMyConsumer.VERTICAL);

        mStringConsumerChain
                .rxRunNextConvenient(flipMyConsumer)
                .subscribe(subscribeAction(mClickPictureVerticalFlipListener));
    }

    public void clickPictureCut() {
    }

    private Consumer<? super Mat> subscribeAction(ObservableField<? super Object> listener) {
        return (Consumer<Mat>) mat -> {
            listener.set(ObserverParamMap.staticSet(PictureTransformMenuVM_mat, mat));
        };
    }

}
