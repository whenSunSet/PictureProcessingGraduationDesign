package com.example.whensunset.pictureprocessinggraduationdesign;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.whensunset.pictureprocessinggraduationdesign.base.ITypefaceFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.http.FileService;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.FilterAction;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.IP;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.MY_PHOTO_SHOP_DIRECTORY;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.MY_SHARE_DIRECTORY;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.PICTURE_FILTER_SAMPLE_IMAGE;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.PICTURE_FRAME_ADD;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.STARRY_NIGHT_IMAGE;

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
        init();

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

    // 将资源变成文件形式
    public static Map<String , Integer> NeedToFileImageNameMap = new HashMap<>();

    void init() {
        NeedToFileImageNameMap.put(PICTURE_FILTER_SAMPLE_IMAGE , R.drawable.picture_filter_sample_image);
        NeedToFileImageNameMap.put(PICTURE_FRAME_ADD , R.drawable.picture_frame_add);
        NeedToFileImageNameMap.put(STARRY_NIGHT_IMAGE , R.drawable.starry_night);

        for (String fileName : NeedToFileImageNameMap.keySet()) {
            File pictureFilterSampleImage = new File(fileName);
            if (!pictureFilterSampleImage.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeResource(PictureProcessingApplication.getAppContext().getResources() , NeedToFileImageNameMap.get(fileName), options);

                options.inSampleSize = options.outWidth / 100;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inJustDecodeBounds = false;
                Bitmap bmp = BitmapFactory.decodeResource(PictureProcessingApplication.getAppContext().getResources() , NeedToFileImageNameMap.get(fileName), options);
                MyUtil.saveBitmap(bmp , fileName);
            }
        }
    }
}
