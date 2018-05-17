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

    private String mModelFile = "file:///android_asset/real.pb";
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

//        Iterator<Operation> operationIterator = inferenceInterface.graph().operations();
//
//        while (operationIterator.hasNext()){
//            Operation operation = operationIterator.next();
//            MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:operation", "", operation.name());
//        }
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
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        options.outHeight = mInHeight;
//        options.outWidth = mInWidth;
//        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        int realWidth = oldMat.width();
        int realHeight = oldMat.height();

        Size inSize = new Size(mInWidth, mInHeight);
        Mat inMat = new Mat(inSize, CV_32F);
        Mat middleMat = new Mat(inSize, CV_8U);
        Imgproc.resize(oldMat, middleMat, inSize);
        middleMat.convertTo(inMat, CV_32F);
//        Converters.Mat_to_vector_float();
        inMat.get(0, 0, mInFloatValues);
        middleMat.release();

//        MyLog.d(TAG, "run", "状态:realWidth:realHeight:inMat.channels():sizess", "", realWidth, realHeight, inMat.channels(), sizess);
        MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:读取图片数据时间", "快速风格迁移读取图片数据时间", System.currentTimeMillis() - initTime);


//        initTime = System.currentTimeMillis();
//        for (int i = 0; i < mInFloatValues.length; ++i) {
//            mInFloatValues[i] = mInByteValues[i] + 127;
//        }
//        MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:从图片中数据转换时间", "快速风格迁移读取数据时间", System.currentTimeMillis() - initTime);

//        initTime = System.currentTimeMillis();
//
//
//        try {
//            for (int i = 0; i < mInFloatValues.length; ++i) {
//                int a = ((int) (mInFloatValues[i * 3 + 2]));
//                int b = ((int) (mInFloatValues[i * 3 + 1]));
//                int c = ((int) (mInFloatValues[i * 3]));
//                int max = (((a > b) ? a : b) > c) ? ((a > b) ? a : b) : c;
//                int min = (((a < b) ? a : b) < c) ? ((a < b) ? a : b) : c;
//
//
//                if ((a + b + c) >= 705 && (max - min) <= 3) {
//                    mInFloatValues[i * 3 + 2] = 230f;
//                    mInFloatValues[i * 3 + 1] = 235f;
//                    mInFloatValues[i * 3] = 240f;
//                    MyLog.d(TAG, "run", "状态:a:b:c:max:min", "太黑或者太白 白", a, b, c, max, min);
//                } else if ((a + b + c) <= 60 && (max - min) <= 3){
//                    mInFloatValues[i * 3 + 2] = 3 * mInFloatValues[i * 3 + 2];
//                    mInFloatValues[i * 3 + 1] = 3 * mInFloatValues[i * 3 + 1];
//                    mInFloatValues[i * 3] = 3 * mInFloatValues[i * 3];
//                    MyLog.d(TAG, "run", "状态:a:b:c:max:min", "太黑或者太白 黑", a, b, c, max, min);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:数据处理时间:e", "快速风格迁移数据处理时间", System.currentTimeMillis() - initTime, e.getCause());
//        }
//        MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:数据处理时间", "快速风格迁移数据处理时间", System.currentTimeMillis() - initTime);

        initTime = System.currentTimeMillis();
        inferenceInterface.feed(mInputNode, mInFloatValues, 1, mInHeight, mInWidth, 3);
        MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:传入数据时间", "快速风格迁移传入数据时间", System.currentTimeMillis() - initTime);

        initTime = System.currentTimeMillis();
        inferenceInterface.run(new String[] {mOutputNode}, true);
        inferenceInterface.fetch(mOutputNode, mOutFloatValues);
        MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:运行时间", "快速风格迁移运行时间", System.currentTimeMillis() - initTime);

        for (int i = 0; i < mOutIntValues.length; ++i) {
            mOutIntValues[i] =
                    0xFF000000
                            | (((int) (mOutFloatValues[i * 3 + 2])) << 16)
                            | (((int) (mOutFloatValues[i * 3 + 1])) << 8)
                            | (((int) (mOutFloatValues[i * 3])));
        }

//        for (int i = 0; i < mInIntValues.length; ++i) {
//            mInIntValues[i] =
//                    0xFF000000
//                            | (((int) (mInFloatValues[i * 3])) << 16)
//                            | (((int) (mInFloatValues[i * 3 + 1])) << 8)
//                            | ((int) (mInFloatValues[i * 3 + 2]));
//        }

        initTime = System.currentTimeMillis();
        Bitmap result = Bitmap.createBitmap(mOutWidth,  mOutHeight, Bitmap.Config.ARGB_8888);
        result.setPixels(mOutIntValues, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());

        Size outSize = new Size(mOutWidth, mOutHeight);
        Mat middleMat2 = new Mat(outSize, CV_8U);
        Utils.bitmapToMat(result, middleMat2);

        Size realSize = new Size(realWidth, realHeight);
        Mat outMat = new Mat(realSize, CV_8U);
        Imgproc.resize(middleMat2, outMat, realSize);
        MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:传出数据时间", "快速风格迁移传出数据时间", System.currentTimeMillis() - initTime);

//        MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:将数据传回图片时间", "快速风格迁移数据传回图片时间", System.currentTimeMillis() - initTime);

//        initTime = System.currentTimeMillis();
//        MyUtil.saveBitmap(result, AI_PROCCESSED_IMAGE);
//        MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:保存图片时间", "快速风格迁移保存图片时间", System.currentTimeMillis() - initTime);

//        initTime = System.currentTimeMillis();
//        for (int i = 0; i < mOutByteValdues.length; ++i) {
//            mOutByteValdues[i] = (byte) (mInFloatValues[i] - 127);
//        }
//        MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:从图片中数据转换时间", "快速风格迁移读取数据时间", System.currentTimeMillis() - initTime);

//        initTime = System.currentTimeMillis();
//        Size outSize = new Size(mInWidth, mInHeight);
//        Mat outMat = new Mat(outSize, CV_32F);
//
////        for (int i = 0; i < 600; i++) {
////            for (int j = 0; j < 800; j++) {
////                inMat.put(i, j, mInByteValuess[i][j]);
////            }
////        }
//        outMat.put(0, 0, mInFloatValues);
//
//        Mat middleMat2 = new Mat(inSize, CV_8U);
//        outMat.convertTo(middleMat2, CV_8U);
//        MyLog.d(TAG, "BaseRapidStyleMigrationAIImageFetch", "状态:输出图片时间", "快速风格迁移输出图片时间", System.currentTimeMillis() - initTime);
//        Imgcodecs.imwrite(AI_PROCCESSED_IMAGE, inMat);
//        return middleMat2;
        return outMat;
    }
}
