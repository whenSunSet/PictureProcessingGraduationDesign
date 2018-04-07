package com.example.whensunset.pictureprocessinggraduationdesign.base;

import android.graphics.Typeface;

import com.example.whensunset.pictureprocessinggraduationdesign.impl.LocalTypefaceFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.SystemTypefaceFetch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by whensunset on 2018/3/25.
 */

public interface ITypefaceFetch {
    List<ITypefaceFetch> mITypefaceFetch = new ArrayList<>();

    List<Typeface> getAllTypeface();
    List<String> getAllTypefaceName();
    Typeface getTypeface(String typefaceName);


    static void init() {
        SystemTypefaceFetch.getInstance();
        LocalTypefaceFetch.getInstance();
    }

    static Typeface getTypefaceFromAll(String typefaceName) {
        Typeface typeface;
        for (ITypefaceFetch iTypefaceFetch: mITypefaceFetch) {
            typeface = iTypefaceFetch.getTypeface(typefaceName);
            if (typeface != null) {
                return typeface;
            }
        }
        throw new RuntimeException("没有这种字体");
    }

    static List<Typeface> getAllTypefaceFromAll() {
        List<Typeface> typefaceList = new ArrayList<>();
        for (ITypefaceFetch iTypefaceFetch: mITypefaceFetch) {
            typefaceList.addAll(iTypefaceFetch.getAllTypeface());
        }
        return typefaceList;
    }

    static List<String> getAllTypefaceNameFromAll() {
        List<String> typefaceNameList = new ArrayList<>();
        for (ITypefaceFetch iTypefaceFetch: mITypefaceFetch) {
            typefaceNameList.addAll(iTypefaceFetch.getAllTypefaceName());
        }
        return typefaceNameList;
    }

    static void addITypefaceFetch(ITypefaceFetch iTypefaceFetch) {
        mITypefaceFetch.add(iTypefaceFetch);
    }
}
