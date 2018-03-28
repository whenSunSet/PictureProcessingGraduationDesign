package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;

import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/14.
 */

public class WhiteBalanceMyConsumer extends BaseMyConsumer {
    public static String TAG = "何时夕:WhiteBalanceMyConsumer";

    @Override
    public void copy(BaseMyConsumer baseMyConsumer) {
        super.copy(baseMyConsumer);
        if (!(baseMyConsumer instanceof  CutMyConsumer)) {
            throw new RuntimeException("被拷贝的 consumer 需要和拷贝的 consumer 类型一致");
        }
    }

    @Override
    protected Mat onNewResultImpl(Mat oldResult) {
        MyLog.d(TAG, "onNewResultImpl", "状态:oldResult:" , "运行" , oldResult);

        if (oldResult == null) {
            throw new RuntimeException("被自动白平衡的Mat 不可为null");
        }

        Mat newResult = new Mat();
        whiteBalance(oldResult.nativeObj , newResult.nativeObj);

        MyLog.d(TAG, "onNewResultImpl", "状态:newResult:" , "运行完毕" , newResult);
        return newResult;
    }

    @Override
    protected void onFailureImpl(Throwable t) {

    }

    @Override
    protected void onCancellationImpl() {

    }

    private native void whiteBalance(long in_mat_addr , long out_mat_addr);

}
