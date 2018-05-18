package com.example.whensunset.pictureprocessinggraduationdesign.impl;

import android.graphics.Bitmap;

import com.example.whensunset.pictureprocessinggraduationdesign.PictureProcessingApplication;
import com.example.whensunset.pictureprocessinggraduationdesign.base.IAIImageFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_8U;

/**
 * Created by whensunset on 2018/5/16.
 */

public class BaseRapidStyleMigrationAIImageFetch implements IAIImageFetch {
    public static final String TAG = "何时夕:BaseRapidStyleMigrationAIImageFetch";

    private String mModelFile = "file:///android_asset/starry.pb";
    private String mInputNode = "padsss:0";
    private String mOutputNode = "squeezesss:0";
    private TensorFlowInferenceInterface inferenceInterface;
    private float[] mInFloatValues;
    private float[] mOutFloatValues;
    private int[] mOutIntValues;

    private int mInWidth = 800;
    private int mInHeight = 600;

    private int mOutWidth = 780;
    private int mOutHeight = 580;

    public BaseRapidStyleMigrationAIImageFetch() {
        init();
    }

    public BaseRapidStyleMigrationAIImageFetch(String modelFile, String inputNode, String outputNode) {
        mModelFile = modelFile;
        mInputNode = inputNode;
        mOutputNode = outputNode;
        init();
    }

    public BaseRapidStyleMigrationAIImageFetch(String modelFile) {
        mModelFile = modelFile;
        init();
    }

    private void init(){
        long initTime = System.currentTimeMillis();
        mInFloatValues = new float[mInHeight * mInWidth * 3];
        mOutFloatValues = new float[mOutHeight * mOutWidth * 3];

        mOutIntValues = new int[mOutHeight * mOutWidth];

        inferenceInterface = new TensorFlowInferenceInterface(PictureProcessingApplication.getAppContext().getAssets(), mModelFile);
        MyLog.d(TAG, "BaseRapidStyleMigrati、onAIImageFetch", "状态:初始化时间", "快速风格迁移初始化时间", System.currentTimeMillis() - initTime);
    }

    public Mat run(Mat oldMat){
        long initTime = System.currentTimeMillis();
        int realWidth = oldMat.width();
        int realHeight = oldMat.height();

        Size inSize = new Size(mInWidth, mInHeight);
        Mat inMat = new Mat(inSize, CV_32F);
        Mat middleMat = new Mat(inSize, CV_8U);

        Imgproc.resize(oldMat, middleMat, inSize);
        middleMat.convertTo(inMat, CV_32F);

        inMat.get(0, 0, mInFloatValues);
        middleMat.release();
        MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:读取图片数据时间", "快速风格迁移读取图片数据时间", System.currentTimeMillis() - initTime);


        initTime = System.currentTimeMillis();
        inferenceInterface.feed(mInputNode, mInFloatValues, 1, mInHeight, mInWidth, 3);
        inferenceInterface.run(new String[] {mOutputNode}, true);
        inferenceInterface.fetch(mOutputNode, mOutFloatValues);
        MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:运行时间", "快速风格迁移运行时间", System.currentTimeMillis() - initTime);


        initTime = System.currentTimeMillis();
        for (int i = 0; i < mOutIntValues.length; ++i) {
            mOutIntValues[i] =
                    0xFF000000
                            | (((int) (mOutFloatValues[i * 3 + 2])) << 16)
                            | (((int) (mOutFloatValues[i * 3 + 1])) << 8)
                            | (((int) (mOutFloatValues[i * 3])));
        }

        Bitmap result = Bitmap.createBitmap(mOutWidth,  mOutHeight, Bitmap.Config.ARGB_8888);
        result.setPixels(mOutIntValues, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());

        Size outSize = new Size(mOutWidth, mOutHeight);
        Mat middleMat2 = new Mat(outSize, CV_8U);
        Utils.bitmapToMat(result, middleMat2);

        Size realSize = new Size(realWidth, realHeight);
        Mat outMat = new Mat(realSize, CV_8U);
        Imgproc.resize(middleMat2, outMat, realSize);

        middleMat2.release();
        result.recycle();
        result = null;
        MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:传出数据时间", "快速风格迁移传出数据时间", System.currentTimeMillis() - initTime);

        return outMat;
    }
}
