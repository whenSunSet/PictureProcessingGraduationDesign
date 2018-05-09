package com.example.whensunset.pictureprocessinggraduationdesign;

import android.app.Application;
import android.content.Context;

import com.example.whensunset.pictureprocessinggraduationdesign.base.ITypefaceFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.http.FileService;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.FilterAction;
import com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.IP;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.MY_PHOTO_SHOP_DIRECTORY;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.MY_SHARE_DIRECTORY;

/**
 * Created by whensunset on 2018/3/2.
 */
public class PictureProcessingApplication extends Application {
    public static final String TAG = "何时夕:PictureProcessingApplication";
    private static Retrofit retrofit;
    private static FileService fileService;

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
        StaticParam.init();

        File file = new File(MY_SHARE_DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }

        File file1 = new File(MY_PHOTO_SHOP_DIRECTORY);
        if (!file1.exists()) {
            file1.mkdir();
        }

        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(60, TimeUnit.SECONDS).
                readTimeout(60, TimeUnit.SECONDS).
                writeTimeout(60, TimeUnit.SECONDS).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(IP)
                .client(client)
                .build();
        fileService = PictureProcessingApplication.getRetrofit().create(FileService.class);
    }
    public static Context getAppContext() {
        return appContext;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static FileService getFileService() {
        return fileService;
    }
}
