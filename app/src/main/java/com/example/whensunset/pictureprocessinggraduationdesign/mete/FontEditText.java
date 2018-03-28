package com.example.whensunset.pictureprocessinggraduationdesign.mete;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

/**
 * Created by whensunset on 2018/3/25.
 */

public class FontEditText extends android.support.v7.widget.AppCompatEditText {
    public static String TAG = "何时夕:MyEditText";

    public FontEditText(Context context) {
        super(context);
    }

    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Bitmap getCacheBitmapFromView() {
        final boolean drawingCacheEnabled = true;
        this.setDrawingCacheEnabled(drawingCacheEnabled);
        this.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = this.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            this.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }
}
