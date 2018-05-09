package com.example.whensunset.pictureprocessinggraduationdesign.staticParam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.whensunset.pictureprocessinggraduationdesign.PictureProcessingApplication;
import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
    String IP =  "http://192.168.198.210:8000/";



    // 将资源变成文件形式
    Map<String , Integer> NeedToFileImageNameMap = new HashMap<>();
    String PICTURE_FILTER_SAMPLE_IMAGE = CACHE_DIRECTORY + "/picture_filter_sample_image.jpg";
    String PICTURE_FRAME_ADD = CACHE_DIRECTORY + "/picture_frame_add.png";

    static void init() {
        NeedToFileImageNameMap.put(PICTURE_FILTER_SAMPLE_IMAGE , R.drawable.picture_filter_sample_image);
        NeedToFileImageNameMap.put(PICTURE_FRAME_ADD , R.drawable.picture_frame_add);

        for (String fileName : NeedToFileImageNameMap.keySet()) {
            File pictureFilterSampleImage = new File(fileName);
            if (!pictureFilterSampleImage.exists()) {
                Bitmap bitmap = BitmapFactory.decodeResource(PictureProcessingApplication.getAppContext().getResources() , NeedToFileImageNameMap.get(fileName));
                MyUtil.saveBitmap(bitmap , fileName);
            }
        }



    }
}
