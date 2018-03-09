package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * Created by whensunset on 2018/3/8.
 */

public class CutMyConsumer extends LinkedMyConsumer {

    private Rect mRect;

    public CutMyConsumer(Rect rect) {
        super(null , null);
        mRect = rect;
        mPreviousConsumer = LinkedMyConsumer.getLastConsumer(StringConsumerChain.getInstance().getFirstCutMyConsumer());
    }

    public CutMyConsumer(LinkedMyConsumer previousConsumer, LinkedMyConsumer nextConsumer, Rect rect) {
        super(previousConsumer, nextConsumer);
        mRect = rect;
    }

    @Override
    protected Mat onNewResultImpl(Mat oldResult) {
        if (oldResult == null) {
            throw new IllegalArgumentException("被剪裁的Mat 不可为null");
        }

        if (mRect == null) {
            throw new IllegalArgumentException("需要剪裁的区域Rect 不可为null");
        }

        Mat newResult = new Mat();
        cut(oldResult.nativeObj , newResult.nativeObj , mRect.x , mRect.y , mRect.width , mRect.height);
        return newResult;
    }

    @Override
    protected void onFailureImpl(Throwable t) {

    }

    @Override
    protected void onCancellationImpl() {

    }

    private native void cut(long in_mat_addr , long out_mat_addr , int x , int y , int width , int height);

    @Override
    public Mat undo(Mat oldResult) {
        if (mPreviousConsumer == null) {
            Log.d("何时夕:CutMyConsumer", ("已经是最第一个消费者了"));
            return null;
        }
        return mPreviousConsumer.onNewResult(oldResult);
    }

    public Rect getRect() {
        return mRect;
    }

    public void setRect(Rect rect) {
        mRect = rect;
    }
}
