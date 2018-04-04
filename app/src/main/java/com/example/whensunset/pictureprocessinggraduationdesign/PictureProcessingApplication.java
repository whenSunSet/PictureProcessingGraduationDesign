package com.example.whensunset.pictureprocessinggraduationdesign;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.LocalTypefaceFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.SystemTypefaceFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.CarvingFilterAction;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.NostalgiaFilterAction;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.ReliefFilterAction;
import com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

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
        SystemTypefaceFetch.getInstance();
        LocalTypefaceFetch.getInstance();

        CarvingFilterAction.getInstance();
        ReliefFilterAction.getInstance();
        NostalgiaFilterAction.getInstance();

        File pictureFilterSampleImage = new File(StaticParam.PICTURE_FILTER_SAMPLE_IMAGE);
        if (!pictureFilterSampleImage.exists()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources() , R.drawable.picture_filter_sample_image);
            MyUtil.saveBitmap(bitmap , StaticParam.PICTURE_FILTER_SAMPLE_IMAGE);
        }

        File pictureFrameAddImage = new File(StaticParam.PICTURE_FRAME_ADD);
        if (!pictureFrameAddImage.exists()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources() , R.drawable.picture_frame_add);
            MyUtil.saveBitmap(bitmap , StaticParam.PICTURE_FRAME_ADD);
        }
    }
    public static Context getAppContext() {
        return appContext;
    }
}
