package com.example.whensunset.pictureprocessinggraduationdesign.staticParam;

import android.os.Environment;

import com.example.whensunset.pictureprocessinggraduationdesign.PictureProcessingApplication;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyUtil;

/**
 * Created by whensunset on 2018/3/18.
 */

public interface StaticParam {
    String CACHE_DIRECTORY = MyUtil.getCacheDirectory(PictureProcessingApplication.getAppContext(), "").getPath();
    String MY_SHARE_DIRECTORY = CACHE_DIRECTORY + "/share";
    String MY_PHOTO_SHOP_DIRECTORY = Environment.getExternalStorageDirectory() + "/MyPhotoShopDirectory";

    String FONT_EDIT_VIEW_IMAGE =  CACHE_DIRECTORY + "/picture_text_font_edit_view.png";
    String UPLOAD_IMAGE =  CACHE_DIRECTORY + "/upload_image.jpg";
    String DOWNLOAD_IMAGE =  CACHE_DIRECTORY + "/download_image.jpg";
    String SHARE_IMAGE =  MY_SHARE_DIRECTORY + "/share_image.jpg";
    String AI_PRE_PROCCESSING_IMAGE =  MY_SHARE_DIRECTORY + "/ai_pre_processing_image.jpg";
    String AI_PROCCESSED_IMAGE =  MY_SHARE_DIRECTORY + "/ai_processed_image.jpg";
    String IP =  "http://192.168.198.210:8000/";

    String PICTURE_FILTER_SAMPLE_ASSET_IMAGE_NAME = "picture_filter_sample_image.jpg";
    String PICTURE_FRAME_ADD_ASSET_IMAGE_NAME = "picture_frame_add.png";
    String LOADING_ASSET_GIF_NAME = "loading.gif";

    String PICTURE_FILTER_SAMPLE_IMAGE = CACHE_DIRECTORY + "/picture_filter_sample_image.jpg";
    String PICTURE_FRAME_ADD_IMAGE = CACHE_DIRECTORY + "/picture_frame_add.png";
    String LOADING_GIF = CACHE_DIRECTORY + "/loading.gif";

}
