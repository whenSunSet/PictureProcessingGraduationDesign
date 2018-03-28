package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.UndoMyConsumer;

import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/8.
 */

public class FlipMyConsumer extends UndoMyConsumer {
    public static final String TAG = "何时夕:UndoMyConsumer";

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;

    private int mFlipCode = 1;

    public FlipMyConsumer(int flipCode) {
        mFlipCode = flipCode;
    }

    public FlipMyConsumer() {
    }

    @Override
    protected Mat onNewResultImpl(Mat oldResult) {
        MyLog.d(TAG, "onNewResultImpl", "状态:oldResult:mFlipCode:" , "运行" , oldResult , mFlipCode);

        if (oldResult == null) {
            throw new IllegalArgumentException("被翻转的Mat 不可为null");
        }

        Mat newResult = new Mat();
        flip(oldResult.nativeObj , newResult.nativeObj , mFlipCode);

        MyLog.d(TAG, "onNewResultImpl", "状态:newResult:" , "运行完毕" , newResult);
        return newResult;
    }

    @Override
    protected void onFailureImpl(Throwable t) {

    }

    @Override
    protected void onCancellationImpl() {

    }

    @Override
    public void copy(BaseMyConsumer baseMyConsumer) {
        super.copy(baseMyConsumer);
        if (!(baseMyConsumer instanceof FlipMyConsumer)) {
            throw new RuntimeException("被拷贝的 consumer 需要和拷贝的 consumer 类型一致");
        }

        FlipMyConsumer beCopyConsumer = (FlipMyConsumer) baseMyConsumer;
        this.mFlipCode = beCopyConsumer.mFlipCode;
    }

    @Override
    public Mat undo(Mat oldResult) {
        return onNewResultImpl(oldResult);
    }

    @Override
    public String toString() {
        return "FlipMyConsumer{" +
                "mFlipCode=" + mFlipCode +
                '}';
    }

    private static native void flip(long src_nativeObj , long dst_nativeObj , int flipCode);

}
