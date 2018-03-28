package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by whensunset on 2018/3/14.
 */

public class PictureParamMyConsumer extends BaseMyConsumer {
    public static String TAG = "何时夕:PictureParamMyConsumer";
    public static final int DEFAULT_PARAM = 50;

    private final List<Integer> mParamList = new ArrayList<>();
    private int mBrightness = DEFAULT_PARAM;
    private int mContrast = DEFAULT_PARAM;
    private int mSaturation = DEFAULT_PARAM;
    private int mTonal = DEFAULT_PARAM;

    public PictureParamMyConsumer(int brightness, int contrast, int saturation, int tonal) {
        mBrightness = brightness;
        mContrast = contrast;
        mSaturation = saturation;
        mTonal = tonal;

        mParamList.add(mBrightness);
        mParamList.add(mContrast);
        mParamList.add(mSaturation);
        mParamList.add(mTonal);
    }

    public PictureParamMyConsumer(List<Integer> paramList) {
        if (paramList == null || paramList.size() != 4) {
            throw new RuntimeException("构建PictureParam时传入的参数不对：" + paramList.toString());
        }
        mParamList.addAll(paramList);
        mBrightness = paramList.get(PictureParamMenuVM.SELECT_BRIGHTNESS);
        mContrast = paramList.get(PictureParamMenuVM.SELECT_CONTRAST);
        mSaturation = paramList.get(PictureParamMenuVM.SELECT_SATURATION);
        mTonal = paramList.get(PictureParamMenuVM.SELECT_TONAL);

        MyLog.d(TAG, "PictureParamMyConsumer", "状态:mBrightness:mContrast:mSaturation:mTonal:mParamList:",
                "构建PictureParamMyConsumer" , mBrightness , mContrast , mSaturation , mTonal , mParamList);
    }

    @Override
    public void copy(BaseMyConsumer baseMyConsumer) {
        super.copy(baseMyConsumer);
        if (!(baseMyConsumer instanceof PictureParamMyConsumer)) {
            throw new RuntimeException("被拷贝的 consumer 需要和拷贝的 consumer 类型一致");
        }

        PictureParamMyConsumer beCopyConsumer = (PictureParamMyConsumer) baseMyConsumer;
        mBrightness = beCopyConsumer.mBrightness;
        mContrast = beCopyConsumer.mContrast;
        mSaturation = beCopyConsumer.mSaturation;
        mTonal = beCopyConsumer.mTonal;
        mParamList.clear();
        mParamList.addAll(beCopyConsumer.getParamList());

        MyLog.d(TAG, "copy", "状态:mBrightness:mContrast:mSaturation:mTonal:mParamList:",
                "拷贝PictureParamMyConsumer" , mBrightness , mContrast , mSaturation , mTonal , mParamList);
    }

    @Override
    public boolean isNeedRun(BaseMyConsumer nextMyConsumer) {
        super.isNeedRun(nextMyConsumer);

        if (!(nextMyConsumer instanceof PictureParamMyConsumer)) {
            MyLog.d(TAG, "isNeedRun", "状态:", "类型不同，需要运行");
            return true;
        }

        List<Integer> paramList = ((PictureParamMyConsumer) nextMyConsumer).mParamList;
        for (int i = 0; i < paramList.size(); i++) {
            if (Math.abs(paramList.get(i) - mParamList.get(i)) >= 1) {
                MyLog.d(TAG, "isNeedRun", "状态:", "有一个参数变化的幅度超过了1，需要运行");
                return true;
            }
        }

        MyLog.d(TAG, "isNeedRun", "状态:", "类型一致，而且每个参数变化的幅度都没有超过1，不需要运行");
        return false;
    }

    @Override
    protected Mat onNewResultImpl(Mat oldResult) {
        MyLog.d(TAG, "onNewResultImpl", "状态:oldResult:", "运行", oldResult);

        if (oldResult == null) {
            throw new RuntimeException("被调整参数的Mat 不可为null");
        }

        int realBrightness = (int) (((double) mBrightness - (double) DEFAULT_PARAM) * 5.1);
        int realContrast = (int) (((double) mContrast - (double) DEFAULT_PARAM) * 5.1);
        int realSaturation = (int) (((double) mSaturation - (double) DEFAULT_PARAM) * 5.1);
        int realTonal = (int) (((double) mTonal - (double) DEFAULT_PARAM) * 3.6);
        Mat newResult = new Mat();
        pictureParamChange(oldResult.getNativeObjAddr(), newResult.getNativeObjAddr() , realBrightness , realContrast , realSaturation , realTonal);

        MyLog.d(TAG, "onNewResultImpl", "状态:realBrightness:realContrast:realSaturation:realTonal:mParamList:newResult:",
                "运行PictureParamMyConsumer" , realBrightness , realContrast , realSaturation , realTonal , mParamList , newResult);
        return newResult;
    }

    @Override
    protected void onFailureImpl(Throwable t) {

    }

    @Override
    protected void onCancellationImpl() {

    }

    private native void pictureParamChange(long in_mat_addr, long out_mat_addr, int brightness, int contrast, int saturation, int tonal);

    public List<Integer> getParamList() {
        return mParamList;
    }

    @Override
    public String toString() {
        return "PictureParamMyConsumer{" +
                "mParamList=" + mParamList +
                ", mBrightness=" + mBrightness +
                ", mContrast=" + mContrast +
                ", mSaturation=" + mSaturation +
                ", mTonal=" + mTonal +
                '}';
    }
}
