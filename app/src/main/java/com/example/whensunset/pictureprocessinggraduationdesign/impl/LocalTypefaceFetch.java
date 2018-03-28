package com.example.whensunset.pictureprocessinggraduationdesign.impl;

import android.graphics.Typeface;

import com.example.whensunset.pictureprocessinggraduationdesign.PictureProcessingApplication;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ITypefaceFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by whensunset on 2018/3/25.
 */

public class LocalTypefaceFetch implements ITypefaceFetch {
    public static final String TAG = "何时夕:LocalTypefaceFetch";

    private static LocalTypefaceFetch mLocalTypefaceFetch;
    private static final Map<String , Typeface> mTypeface = new HashMap<>();

    public static final List<String> mTypefaceName = new ArrayList<>();
    static {
        mTypefaceName.add("光棍体");
        mTypefaceName.add("甜妞体");
    }

    public static LocalTypefaceFetch getInstance() {
        if (mLocalTypefaceFetch == null) {
            synchronized (SystemImageUriFetch.class) {
                if (mLocalTypefaceFetch == null) {
                    mLocalTypefaceFetch = new LocalTypefaceFetch();
                    ITypefaceFetch.addITypefaceFetch(mLocalTypefaceFetch);
                }
            }
        }
        return mLocalTypefaceFetch;
    }

    private LocalTypefaceFetch() {

    }

    @Override
    public List<Typeface> getAllTypeface() {
        if (mTypefaceName.size() != mTypeface.size()) {
            for (String typefaceName:mTypefaceName) {
                getTypeface(typefaceName);
            }
        }
        MyLog.d(TAG, "getAllTypeface", "状态:mTypefaceName:mTypeface:", mTypefaceName , mTypeface);
        return new ArrayList<>(mTypeface.values());
    }

    @Override
    public List<String> getAllTypefaceName() {
        return mTypefaceName;
    }

    @Override
    public Typeface getTypeface(String typefaceName) {
        MyLog.d(TAG, "getTypefaceFromSystem", "状态:typefaceName:", "" , typefaceName);
        Typeface typeface = mTypeface.get(typefaceName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(PictureProcessingApplication.getAppContext().getAssets(), "fonts/" + typefaceName + ".ttf");
            mTypeface.put(typefaceName , typeface);
        }
        return typeface;
    }
}
