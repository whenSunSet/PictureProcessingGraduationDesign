package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_UNCHANGED;

/**
 * Created by whensunset on 2018/3/18.
 */

public class PictureFrameMyConsumer extends BaseMyConsumer {
    public static String TAG = "何时夕:PictureFrameMyConsumer";
    public static final int DEFAULT_ALPH = 100;
    public static final int MAX_ALPH = 100;

    private int mAlpha;
    private String mFrameImagePath;
    private Rect mRect;

    public PictureFrameMyConsumer(String frameImagePath , Rect rect) {
        mFrameImagePath = frameImagePath;
        mAlpha = DEFAULT_ALPH;
        mRect = rect;
    }

    public PictureFrameMyConsumer(int alpha, String frameImagePath, Rect rect) {
        mAlpha = alpha;
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
        int alpha = ((PictureFrameMyConsumer) nextMyConsumer).mAlpha;

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

        if (!mFrameImagePath.equals(nextFrameImageFile) || !mRect.equals(nextRect) || mAlpha != alpha) {
            MyLog.d(TAG, "isNeedRun", "状态:nextFrameImageFile:mFrameImagePath:mAlpha:", "两个图片框地址或者图片框Rect或者alpha不同，需要运行" , nextFrameImageFile , mFrameImagePath , mAlpha);
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
            Mat matBgr = Imgcodecs.imread(mFrameImagePath , CV_LOAD_IMAGE_UNCHANGED);
            insertMat = new Mat();
            Imgproc.cvtColor(matBgr , insertMat , Imgproc.COLOR_BGR2BGRA);
            matBgr.release();
        }

        Mat newMat = new Mat();
        float realAlpha = (float) mAlpha / (float) MAX_ALPH;
        mixed(oldResult.getNativeObjAddr() , insertMat.getNativeObjAddr() , newMat.getNativeObjAddr() , mRect.x , mRect.y , mRect.width , mRect.height , realAlpha);
        MyLog.d(TAG, "onNewResultImpl", "状态:insertMat:oldResult:newMat:mRect:mFrameImagePath:mAlpha:realAlpha:",
                "" , insertMat , oldResult , newMat , mRect ,  mFrameImagePath , mAlpha , realAlpha);
        return newMat;
    }

    private native void mixed(long in_mat_addr , long insert_mat_addr , long out_mat_addr , int x , int y , int width , int height , float alph);

    @Override
    protected void onFailureImpl(Throwable t) {

    }

    @Override
    protected void onCancellationImpl() {

    }

}

