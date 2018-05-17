package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction;

import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseRapidStyleMigrationAIImageFetch;

import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/28.
 */

public class AIFilterAction implements FilterAction {
    private static AIFilterAction INSTANCE = new AIFilterAction();
    private static final String NAME = "星夜";

    private AIFilterAction() {
    }

    public static AIFilterAction getInstance() {
        return INSTANCE;
    }

    @Override
    public void filter(Mat oldMat, Mat newMat) {
//        File file = new File(AI_PRE_PROCCESSING_IMAGE);
//        if (file.exists()) {
//            file.delete();
//        }
//        Imgcodecs.imwrite(AI_PRE_PROCCESSING_IMAGE , oldMat);
//        FileService fileService = PictureProcessingApplication.getRetrofit().create(FileService.class);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "test.jpg" , requestFile);
//        Call<ResponseBody> call = fileService.upload(body);
//        try {
//            Response response = call.execute();
//        } catch (IOException e) {
//            throw new RuntimeException("图片处理失败");
//        }
//
//        try {
//            Response<ResponseBody> response = fileService.download(IP + "show").execute();
//            MyUtil.writeResponseBodyToDisk(response.body());
//        } catch (IOException e) {
//            throw new RuntimeException("图片处理失败");
//        }
        BaseRapidStyleMigrationAIImageFetch baseRapidStyleMigrationAIImageFetch = new BaseRapidStyleMigrationAIImageFetch();

        baseRapidStyleMigrationAIImageFetch.run(oldMat).assignTo(newMat);
    }

    @Override
    public String getFilterName() {
        return NAME;
    }



}
