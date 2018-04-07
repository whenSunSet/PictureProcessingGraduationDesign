package com.example.whensunset.pictureprocessinggraduationdesign.impl;

import android.graphics.Typeface;

import com.example.whensunset.pictureprocessinggraduationdesign.base.ITypefaceFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by whensunset on 2018/3/25.
 */

public class SystemTypefaceFetch implements ITypefaceFetch {
    public static final String TAG = "何时夕:SystemTypefaceFetch";

    private static SystemTypefaceFetch mSystemTypefaceFetch;
    private static final Map<String , Typeface> mTypeface = new HashMap<>();
    public static final List<String> mTypefaceName = new ArrayList<>();
    static {
        mTypefaceName.add("默认");
        mTypefaceName.add("粗体");
    }

    public static SystemTypefaceFetch getInstance() {
        if (mSystemTypefaceFetch == null) {
            synchronized (SystemImageUriFetch.class) {
                if (mSystemTypefaceFetch == null) {
                    mSystemTypefaceFetch = new SystemTypefaceFetch();
                    ITypefaceFetch.addITypefaceFetch(mSystemTypefaceFetch);
                }
            }
        }
        return mSystemTypefaceFetch;
    }

    private SystemTypefaceFetch() {

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
        MyLog.d(TAG, "getTypefaceFromSystem", "状态:typefaceName:", typefaceName);
        Typeface typeface = mTypeface.get(typefaceName);
        if (typeface == null) {
            if (typefaceName.equals("默认")) {
                typeface = Typeface.DEFAULT;
            } else if (typefaceName.equals("粗体")) {
                typeface = Typeface.DEFAULT_BOLD;
            }
            mTypeface.put(typefaceName , typeface);
        }
        return typeface;
    }

}
