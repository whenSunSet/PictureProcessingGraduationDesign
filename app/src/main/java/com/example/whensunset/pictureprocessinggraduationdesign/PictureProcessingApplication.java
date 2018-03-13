package com.example.whensunset.pictureprocessinggraduationdesign;

import android.app.Application;
import android.content.Context;

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
    }
    public static Context getAppContext() {
        return appContext;
    }
}
