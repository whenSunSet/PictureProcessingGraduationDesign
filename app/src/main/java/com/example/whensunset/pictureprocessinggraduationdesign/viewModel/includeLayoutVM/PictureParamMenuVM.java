package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.PictureParamMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTransformMenuVM_mat;


/**
 * Created by whensunset on 2018/3/6.
 */

public class PictureParamMenuVM extends BaseVM {
    public static final String TAG = "何时夕:PictureParamMenuVM";

    public static final int THRESHOLD_OF_SAVE_CONSUMER = 33;

    public static final int MENU_PADDING = 10;
    public static final int MENU_ITEM_SIZE = 4;
    public static final int MENU_HEIGHT = PictureTransformMenuVM.MENU_HEIGHT - MENU_PADDING;
    public static final int MENU_ITEM_WIDTH = MENU_HEIGHT - MENU_PADDING;
    public static final int MENU_ITEM_MARGIN = (MyUtil.getDisplayWidthDp() - 2 * MENU_PADDING - MENU_ITEM_SIZE * MENU_ITEM_WIDTH) / (2 * (MENU_ITEM_SIZE - 1)) ;

    public static final int SELECT_BRIGHTNESS = 0;
    public static final int SELECT_CONTRAST = 1;
    public static final int SELECT_SATURATION = 2;
    public static final int SELECT_TONAL = 3;

    public final ObservableField<Integer> mSelectParam = new ObservableField<>(50);

    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    private final List<Integer> mParamList = new ArrayList<>();
    private final List<Integer> mLastNotSaveParamList = new ArrayList<>();

    private boolean isRunNow = false;
    private int mNowSelectListenerPosition = SELECT_BRIGHTNESS;

    public PictureParamMenuVM() {
        super(4);
        for (int i = 0; i < 4; i++) {
            mParamList.add(50);
            mLastNotSaveParamList.add(50);
        }

        MyLog.d(TAG, "PictureParamMenuVM", "状态:", "初始化了PictureParamMenuVM");
    }

    @Override
    public void onResume() {
        super.onResume();
        isRunNow = false;
        mNowSelectListenerPosition = 0;
        for (int i = 0; i < 4; i++) {
            mParamList.set(i , 50);
            mLastNotSaveParamList.set(i , 50);
        }

        MyLog.d(TAG, "onResume", "状态:", "重新进入PictureParamMenuVM，重新初始化了数据");
    }

    @Override
    public void onClick(int position) {
        super.onClick(position);
        mNowSelectListenerPosition = position;
        mSelectParam.set(mParamList.get(mNowSelectListenerPosition));

        switch (mNowSelectListenerPosition) {
            case SELECT_BRIGHTNESS:
                break;
            case SELECT_CONTRAST:
                break;
            case SELECT_SATURATION:
                break;
            case SELECT_TONAL:
                break;
        }

        getListener(position).notifyChange();

        MyLog.d(TAG, "onClick", "状态:mNowSelectListenerPosition:mSelectParam:mParamList:",
                "切换了需要变化了图片参数" , mNowSelectListenerPosition , mSelectParam.get() , mParamList);
    }

    public void progressChanged(int progress) {

        if (getState() != RESUME) {
            return;
        }

        mParamList.set(mNowSelectListenerPosition , progress);
        mSelectParam.set(progress);

        MyLog.d(TAG, "progressChanged", "状态:mParamList:mNowSelectListenerPosition:progress:isRunNow:",
                "某个图片参数变化了" , mParamList , mNowSelectListenerPosition , progress , isRunNow);

        PictureParamMyConsumer pictureParamMyConsumer = new PictureParamMyConsumer(mParamList);

        Flowable<Mat> flowable;
        if (isRunNow) {
            flowable = mStringConsumerChain.rxRunNowConvenient(pictureParamMyConsumer);
        } else {
            flowable = mStringConsumerChain.rxRunNextConvenient(pictureParamMyConsumer);
            isRunNow = true;
        }

        flowable.subscribe(mat -> {
            getListener(mNowSelectListenerPosition).set(ObserverParamMap.staticSet(PictureTransformMenuVM_mat, mat));
        });

        if (Math.abs(mParamList.get(mNowSelectListenerPosition) - mLastNotSaveParamList.get(mNowSelectListenerPosition)) >= THRESHOLD_OF_SAVE_CONSUMER) {
            isRunNow = false;
            mLastNotSaveParamList.set(mNowSelectListenerPosition , mParamList.get(mNowSelectListenerPosition));

            MyLog.d(TAG, "progressChanged", "状态:mLastNotSaveParamList:", "某个图片参数变化太大，需要保存起来" , mLastNotSaveParamList);
        }
    }

    public void fresh() {
        BaseMyConsumer consumer = mStringConsumerChain.getConsumerList().get(mStringConsumerChain.getConsumerPoint());
        if (consumer instanceof PictureParamMyConsumer) {
            PictureParamMyConsumer pictureParamMyConsumer = ((PictureParamMyConsumer) consumer);
            mSelectParam.set(pictureParamMyConsumer.getParamList().get(mNowSelectListenerPosition));

            mParamList.clear();
            mParamList.addAll(pictureParamMyConsumer.getParamList());

            mLastNotSaveParamList.clear();
            mLastNotSaveParamList.addAll(mParamList);

            MyLog.d(TAG, "fresh", "状态:pictureParamMyConsumer:mNowSelectListenerPosition:mParamList:mLastNotSaveParamList",
                    "运行了undo或者redo并且当前处于param中并且当前指向的consumer为PictureParamMyConsumer，此时需要将数据刷新为当前PictureParamMyConsumer中的数据" , pictureParamMyConsumer , mNowSelectListenerPosition , mParamList , mLastNotSaveParamList);
        } else {
            mSelectParam.set(50);
            for (int i = 0; i < 4; i++) {
                mParamList.set(i , 50);
                mLastNotSaveParamList.set(i , 50);
            }

            isRunNow = false;

            MyLog.d(TAG, "fresh", "状态:consumer:mNowSelectListenerPosition:mParamList:mLastNotSaveParamList:",
                    "运行了undo或者redo并且当前处于param中但是当前的consumer不为PictureParamMyConsumer，此时需要将数据刷新为初始状态" , consumer , mNowSelectListenerPosition , mParamList , mLastNotSaveParamList);
        }
    }

    public int getNowSelectListenerPosition() {
        return mNowSelectListenerPosition;
    }
}
