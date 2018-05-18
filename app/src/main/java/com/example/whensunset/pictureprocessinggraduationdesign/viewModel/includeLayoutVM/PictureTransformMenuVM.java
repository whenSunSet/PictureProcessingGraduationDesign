package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM;

import com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.ClickUIAction;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ChildBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.CutMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.FlipMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.RotateMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.WhiteBalanceMyConsumer;

import org.opencv.core.Rect;

import static com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.UIActionManager.CLICK_ACTION;
import static com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.RotateMyConsumer.ROTATE_ANGLE_90;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureFrameItemVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTransformMenuVM_mat;

/**
 * Created by whensunset on 2018/3/6.
 */

public class PictureTransformMenuVM extends ChildBaseVM implements CutView.OnLimitRectChangedListener{
    public static final String TAG = "何时夕:PictureTransformMenuVM";

    public static final int MENU_PADDING = 10;
    public static final int MENU_ITEM_SIZE = 5;
    public static final int MENU_ITEM_WIDTH = PictureParamMenuVM.MENU_ITEM_WIDTH;
    public static final int MENU_ITEM_HEIGHT = MENU_ITEM_WIDTH;
    public static final int MENU_HEIGHT = PictureParamMenuVM.MENU_HEIGHT;
    public static final int MENU_WIDTH = MyUtil.getDisplayWidthDp();
    public static final int MENU_ITEM_MARGIN = (MENU_WIDTH - 2 * MENU_PADDING - MENU_ITEM_SIZE * MENU_ITEM_HEIGHT) / (2 * (MENU_ITEM_SIZE - 1));

    public static final int SELECT_PICTURE_ROTATE = 0;
    public static final int SELECT_PICTURE_HORIZONTAL_FLIP = 1;
    public static final int SELECT_PICTURE_VERTICAL_FLIP = 2;
    public static final int SELECT_PICTURE_WHITE_BALANCE = 3;
    public static final int SELECT_PICTURE_CUT = 4;
    public static final int LEAVE_TRANSFORM_LISTENER = 5;

    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    public PictureTransformMenuVM() {
        super(6);
        initDefaultUIActionManager();

        initClick();
    }

    private void initClick() {
        mUIActionManager
                .<ClickUIAction>getDefaultThrottleFlowable(CLICK_ACTION)
                .subscribe(clickUIAction -> {
                    int position = clickUIAction.getLastEventListenerPosition();
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
                            clickUIAction.getCallAllAfterEventAction().callAllAfterEventAction();
                            return;
                    }
                    mStringConsumerChain
                            .rxRunNextConvenient(consumer)
                            .subscribe(mat -> {
                                mEventListenerList.get(position).set(ObserverParamMap.staticSet(PictureTransformMenuVM_mat, mat));

                                clickUIAction.getCallAllAfterEventAction().callAllAfterEventAction();
                            });

                    MyLog.d(TAG, "initClickAction", "状态:consumer:", "" , consumer);
                });
    }

    @Override
    public void resume() {
        super.resume();
        nowCutRect = mStringConsumerChain.getNowRect();
    }

    @Override
    public void stop() {
        super.stop();
        runCut();
    }

    private void runCut() {
        boolean isNeedRunCut = isImageSizeChanged(nowCutRect , mStringConsumerChain.getNowRect());
        if (isNeedRunCut) {
            CutMyConsumer cutMyConsumer = new CutMyConsumer(nowCutRect);
            mStringConsumerChain
                    .rxRunNextConvenient(cutMyConsumer)
                    .subscribe(mat -> mEventListenerList.get(LEAVE_TRANSFORM_LISTENER).set(ObserverParamMap.staticSet(PictureFrameItemVM_mat , mat)));
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
}
