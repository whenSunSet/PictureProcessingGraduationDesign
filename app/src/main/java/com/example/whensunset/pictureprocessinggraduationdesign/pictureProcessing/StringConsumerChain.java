package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing;

import android.text.TextUtils;

import com.example.whensunset.pictureprocessinggraduationdesign.impl.ConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_COLOR;

/**
 * Created by whensunset on 2018/3/8.
 */

public class StringConsumerChain extends ConsumerChain<String> {
    public static final String TAG = "何时夕:StringConsumerChain";

    private static StringConsumerChain INSTANCE;

    public static StringConsumerChain getInstance() {
        if (INSTANCE == null) {
            synchronized (StringConsumerChain.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StringConsumerChain();
                }
            }
        }
        return INSTANCE;
    }

    private StringConsumerChain() {
    }

    @Override
    protected Mat getStartResult(String path) {
        MyLog.d(TAG, "getStartResult", "path:", path);

        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("需要处理的图片路径为空");
        }

        File imageFile = new File(path);
        if (!imageFile.exists()) {
            throw new RuntimeException("需要处理的图片不存在");
        }

        int[] imageInfo = MyUtil.getImageWidthHeight(path);
        Mat firstMat = Imgcodecs.imread(path , IMREAD_COLOR);

        if (firstMat == null) {
            throw new RuntimeException("初始化 Chain 图片失败，图片Mat为null");
        }

        Rect rect = new Rect(0 , 0 , imageInfo[0] , imageInfo[1]);
        BaseMyConsumer firstCutMyConsumer = new CutMyConsumer(rect) {
            @Override
            protected Mat onNewResultImpl(Mat oldResult) {
                return firstMat;
            }
        };
        addConsumer(firstCutMyConsumer);

        MyLog.d(TAG, "getStartResult", "firstMat:rect" , firstMat , rect);
        return firstMat;
    }

}
