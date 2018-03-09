package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing;

import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/8.
 */

public class RotateMyConsumer extends UndoMyConsumer {
    private boolean isClockwise = false;
    private double mAngle = 0;
    private double mScale = 1.0;

    public RotateMyConsumer(boolean isClockwise, double angle, double scale) {
        this.isClockwise = isClockwise;
        mAngle = angle;
        mScale = scale;
    }

    public RotateMyConsumer(double angle) {
        mAngle = angle;
    }

    @Override
    protected Mat onNewResultImpl(Mat oldResult) {
        if (oldResult == null) {
            throw new IllegalArgumentException("被旋转的Mat 不可为null");
        }

        if (mAngle == 0) {
            return oldResult;
        }

        if (!isClockwise) {
            mAngle = 360 - mAngle;
        }

        Mat newResult = new Mat();
        rotate(oldResult.nativeObj , newResult.nativeObj , mAngle , mScale);
        return newResult;
    }

    @Override
    protected void onFailureImpl(Throwable t) {

    }

    @Override
    protected void onCancellationImpl() {

    }

    private native void rotate(long in_mat_addr , long out_mat_addr , double angle , double scale);

    @Override
    public Mat undo(Mat oldResult) {
        if (oldResult == null) {
            throw new IllegalArgumentException("被undo的旋转的Mat 不可为null");
        }

        if (mAngle == 0) {
            return oldResult;
        }

        Mat newResult = new Mat();
        if (isClockwise) {
            rotate(oldResult.nativeObj , newResult.nativeObj , 360 - mAngle , mScale);
        } else {
            rotate(oldResult.nativeObj , newResult.nativeObj , mAngle , mScale);
        }

        return newResult;
    }
}
