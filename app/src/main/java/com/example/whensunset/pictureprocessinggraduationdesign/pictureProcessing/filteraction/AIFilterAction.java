package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction;

import android.net.Uri;

import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseRapidStyleMigrationAIImageFetch;

import org.opencv.core.Mat;

import java.io.File;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.CACHE_DIRECTORY;

/**
 * Created by whensunset on 2018/3/28.
 */

public class AIFilterAction implements FilterAction {
    public static final String SCREAM = "呐喊";
    public static final String FEATHERS = "羽毛";
    public static final String STARRY = "星夜";
    public static final String WAVE = "浮世绘";
    public static final String SUMIAO = "素描";

    public static final String FEATHERS_LOW_ASSET_PB = "file:///android_asset/feathers.pb";
    public static final String SCREAM_LOW_ASSET_PB = "file:///android_asset/scream.pb";
    public static final String STARRY_LOW_ASSET_PB = "file:///android_asset/starry.pb";
    public static final String WAVE_LOW_ASSET_PB = "file:///android_asset/wave.pb";
    public static final String SUMIAO_LOW_ASSET_PB = "file:///android_asset/sumiao.pb";

    public static final String FEATHERS_LOW_ASSET_IMAGE_NAME = "feathers_low.jpg";
    public static final String SCREAM_LOW_ASSET_IMAGE_NAME = "scream_low.jpg";
    public static final String STARRY_LOW_ASSET_IMAGE_NAME = "starry_low.jpg";
    public static final String WAVE_LOW_ASSET_IMAGE_NAME = "wave_low.jpg";
    public static final String SUMIAO_LOW_ASSET_IMAGE_NAME = "sumiao_low.jpg";

    public static final String STARRY_IMAGE = CACHE_DIRECTORY + "/starry_image.jpg";
    public static final String FEATHERS_IMAGE = CACHE_DIRECTORY + "/feathers_image.jpg";
    public static final String WAVE_IMAGE = CACHE_DIRECTORY + "/wave_image.jpg";
    public static final String SCREAM_IMAGE = CACHE_DIRECTORY + "/scream_image.jpg";
    public static final String SUMIAO_IMAGE = CACHE_DIRECTORY + "/sumiao_image.jpg";

    private static AIFilterAction INSTANCE = new AIFilterAction();
    private String name;
    private String imageUri;
    private BaseRapidStyleMigrationAIImageFetch baseRapidStyleMigrationAIImageFetch;

    private AIFilterAction() {
    }

    public AIFilterAction(String name) {
        this.name = name;

        switch (name) {
            case SCREAM:
                imageUri = Uri.fromFile(new File(SCREAM_IMAGE)).toString();
                baseRapidStyleMigrationAIImageFetch = new BaseRapidStyleMigrationAIImageFetch(SCREAM_LOW_ASSET_PB);
                break;
            case FEATHERS:
                imageUri = Uri.fromFile(new File(FEATHERS_IMAGE)).toString();
                baseRapidStyleMigrationAIImageFetch = new BaseRapidStyleMigrationAIImageFetch(FEATHERS_LOW_ASSET_PB);
                break;
            case STARRY:
                imageUri = Uri.fromFile(new File(STARRY_IMAGE)).toString();
                baseRapidStyleMigrationAIImageFetch = new BaseRapidStyleMigrationAIImageFetch(STARRY_LOW_ASSET_PB);
                break;
            case WAVE:
                imageUri = Uri.fromFile(new File(WAVE_IMAGE)).toString();
                baseRapidStyleMigrationAIImageFetch = new BaseRapidStyleMigrationAIImageFetch(WAVE_LOW_ASSET_PB);
                break;
            case SUMIAO:
                imageUri = Uri.fromFile(new File(SUMIAO_IMAGE)).toString();
                baseRapidStyleMigrationAIImageFetch = new BaseRapidStyleMigrationAIImageFetch(SUMIAO_LOW_ASSET_PB);
                break;
        }
    }

    public static AIFilterAction getInstance() {
        return INSTANCE;
    }

    @Override
    public void filter(Mat oldMat, Mat newMat) {
        baseRapidStyleMigrationAIImageFetch.run(oldMat).assignTo(newMat);
    }

    @Override
    public String getFilterName() {
        return name;
    }

    public String getImageUri() {
        return imageUri;
    }
}
