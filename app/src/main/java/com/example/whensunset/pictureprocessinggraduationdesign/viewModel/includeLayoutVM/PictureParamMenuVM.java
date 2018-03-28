package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.ProgressChangedUIAction;
import com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.UIActionManager;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ChildBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.PictureParamMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.UIActionManager.CLICK_ACTION;
import static com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.UIActionManager.PROGRESS_CHANGED_ACTION;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureParamMenuVM_mat;


/**
 * Created by whensunset on 2018/3/6.
 */

public class PictureParamMenuVM extends ChildBaseVM{
    public static final String TAG = "何时夕:PictureParamMenuVM";
    public static final int PROGRESS_CHANGE_THROTTLE_MILLISECONDS = 400;

    public static final int PROGRESS_MAX = 100;
    public static final int LISTENER_SIZE = 5;

    public static final int MENU_PADDING = 10;
    public static final int MENU_ITEM_SIZE = 4;
    public static final int SEEK_BAR_HEIGHT = 26;
    public static final int MENU_ITEM_MARGIN = 15;
    public static final int MENU_WIDTH = MyUtil.getDisplayWidthDp();
    public static final int MENU_ITEM_WIDTH = (MENU_WIDTH - 2 * MENU_PADDING - (MENU_ITEM_SIZE - 1) * 2 * MENU_ITEM_MARGIN) / MENU_ITEM_SIZE;
    public static final int MENU_ITEM_HEIGHT = MENU_ITEM_WIDTH;
    public static final int MENU_HEIGHT = MENU_PADDING + MENU_ITEM_WIDTH + SEEK_BAR_HEIGHT;

    public static final int SELECT_BRIGHTNESS = 0;
    public static final int SELECT_CONTRAST = 1;
    public static final int SELECT_SATURATION = 2;
    public static final int SELECT_TONAL = 3;
    public static final int PARAM_PROGRESS_CHANGE = 4;

    public final ObservableField<Integer> mSelectParam = new ObservableField<>(PROGRESS_MAX / 2);
    public final ObservableField<Boolean> isInTonal = new ObservableField<>(false);

    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    private final List<Integer> mParamList = new ArrayList<>();
    private int mNowSelectListenerPosition = SELECT_BRIGHTNESS;
    private boolean isRunNow = false;

    public PictureParamMenuVM() {
        super(LISTENER_SIZE);
        initDefaultUIActionManager();

        for (int i = 0; i < 4; i++) {
            mParamList.add(PROGRESS_MAX / 2);
        }
        initClick();
        initProgressChanged();
        MyLog.d(TAG, "PictureParamMenuVM", "状态:", "初始化了PictureParamMenuVM");
    }

    @Override
    protected void initDefaultUIActionManager() {
        mUIActionManager = new UIActionManager(this , CLICK_ACTION , PROGRESS_CHANGED_ACTION);
    }

    private void initClick() {
        getDefaultClickThrottleFlowable()
                .subscribe(position -> {
                    mNowSelectListenerPosition = position;
                    mSelectParam.set(mParamList.get(mNowSelectListenerPosition));
                    isInTonal.set(false);

                    switch (mNowSelectListenerPosition) {
                        case SELECT_BRIGHTNESS:
                            break;
                        case SELECT_CONTRAST:
                            break;
                        case SELECT_SATURATION:
                            break;
                        case SELECT_TONAL:
                            isInTonal.set(true);
                            break;
                    }

                    MyLog.d(TAG, "onClick", "状态:mNowSelectListenerPosition:mSelectParam:mParamList:",
                            "切换了需要变化了图片参数" , mNowSelectListenerPosition , mSelectParam.get() , mParamList);
                });
    }

    private void initProgressChanged() {
        mUIActionManager
                .<ProgressChangedUIAction>getDefaultThrottleFlowable(PROGRESS_CHANGED_ACTION)
                .filter(progressChangedUIAction -> {
                    int progress = progressChangedUIAction.getProgress();
                    mParamList.set(mNowSelectListenerPosition , progress);
                    mSelectParam.set(progress);
                    MyLog.d(TAG, "initProgressChangedAction", "状态:mParamList:mNowSelectListenerPosition:progress:progressChangedUIAction:",
                            "某个图片参数变化了，构建PictureParamMyConsumer" , mParamList , mNowSelectListenerPosition , progress , progressChangedUIAction);
                    for (Integer param : mParamList) {
                        if (Math.abs(param - (PROGRESS_MAX / 2)) >= 1){
                            return true;
                        }
                    }
                    return false;
                }).flatMap(progressChangedUIAction -> {
                    MyLog.d(TAG, "apply", "状态:isRunNow", "判断是否runNow" , isRunNow);
                    PictureParamMyConsumer pictureParamMyConsumer = new PictureParamMyConsumer(mParamList);
                    if (isRunNow) {
                        return mStringConsumerChain.rxRunNow(pictureParamMyConsumer);
                    } else {
                        isRunNow = true;
                        return mStringConsumerChain.rxRunNext(pictureParamMyConsumer);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(mat -> {
                    MyLog.d(TAG, "initProgressChangedAction", "状态:mat", "修改图片参数之后，PictureParamMyConsumer运行完毕，接下来要将图片显示" , mat);
                    mEventListenerList.get(PARAM_PROGRESS_CHANGE).set(ObserverParamMap.staticSet(PictureParamMenuVM_mat, mat));
                });
    }

    @Override
    public void resume() {
        super.resume();
        isRunNow = false;
        mSelectParam.set(PROGRESS_MAX / 2);
        mNowSelectListenerPosition = SELECT_BRIGHTNESS;
        for (int i = 0; i < 4; i++) {
            mParamList.set(i , PROGRESS_MAX / 2);
        }

        MyLog.d(TAG, "onResume", "状态:", "重新进入PictureParamMenuVM，重新初始化了数据");
    }

    public void fresh() {
        BaseMyConsumer consumer = mStringConsumerChain.getNowConsumer();

        if (consumer instanceof PictureParamMyConsumer) {
            PictureParamMyConsumer pictureParamMyConsumer = ((PictureParamMyConsumer) consumer);
            mSelectParam.set(pictureParamMyConsumer.getParamList().get(mNowSelectListenerPosition));

            mParamList.clear();
            mParamList.addAll(pictureParamMyConsumer.getParamList());

            MyLog.d(TAG, "fresh", "状态:pictureParamMyConsumer:mNowSelectListenerPosition:mParamList:",
                    "运行了undo或者redo并且当前处于param中并且当前指向的consumer为PictureParamMyConsumer，此时需要将数据刷新为当前PictureParamMyConsumer中的数据" , pictureParamMyConsumer , mNowSelectListenerPosition , mParamList);
        } else {
            mSelectParam.set(PROGRESS_MAX / 2);
            for (int i = 0; i < 4; i++) {
                mParamList.set(i , PROGRESS_MAX / 2);
            }

            isRunNow = false;

            MyLog.d(TAG, "fresh", "状态:consumer:mNowSelectListenerPosition:mParamList:",
                    "运行了undo或者redo并且当前处于param中但是当前的consumer不为PictureParamMyConsumer，此时需要将数据刷新为初始状态" , consumer , mNowSelectListenerPosition , mParamList);
        }
    }

}
