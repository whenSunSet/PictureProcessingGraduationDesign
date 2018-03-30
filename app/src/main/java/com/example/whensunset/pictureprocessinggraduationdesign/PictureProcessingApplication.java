package com.example.whensunset.pictureprocessinggraduationdesign;

import android.app.Application;
import android.content.Context;

import com.example.whensunset.pictureprocessinggraduationdesign.impl.LocalTypefaceFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.SystemTypefaceFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.CarvingFilterAction;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.NormalFilterAction;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.NostalgiaFilterAction;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.ReliefFilterAction;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

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

        NormalFilterAction.getInstance();
        CarvingFilterAction.getInstance();
        ReliefFilterAction.getInstance();
        NostalgiaFilterAction.getInstance();
    }
    public static Context getAppContext() {
        return appContext;
    }
}
