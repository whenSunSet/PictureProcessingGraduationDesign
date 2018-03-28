package com.example.whensunset.pictureprocessinggraduationdesign.mete;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;

/**
 * Created by whensunset on 2018/3/25.
 */

public class MoveFrameLayout extends FrameLayout {
    public static String TAG = "何时夕:MoveFrameLayout";
    private static final int STROKE_WIDTH = 4;
    private OnPlaceChangedListener mOnPlaceChangedListener;

    public MoveFrameLayout(@NonNull Context context) {
        super(context);
    }

    public MoveFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MoveFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;

        if (action == MotionEvent.ACTION_DOWN) {
            mLastMovePointF.set(event.getRawX(), event.getRawY());
            return super.onTouchEvent(event);
        } else if (action == MotionEvent.ACTION_UP) {
            mLastMovePointF.set(0, 0);
            return super.onTouchEvent(event);
        } else if (action == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return false;
    }

    int mDetaX = 0;
    int mDetaY = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;

        int detaX = (int) (event.getRawX() - mLastMovePointF.x);
        int detaY = (int) (event.getRawY() - mLastMovePointF.y);

        if (action == MotionEvent.ACTION_MOVE) {
            mDetaX = mDetaX + detaX;
            mDetaY = mDetaY + detaY;
            offsetLeftAndRight(detaX);
            offsetTopAndBottom(detaY);

        }
        MyLog.d(TAG, "onTouchEvent", "状态:detaX:detaY:event.getX():event.getY():mLastMovePointF.x:mLastMovePointF.y",
                "进入移动状态aaa" , detaX , detaY , event.getRawX() , event.getRawY() , mLastMovePointF.x , mLastMovePointF.y);

        mLastMovePointF.set(event.getRawX(), event.getRawY());
        postInvalidate();

        return true;
    }

    private PointF mLastMovePointF = new PointF();

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        offsetLeftAndRight(mDetaX);
        offsetTopAndBottom(mDetaY);
        MyLog.d(TAG, "onLayout", "状态:left:top:right:bottom:", "aaa" , left , top , right , bottom);
    }

    Paint mPaint = new Paint();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mOnPlaceChangedListener != null) {
            mOnPlaceChangedListener.onPlaceChanged(getRect());
        }

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        mPaint.setStyle(Paint.Style.STROKE);

        @SuppressLint("DrawAllocation") Rect rect = new Rect(0 , 0 , width ,  height);
        canvas.drawRect(rect , mPaint);

        MyLog.d(TAG, "drawCutMask", "状态:width:height:rect:", "" , width , height , rect);
    }

    private Rect getRect() {
        return new Rect(getLeft() , getTop() , getRight() , getBottom());
    }

    public void setOnPlaceChangedListener(OnPlaceChangedListener onPlaceChangedListener) {
        mOnPlaceChangedListener = onPlaceChangedListener;
    }

    public interface OnPlaceChangedListener{
        void onPlaceChanged(Rect rect);
    }
}
