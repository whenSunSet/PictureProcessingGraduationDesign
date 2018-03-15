package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.WhiteBalanceMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.FlipMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.RotateMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;

import static com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.RotateMyConsumer.ROTATE_ANGLE_90;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTransformMenuVM_mat;

/**
 * Created by whensunset on 2018/3/6.
 */

public class PictureTransformMenuVM extends BaseVM {
    public static final String TAG = "何时夕:PictureTransformMenuVM";

    public static final int MENU_PADDING = 10;
    public static final int MENU_ITEM_SIZE = 5;
    public static final int MENU_ITEM_MARGIN = 2;
    public static final int MENU_ITEM_WIDTH = (MyUtil.getDisplayWidthDp() - 2 * MENU_PADDING - (MENU_ITEM_SIZE - 1) * 2 * MENU_ITEM_MARGIN) / MENU_ITEM_SIZE;
    public static final int MENU_HEIGHT = MENU_ITEM_WIDTH + 2 * MENU_PADDING;

    public static final int SELECT_PICTURE_ROTATE = 0;
    public static final int SELECT_PICTURE_HORIZONTAL_FLIP = 1;
    public static final int SELECT_PICTURE_VERTICAL_FLIP = 2;
    public static final int SELECT_PICTURE_WHITE_BALANCE = 3;
    public static final int SELECT_PICTURE_CUT = 4;

    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();

    public PictureTransformMenuVM() {
        super(5);
    }

    @Override
    public void onClick(int position) {
        super.onClick(position);

        BaseMyConsumer consumer = null;

        switch (position) {
            case SELECT_PICTURE_ROTATE:
                showToast(MENU_ITEM_WIDTH + "");
                consumer = new RotateMyConsumer(ROTATE_ANGLE_90);
                break;
            case SELECT_PICTURE_HORIZONTAL_FLIP:
                consumer = new FlipMyConsumer(FlipMyConsumer.HORIZONTAL);
                break;
            case SELECT_PICTURE_VERTICAL_FLIP:
                consumer = new FlipMyConsumer(FlipMyConsumer.VERTICAL);
                break;
            case SELECT_PICTURE_WHITE_BALANCE:
                consumer = new WhiteBalanceMyConsumer();
                break;
            case SELECT_PICTURE_CUT:
                break;
        }
        mStringConsumerChain
                .rxRunNextConvenient(consumer)
                .subscribe(mat -> {
                    getListener(position).set(ObserverParamMap.staticSet(PictureTransformMenuVM_mat, mat));
                });
    }

}
