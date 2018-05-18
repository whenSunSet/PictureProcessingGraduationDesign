package com.example.whensunset.pictureprocessinggraduationdesign;

import android.app.Application;
import android.content.Context;

import com.example.whensunset.pictureprocessinggraduationdesign.base.ITypefaceFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.FilterAction;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.AIFilterAction.FEATHERS_IMAGE;
import static com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.AIFilterAction.FEATHERS_LOW_ASSET_IMAGE_NAME;
import static com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.AIFilterAction.SCREAM_IMAGE;
import static com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.AIFilterAction.SCREAM_LOW_ASSET_IMAGE_NAME;
import static com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.AIFilterAction.STARRY_IMAGE;
import static com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.AIFilterAction.STARRY_LOW_ASSET_IMAGE_NAME;
import static com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.AIFilterAction.SUMIAO_IMAGE;
import static com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.AIFilterAction.SUMIAO_LOW_ASSET_IMAGE_NAME;
import static com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.AIFilterAction.WAVE_IMAGE;
import static com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.AIFilterAction.WAVE_LOW_ASSET_IMAGE_NAME;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.LOADING_ASSET_GIF_NAME;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.LOADING_GIF;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.MY_PHOTO_SHOP_DIRECTORY;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.MY_SHARE_DIRECTORY;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.PICTURE_FILTER_SAMPLE_ASSET_IMAGE_NAME;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.PICTURE_FILTER_SAMPLE_IMAGE;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.PICTURE_FRAME_ADD_ASSET_IMAGE_NAME;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.PICTURE_FRAME_ADD_IMAGE;

/**
 * Created by whensunset on 2018/3/2.
 */
public class PictureProcessingApplication extends Application {
    public static final String TAG = "何时夕:PictureProcessingApplication";

    private static Context appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this , config);

        ITypefaceFetch.init();
        FilterAction.init();
        init();

        File file = new File(MY_SHARE_DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }

        File file1 = new File(MY_PHOTO_SHOP_DIRECTORY);
        if (!file1.exists()) {
            file1.mkdir();
        }

    }
    public static Context getAppContext() {
        return appContext;
    }

    // 将资源变成文件形式
    public static Map<String , String> NeedToFileImageNameMap = new HashMap<>();

    void init() {
        NeedToFileImageNameMap.put(PICTURE_FILTER_SAMPLE_IMAGE , PICTURE_FILTER_SAMPLE_ASSET_IMAGE_NAME);
        NeedToFileImageNameMap.put(PICTURE_FRAME_ADD_IMAGE, PICTURE_FRAME_ADD_ASSET_IMAGE_NAME);

        NeedToFileImageNameMap.put(STARRY_IMAGE , STARRY_LOW_ASSET_IMAGE_NAME);
        NeedToFileImageNameMap.put(FEATHERS_IMAGE , FEATHERS_LOW_ASSET_IMAGE_NAME);
        NeedToFileImageNameMap.put(WAVE_IMAGE , WAVE_LOW_ASSET_IMAGE_NAME);
        NeedToFileImageNameMap.put(SCREAM_IMAGE , SCREAM_LOW_ASSET_IMAGE_NAME);
        NeedToFileImageNameMap.put(SUMIAO_IMAGE , SUMIAO_LOW_ASSET_IMAGE_NAME);
        NeedToFileImageNameMap.put(LOADING_GIF, LOADING_ASSET_GIF_NAME);

        for (String filePath : NeedToFileImageNameMap.keySet()) {
            File pictureFilterSampleImage = new File(filePath);
            if (!pictureFilterSampleImage.exists()) {
                MyUtil.assetToFile(NeedToFileImageNameMap.get(filePath) ,filePath);
            }
        }
    }
}
