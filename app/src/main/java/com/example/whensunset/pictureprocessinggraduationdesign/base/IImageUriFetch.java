package com.example.whensunset.pictureprocessinggraduationdesign.base;

import java.util.List;

/**
 * Created by whensunset on 2018/3/2.
 */

public interface IImageUriFetch {
    List<String> getAllImageUriList();
    List<String> getRangeImageUriList(int start , int end);
    List<String> getALlImageUriListFromTag(Object tag);
    List<String> getRangeImageUriListFromTag(Object tag , int start , int end);
    List<Object> getAllTag();
    void freshImageInfo();
}
