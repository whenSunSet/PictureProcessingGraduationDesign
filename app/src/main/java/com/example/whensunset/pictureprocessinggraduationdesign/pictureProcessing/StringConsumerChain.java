package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing;

import android.text.TextUtils;
import android.util.Log;

import com.example.whensunset.pictureprocessinggraduationdesign.base.ConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil.BindingUtils;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_COLOR;

/**
 * Created by whensunset on 2018/3/8.
 */

public class StringConsumerChain extends ConsumerChain<String> {
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
        if (TextUtils.isEmpty(path)) {
            Log.d("何时夕:StringConsumerChain", ("需要处理的图片路径为null startParam：" + path));
            return null;
        }

        File imageFile = new File(path);
        if (!imageFile.exists()) {
            Log.d("何时夕:StringConsumerChain", ("需要处理的图片不存在 startParam：" + path));
            return null;
        }
        int[] imageInfo = BindingUtils.getImageWidthHeight(path);
        mFirstCutMyConsumer.setRect(new Rect(0 , 0 , imageInfo[0] , imageInfo[1]));
        return Imgcodecs.imread(path , IMREAD_COLOR);
    }

}
