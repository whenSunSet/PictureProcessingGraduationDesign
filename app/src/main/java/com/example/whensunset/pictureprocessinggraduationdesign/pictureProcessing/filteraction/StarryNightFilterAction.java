package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction;

import com.example.whensunset.pictureprocessinggraduationdesign.PictureProcessingApplication;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.http.FileService;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.DOWNLOAD_IMAGE;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.IP;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.UPLOAD_IMAGE;
import static org.opencv.imgcodecs.Imgcodecs.IMREAD_COLOR;

/**
 * Created by whensunset on 2018/3/28.
 */

public class StarryNightFilterAction implements FilterAction {
    private static StarryNightFilterAction INSTANCE = new StarryNightFilterAction();
    private static final String NAME = "星夜";

    private StarryNightFilterAction() {
    }

    public static StarryNightFilterAction getInstance() {
        return INSTANCE;
    }

    @Override
    public void filter(Mat oldMat, Mat newMat) {
        File file = new File(UPLOAD_IMAGE);
        if (file.exists()) {
            file.delete();
        }
        Imgcodecs.imwrite(UPLOAD_IMAGE , oldMat);
        FileService fileService = PictureProcessingApplication.getRetrofit().create(FileService.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "test.jpg" , requestFile);
        Call<ResponseBody> call = fileService.upload(body);
        try {
            Response response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException("图片处理失败");
        }

        try {
            Response<ResponseBody> response = fileService.download(IP + "show").execute();
            MyUtil.writeResponseBodyToDisk(response.body());
        } catch (IOException e) {
            throw new RuntimeException("图片处理失败");
        }

        Imgcodecs.imread(DOWNLOAD_IMAGE , IMREAD_COLOR).assignTo(newMat);
    }

    @Override
    public String getFilterName() {
        return NAME;
    }

}
