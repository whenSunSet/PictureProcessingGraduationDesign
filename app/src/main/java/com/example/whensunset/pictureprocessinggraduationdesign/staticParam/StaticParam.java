package com.example.whensunset.pictureprocessinggraduationdesign.staticParam;

import com.example.whensunset.pictureprocessinggraduationdesign.PictureProcessingApplication;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyUtil;

/**
 * Created by whensunset on 2018/3/18.
 */

public interface StaticParam {
    String FONT_EDIT_VIEW_IMAGE = MyUtil.getCacheDirectory(PictureProcessingApplication.getAppContext(), "").getPath() + "/picture_text_font_edit_view.png";
    String PICTURE_FILTER_SAMPLE_IMAGE = MyUtil.getCacheDirectory(PictureProcessingApplication.getAppContext(), "").getPath() + "/picture_filter_sample_image.jpg";
    String PICTURE_FRAME_ADD = MyUtil.getCacheDirectory(PictureProcessingApplication.getAppContext(), "").getPath() + "/picture_frame_add.png";
}
