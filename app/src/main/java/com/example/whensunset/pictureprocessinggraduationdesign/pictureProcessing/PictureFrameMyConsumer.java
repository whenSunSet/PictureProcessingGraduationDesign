package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_COLOR;
import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_UNCHANGED;

/**
 * Created by whensunset on 2018/3/18.
 */

public class PictureFrameMyConsumer extends BaseMyConsumer {
    public static String TAG = "何时夕:PictureFrameMyConsumer";

    private String mFrameImagePath;
    private Rect mRect;

    public PictureFrameMyConsumer(String frameImagePath, Rect rect) {
        mFrameImagePath = frameImagePath;
        mRect = rect;
    }

    public PictureFrameMyConsumer() {
    }

    @Override
    public void copy(BaseMyConsumer baseMyConsumer) {
        super.copy(baseMyConsumer);
        if (!(baseMyConsumer instanceof PictureFrameMyConsumer)) {
            throw new RuntimeException("被拷贝的 consumer 需要和拷贝的 consumer 类型一致");
        }

        PictureFrameMyConsumer beCopyConsumer = (PictureFrameMyConsumer) baseMyConsumer;
        mFrameImagePath = beCopyConsumer.mFrameImagePath;
    }

    @Override
    public boolean isNeedRun(BaseMyConsumer nextMyConsumer) {
        super.isNeedRun(nextMyConsumer);

        if (!(nextMyConsumer instanceof PictureFrameMyConsumer)) {
            MyLog.d(TAG, "isNeedRun", "状态:", "类型不同，需要运行");
            return true;
        }

        String nextFrameImageFile = ((PictureFrameMyConsumer) nextMyConsumer).mFrameImagePath;
        Rect nextRect  = ((PictureFrameMyConsumer) nextMyConsumer).mRect;

        if (nextFrameImageFile == null || nextRect == null) {
            MyLog.d(TAG, "isNeedRun", "状态:nextFrameImageFile:nextRect:", "图片框地址或者图片框Rect为null，不需要运行");
            return false;
        }

        File frameImageFile = new File(nextFrameImageFile);
        if (!frameImageFile.exists()) {
            MyLog.d(TAG, "isNeedRun", "状态:nextFrameImageFile:", "图片框文件不存在，不需要运行" , nextFrameImageFile);
            return false;
        }

        if (!frameImageFile.isFile()) {
            MyLog.d(TAG, "isNeedRun", "状态:nextFrameImageFile", "图片框不是文件，不需要运行" , nextFrameImageFile);
            return false;
        }

        if (!mFrameImagePath.equals(nextFrameImageFile) || !mRect.equals(nextRect)) {
            MyLog.d(TAG, "isNeedRun", "状态:nextFrameImageFile:mFrameImagePath:", "两个图片框地址或者图片框Rect不同，需要运行" , nextFrameImageFile , mFrameImagePath);
            return true;
        }

        return false;
    }

    @Override
    protected Mat onNewResultImpl(Mat oldResult) {
        MyLog.d(TAG, "onNewResultImpl", "状态:oldResult:", "运行", oldResult);

        if (oldResult == null) {
            throw new RuntimeException("被添加图形框的参数的Mat 不可为null");
        }
        Mat insertMat;
        if (mFrameImagePath.contains(".png")) {
            insertMat = Imgcodecs.imread(mFrameImagePath , CV_LOAD_IMAGE_UNCHANGED);
        } else {
            insertMat = Imgcodecs.imread(mFrameImagePath , CV_LOAD_IMAGE_COLOR);
        }

        Mat newMat = new Mat();
        mixed(oldResult.getNativeObjAddr() , insertMat.getNativeObjAddr() , newMat.getNativeObjAddr() , mRect.x , mRect.y , mRect.width , mRect.height);
        MyLog.d(TAG, "onNewResultImpl", "状态:insertMat:oldResult:newMat:mRect:mFrameImagePath:", "" , insertMat , oldResult , newMat , mRect , mFrameImagePath);
        return newMat;
    }

    private native void mixed(long in_mat_addr , long insert_mat_addr , long out_mat_addr , int x , int y , int width , int height);

    @Override
    protected void onFailureImpl(Throwable t) {

    }

    @Override
    protected void onCancellationImpl() {

    }

}

