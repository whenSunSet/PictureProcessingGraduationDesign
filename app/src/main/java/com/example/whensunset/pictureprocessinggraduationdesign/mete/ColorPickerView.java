package com.example.whensunset.pictureprocessinggraduationdesign.mete;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

/**
 * Created by whensunset on 2018/3/26.
 */

public class ColorPickerView extends AppCompatSeekBar {
    public static String TAG = "何时夕:ColorPickerView";
    private boolean isWB = false;

    public ColorPickerView(Context context) {
        super(context);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    Paint mPaint = new Paint();
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        int realWidth = getMeasuredWidth() - paddingEnd - paddingStart;
        int height = getMeasuredHeight();
        int tinyWidth = (realWidth / 256) + 1;
        int remainderWidth = realWidth % 256;

        int color = isWB ? changeColorFromHexToRGBWB(0) : changeColorFromHexToRGB(0);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        @SuppressLint("DrawAllocation") Rect rect = new Rect(0 , 0 , paddingStart , height);
        canvas.drawRect(rect , mPaint);
        rect.left = paddingStart;
        rect.left = paddingStart + tinyWidth;

        for (int i = 1; i <= 256; i++) {
            color = isWB ? changeColorFromHexToRGBWB(i) : changeColorFromHexToRGB(i);
            canvas.drawRect(rect , mPaint);
            rect.left = rect.right;
            rect.right = rect.left + tinyWidth;
            mPaint.setColor(color);

            if (i == remainderWidth) {
                tinyWidth--;
            }
        }

        rect.right = rect.right + paddingEnd;
        canvas.drawRect(rect , mPaint);

        super.onDraw(canvas);
    }

    public boolean isWB() {
        return isWB;
    }

    public void setWB(boolean WB) {
        isWB = WB;
    }

    public static int changeColorFromHexToRGB(int i) {
        int color = 0x0;
        if (i <= 42) {
            if (i == 42) {
                color = Color.RED + 0xFF00;
            } else {
                color = Color.RED + 0x0600 * i;
            }
        } else if (42 < i  && i <= 86) {
            if (i == 86) {
                color = Color.YELLOW - 0xFF0000;
            } else {
                color = Color.YELLOW - 0x060000 * (i - 43);
            }
        } else if (86 < i  && i <= 129) {
            if (i == 129) {
                color = Color.GREEN + 0xFF;
            } else {
                color = Color.GREEN + 0x06 * (i - 87);
            }
        } else if (129 < i  && i <= 171) {
            if (i == 171) {
                color = Color.CYAN - 0xFF00;
            } else {
                color = Color.CYAN - 0x0600 * (i - 129);
            }
        } else if (171 < i  && i <= 213) {
            if (i == 213) {
                color = Color.BLUE + 0xFF0000;
            } else {
                color = Color.BLUE + 0x060000 * (i - 171);
            }
        } else if (213 < i  && i <= 256) {
            if (i == 256) {
                color = Color.MAGENTA - 0xFF;
            } else {
                color = Color.MAGENTA - 0x06 * (i - 213);
            }
        }
        return color;
    }

    public static int changeColorFromHexToRGBWB(int i) {
        return 0xFF000000 + 0x00010000 * i + 0x00000100 * i + i;
    }
}
