package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing;

import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/8.
 */

public class FlipMyConsumer extends UndoMyConsumer {
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
        if (oldResult == null) {
            throw new IllegalArgumentException("被翻转的Mat 不可为null");
        }

        Mat newResult = new Mat();
        flip(oldResult.nativeObj , newResult.nativeObj , mFlipCode);
        return newResult;
    }

    @Override
    protected void onFailureImpl(Throwable t) {

    }

    @Override
    protected void onCancellationImpl() {

    }

    @Override
    public Mat undo(Mat oldResult) {
        return onNewResultImpl(oldResult);
    }

    private static native void flip(long src_nativeObj , long dst_nativeObj , int flipCode);

}
