package com.example.whensunset.pictureprocessinggraduationdesign.mete;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.whensunset.pictureprocessinggraduationdesign.base.MyLog;

/**
 * Created by whensunset on 2018/3/11.
 */

public class CutView extends PinchImageView {
    public static String TAG = "何时夕:CutView";
    private static final int TOUCH_LINE_WIDTH = 80;
    private static final int MASK_ALPHA = 100;
    private static final int STROKE_WIDTH = 4;

    private OnLimitRectChangedListener mOnLimitRectChangedListener;
    private Rect mLimitRect = new Rect();
    private Rect mLimitMaxRect = new Rect();
    private PointF mLastMovePointF = new PointF();
    private Paint mPaint = new Paint();

    private int nowLine = -1;
    private boolean isInCut = false;
    private boolean isInResize = false;
    private boolean isInMove = false;
    private boolean isInit = true;

    public CutView(Context context) {
        super(context);
    }

    public CutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isOpenPinchImage) {
            return super.onTouchEvent(event);
        }
        super.onTouchEvent(event);

        int action = event.getAction() & MotionEvent.ACTION_MASK;
        MyLog.d(TAG, "onTouchEvent", "tt:mLastMovePointF:mLimitRect:mLimitMaxRect:", "gggg" , mLastMovePointF , mLimitRect , mLimitMaxRect);

        if (action == MotionEvent.ACTION_DOWN) {
            MyLog.d(TAG, "onTouchEvent", "状态gggg:触摸状态:", "进入触摸事件方法" , "DOWN");
            mLastMovePointF.set(event.getX(), event.getY());
        } else if (action == MotionEvent.ACTION_UP) {
            MyLog.d(TAG, "onTouchEvent", "状态gggg:触摸状态:", "进入触摸事件方法" , "UP");
            mLastMovePointF.set(0 , 0);
            isInResize = false;
            isInMove = false;
        }

        int x = (int) event.getX();
        int y = (int) event.getY();
        int detaX = (int) (event.getX() - mLastMovePointF.x);
        int detaY = (int) (event.getY() - mLastMovePointF.y);
        Rect nowLimitRect = new Rect(mLimitRect);

        if (action == MotionEvent.ACTION_MOVE) {

            if (isNearLine(x , y) && !isInMove) {

                if (nowLine == 1){
                    nowLimitRect.left = nowLimitRect.left + detaX;
                } else if (nowLine == 2) {
                    nowLimitRect.top = nowLimitRect.top + detaY;
                } else if (nowLine == 3) {
                    nowLimitRect.right = nowLimitRect.right + detaX;
                } else if (nowLine == 4) {
                    nowLimitRect.bottom = nowLimitRect.bottom + detaY;
                }

                if (mLimitMaxRect.contains(nowLimitRect)) {
                    mLimitRect.set(nowLimitRect);
                }

                isInResize = true;
                postInvalidate();

                MyLog.d(TAG, "onTouchEvent", "状态gggg:x:y:detaX:detaY:mLimitRect:nowLimitRect:mLimitMaxRect:nowLine:",
                        "进入伸缩边框" , x , y , detaX , detaY , mLimitRect , nowLimitRect , mLimitMaxRect , nowLine);
            } else if (!isNearLine(x , y) && mLimitRect.contains(x , y) && !isInResize) {
                nowLimitRect.left = nowLimitRect.left + detaX;
                nowLimitRect.right = nowLimitRect.right + detaX;
                if (mLimitMaxRect.left <= nowLimitRect.left && nowLimitRect.right <= mLimitMaxRect.right) {
                    mLimitRect.left = nowLimitRect.left;
                    mLimitRect.right = nowLimitRect.right;
                }

                nowLimitRect.top = nowLimitRect.top + detaY;
                nowLimitRect.bottom = nowLimitRect.bottom + detaY;
                if ((mLimitMaxRect.top <= nowLimitRect.top) && (nowLimitRect.bottom <= mLimitMaxRect.bottom)) {
                    mLimitRect.top = nowLimitRect.top;
                    mLimitRect.bottom = nowLimitRect.bottom;
                }

                isInMove = true;
                postInvalidate();

                MyLog.d(TAG, "onTouchEvent", "状态gggg:x:y:detaX:detaY:mLimitRect:nowLimitRect:mLimitMaxRect:",
                        "进入移动边框" , x , y , detaX , detaY , mLimitRect , nowLimitRect , mLimitMaxRect);
            }

            mLastMovePointF.set(event.getX(), event.getY());
            if (mOnLimitRectChangedListener != null) mOnLimitRectChangedListener.onLimitRectChanged(getOpencvCutRect());

        }

        return true;
    }

    private boolean isNearLine(int x, int y) {
        Rect rectLeft = new Rect(mLimitRect.left - TOUCH_LINE_WIDTH , mLimitRect.top , mLimitRect.left + TOUCH_LINE_WIDTH , mLimitRect.bottom);
        Rect rectTop = new Rect(mLimitRect.left , mLimitRect.top - TOUCH_LINE_WIDTH , mLimitRect.right , mLimitRect.top + TOUCH_LINE_WIDTH);
        Rect rectRight = new Rect(mLimitRect.right - TOUCH_LINE_WIDTH , mLimitRect.top , mLimitRect.right + TOUCH_LINE_WIDTH , mLimitRect.bottom);
        Rect rectBottom = new Rect(mLimitRect.left , mLimitRect.bottom - TOUCH_LINE_WIDTH , mLimitRect.right , mLimitRect.bottom + TOUCH_LINE_WIDTH);

        MyLog.d(TAG, "isNearLine", "状态gggg:rectLeft:rectTop:rectRight:rectBottom",
                "进入判断是否靠近边框" , rectLeft , rectTop , rectRight , rectBottom);

        if (rectLeft.contains(x , y)) {
            nowLine = 1;
            return true;
        } else if (rectTop.contains(x , y)) {
            nowLine = 2;
            return true;
        } else if (rectRight.contains(x , y)) {
            nowLine = 3;
            return true;
        } else if (rectBottom.contains(x , y)) {
            nowLine = 4;
            return true;
        }


        MyLog.d(TAG, "isNearLine", "状态gggg:nowLine:", "判断是否靠近边框完毕" , nowLine);
        return false;
    }

    public void setInCut(boolean inCut) {
        isInCut = inCut;
    }

    public Rect getLimitRect() {
        return mLimitRect;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public void setOnLimitRectChangedListener(OnLimitRectChangedListener onLimitRectChangedListener) {
        mOnLimitRectChangedListener = onLimitRectChangedListener;
    }

    private org.opencv.core.Rect getOpencvCutRect() {
        Drawable imgDrawable = getDrawable();
        if (imgDrawable != null) {
            int dw = imgDrawable.getBounds().width();
            int dh = imgDrawable.getBounds().height();

            //获得ImageView中Image的变换矩阵
            Matrix m = getImageMatrix();
            float[] values = new float[10];
            m.getValues(values);

            //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数
            float sx = values[0];
            float sy = values[4];

            org.opencv.core.Rect rect = new org.opencv.core.Rect((int) (((float) mLimitRect.left - (float) mLimitMaxRect.left) / sx),
                    (int) (((float) mLimitRect.top - (float) mLimitMaxRect.top) / sy), (int)((float)mLimitRect.width() / sx) , (int)((float)mLimitRect.height() / sx));

            MyLog.d(TAG, "getOpencvCutRect", "状态gggg:dw:dh:m:sx:sy:rect", "返回opencv的Rect" , dw , dh , m , sx , sy , rect);
            return rect;
        } else {
            throw new RuntimeException("CutView还没初始化");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getImgDisplaySize();

        // 每次进入 transform 的时候需要重新初始化
        if (isInit) {
            mLimitRect.set(mLimitMaxRect.left , mLimitMaxRect.top , mLimitMaxRect.right , mLimitMaxRect.bottom);
            isInit = false;
            MyLog.d(TAG, "onDraw", "状态gggg:mLimitMaxRect", "重新初始化cutView" , mLimitMaxRect);
        }
        drawMask(canvas);
    }

    private void drawMask(Canvas canvas) {
        if (isInCut) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();

            mPaint.setColor(Color.BLACK);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAlpha(MASK_ALPHA);
            isOpenPinchImage = false;

            if (mLimitMaxRect.contains(mLimitRect)) {
                canvas.drawRect(0 , 0 , width , mLimitRect.top , mPaint);
                canvas.drawRect(0 , mLimitRect.bottom , width , height , mPaint);
                canvas.drawRect(0 , mLimitRect.top , mLimitRect.left , mLimitRect.bottom , mPaint);
                canvas.drawRect(mLimitRect.right , mLimitRect.top , width , mLimitRect.bottom , mPaint);
            } else {
                canvas.drawRect(0 , 0 , width , mLimitMaxRect.top , mPaint);
                canvas.drawRect(0 , mLimitMaxRect.bottom , width , height , mPaint);
                canvas.drawRect(0 , mLimitMaxRect.top , mLimitMaxRect.left , mLimitMaxRect.bottom , mPaint);
                canvas.drawRect(mLimitMaxRect.right , mLimitMaxRect.top , width , mLimitMaxRect.bottom , mPaint);
                mLimitRect.set(mLimitMaxRect);
            }


            mPaint.setColor(Color.WHITE);
            mPaint.setStrokeWidth(STROKE_WIDTH);
            mPaint.setStyle(Paint.Style.STROKE);

            if (mLimitMaxRect.contains(mLimitRect)) {
                canvas.drawRect(mLimitRect , mPaint);
            } else {
                canvas.drawRect(mLimitMaxRect , mPaint);
            }

        } else {
            setOpenPinchImage(true);
        }

        MyLog.d(TAG, "drawMask", "状态gggg:isInCut:mLimitRect:mLimitMaxRect:", "进入绘制蒙版" , isInCut , mLimitRect , mLimitMaxRect);
    }

    private void getImgDisplaySize() {
        Drawable imgDrawable = getDrawable();
        if (imgDrawable != null) {
            //获得ImageView中Image的真实宽高，
            int dw = imgDrawable.getBounds().width();
            int dh = imgDrawable.getBounds().height();

            //获得ImageView中Image的变换矩阵
            Matrix m = getImageMatrix();
            float[] values = new float[10];
            m.getValues(values);

            //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数
            float sx = values[0];
            float sy = values[4];

            //计算Image在屏幕上实际绘制的宽高
            int realImgShowWidth = (int) (dw * sx);
            int realImgShowHeight = (int) (dh * sy);

            mLimitMaxRect.top =  (getMeasuredHeight() - realImgShowHeight) / 2;
            mLimitMaxRect.bottom = (getMeasuredHeight() + realImgShowHeight) / 2;
            mLimitMaxRect.left = (getMeasuredWidth() - realImgShowWidth) / 2;
            mLimitMaxRect.right = (getMeasuredWidth() + realImgShowWidth) / 2;

            MyLog.d(TAG, "getImgDisplaySize", "状态gggg:dw:dh:matrix:sx:sy:realImgShowWidth:realImgShowHeight:mLimitMaxRect:getMeasuredHeight():",
                    "进入获取imageView真实显示图片长宽方法" , dw , dh , m , sx , sy , realImgShowWidth , realImgShowHeight , mLimitMaxRect , getMeasuredHeight());
        }
    }

    public interface OnLimitRectChangedListener {
        void onLimitRectChanged(org.opencv.core.Rect rect);
    }

}
