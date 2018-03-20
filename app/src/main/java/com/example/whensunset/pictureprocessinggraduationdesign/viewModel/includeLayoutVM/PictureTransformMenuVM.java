package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ViewModelClickOnSubscribe;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.CutMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.FlipMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.RotateMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.WhiteBalanceMyConsumer;

import org.opencv.core.Rect;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import static com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.RotateMyConsumer.ROTATE_ANGLE_90;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureFrameItemVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTransformMenuVM_mat;

/**
 * Created by whensunset on 2018/3/6.
 */

public class PictureTransformMenuVM extends BaseVM implements CutView.OnLimitRectChangedListener{
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
    public static final int LEAVE_TRANSFORM_LISTENER = 5;

    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    public PictureTransformMenuVM() {
        super(6);
        initClickAction();
    }

    @Override
    protected void initClickAction() {
        Flowable.create(new ViewModelClickOnSubscribe(this) , BackpressureStrategy.BUFFER)
                .throttleFirst(400 , TimeUnit.MILLISECONDS)
                .subscribe(baseVM -> {
                    MyLog.d(TAG, "initClickAction", "状态:baseVM:", "" , baseVM);
                    if (baseVM == null) {
                        return;
                    }
                    int position = baseVM.getClickPosition();
                    BaseMyConsumer consumer = null;
                    switch (position) {
                        case SELECT_PICTURE_ROTATE:
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

                    MyLog.d(TAG, "initClickAction", "状态:consumer:", "" , consumer);
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        nowCutRect = mStringConsumerChain.getNowRect();
    }

    @Override
    public void onStop() {
        super.onStop();
        runCut();
    }

    private void runCut() {
        boolean isNeedRunCut = isImageSizeChanged(nowCutRect , mStringConsumerChain.getNowRect());
        if (isNeedRunCut) {
            CutMyConsumer cutMyConsumer = new CutMyConsumer(nowCutRect);
            mStringConsumerChain
                    .rxRunNextConvenient(cutMyConsumer)
                    .subscribe(mat -> getListener(LEAVE_TRANSFORM_LISTENER).set(ObserverParamMap.staticSet(PictureFrameItemVM_mat , mat)));
        }

        MyLog.d(TAG, "runCut", "状态:mStringConsumerChain.getNowRect():nowCutRect:isNeedRunCut:", "离开了transform，所以进行了图片剪裁" , mStringConsumerChain.getNowRect() , nowCutRect , isNeedRunCut);
    }

    private Rect nowCutRect = new Rect();
    @Override
    public void onLimitRectChanged(Rect cutRect) {
        if (isImageSizeChanged(cutRect , nowCutRect)) {
            nowCutRect = cutRect;
            MyLog.d(TAG, "onLimitRectChanged", "状态:cutRect", "图片限制框发生改变" , cutRect);
        }
    }

    private boolean isImageSizeChanged(Rect nowRect , Rect lastCut) {
        if (Math.abs(lastCut.width - nowRect.width) >= 2 || Math.abs(lastCut.height - nowRect.height) >= 2 || Math.abs(lastCut.x - nowRect.x) >= 2 || Math.abs(lastCut.y - nowRect.y) >= 2) {
            return true;
        }
        return false;
    }

}
