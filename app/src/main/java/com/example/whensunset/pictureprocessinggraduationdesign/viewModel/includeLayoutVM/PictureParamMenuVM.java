package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ViewModelProgressChangeOnSubscribe;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.PictureParamMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureParamMenuVM_mat;


/**
 * Created by whensunset on 2018/3/6.
 */

public class PictureParamMenuVM extends BaseVM {
    public static final String TAG = "何时夕:PictureParamMenuVM";

    public static final int PROGRESS_MAX = 100;
    public static final int LISTENER_SIZE = 4;

    public static final int MENU_PADDING = 10;
    public static final int MENU_ITEM_SIZE = 4;
    public static final int MENU_HEIGHT = PictureTransformMenuVM.MENU_HEIGHT - MENU_PADDING;
    public static final int MENU_ITEM_WIDTH = MENU_HEIGHT - MENU_PADDING;
    public static final int MENU_ITEM_MARGIN = (MyUtil.getDisplayWidthDp() - 2 * MENU_PADDING - MENU_ITEM_SIZE * MENU_ITEM_WIDTH) / (2 * (MENU_ITEM_SIZE - 1)) ;

    public static final int SELECT_BRIGHTNESS = 0;
    public static final int SELECT_CONTRAST = 1;
    public static final int SELECT_SATURATION = 2;
    public static final int SELECT_TONAL = 3;

    public final ObservableField<Integer> mSelectParam = new ObservableField<>(PROGRESS_MAX / 2);

    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    private final List<Integer> mParamList = new ArrayList<>();
    private int mNowSelectListenerPosition = SELECT_BRIGHTNESS;
    private boolean isRunNow = false;

    public PictureParamMenuVM() {
        super(LISTENER_SIZE);
        for (int i = 0; i < LISTENER_SIZE; i++) {
            mParamList.add(PROGRESS_MAX / 2);
        }
        initClickAction();
        initProgressChangedAction();
        MyLog.d(TAG, "PictureParamMenuVM", "状态:", "初始化了PictureParamMenuVM");
    }

    @Override
    public void onResume() {
        super.onResume();
        isRunNow = false;
        mSelectParam.set(PROGRESS_MAX / 2);
        mNowSelectListenerPosition = SELECT_BRIGHTNESS;
        for (int i = 0; i < LISTENER_SIZE; i++) {
            mParamList.set(i , PROGRESS_MAX / 2);
        }

        MyLog.d(TAG, "onResume", "状态:", "重新进入PictureParamMenuVM，重新初始化了数据");
    }

    @Override
    protected void initClickAction() {
        getDefaultClickFlowable()
                .subscribe(position -> {
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

                    MyLog.d(TAG, "onClick", "状态:mNowSelectListenerPosition:mSelectParam:mParamList:",
                            "切换了需要变化了图片参数" , mNowSelectListenerPosition , mSelectParam.get() , mParamList);
                });
    }

    private void initProgressChangedAction() {
        Flowable.create(new ViewModelProgressChangeOnSubscribe(this) , BackpressureStrategy.BUFFER)
                .throttleFirst(100 , TimeUnit.MILLISECONDS)
                .filter(baseVM -> (baseVM != null))
                .filter(baseVM -> (getState() == RESUME))
                .map(BaseVM::getProgress)
                .map(progress -> {
                    mParamList.set(mNowSelectListenerPosition , progress);
                    mSelectParam.set(progress);
                    MyLog.d(TAG, "initProgressChangedAction", "状态:mParamList:mNowSelectListenerPosition:progress:",
                            "某个图片参数变化了，构建PictureParamMyConsumer" , mParamList , mNowSelectListenerPosition , progress);
                    return new PictureParamMyConsumer(mParamList);
                }).filter(pictureParamMyConsumer -> {
                    MyLog.d(TAG, "initProgressChangedAction", "状态:pictureParamMyConsumer:", "判断目前是否处于初始状态，如果处于就不运行consumer" , pictureParamMyConsumer);
                    for (Integer param : pictureParamMyConsumer.getParamList()) {
                        if (Math.abs(param - (PROGRESS_MAX / 2)) >= 1){
                            return true;
                        }
                    }
                    return false;
                }).flatMap((Function<PictureParamMyConsumer, Publisher<?>>) pictureParamMyConsumer -> {
                    MyLog.d(TAG, "apply", "状态:pictureParamMyConsumer:isRunNow", "判断是否runNow" , pictureParamMyConsumer , isRunNow);
                    if (isRunNow) {
                        return mStringConsumerChain.rxRunNow(pictureParamMyConsumer);
                    } else {
                        isRunNow = true;
                        return mStringConsumerChain.rxRunNext(pictureParamMyConsumer);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(mat -> {
                    MyLog.d(TAG, "initProgressChangedAction", "状态:mat", "修改图片参数之后，PictureParamMyConsumer运行完毕，接下来要将图片显示" , mat);
                    getListener(mNowSelectListenerPosition).set(ObserverParamMap.staticSet(PictureParamMenuVM_mat, mat));
                });
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

    public int getNowSelectListenerPosition() {
        return mNowSelectListenerPosition;
    }
}
