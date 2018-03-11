package com.example.whensunset.pictureprocessinggraduationdesign.impl;

import com.example.whensunset.pictureprocessinggraduationdesign.base.MyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyLog;

import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/8.
 */

public abstract class BaseMyConsumer implements MyConsumer<Mat , Mat> {
    public static final String TAG = "何时夕:BaseMyConsumer";

    protected boolean isSaveNowResult = false;
    private Mat mNowResult;

    @Override
    public Mat onNewResult(Mat oldResult) {
        try {
            Mat nowResult = onNewResultImpl(oldResult);
            if (isSaveNowResult) {
                mNowResult = nowResult;
            }
            return nowResult;
        } catch (Exception e) {
            onUnhandledException(e);
        }
        return null;
    }

    @Override
    public synchronized void onFailure(Throwable throwable) {
        try {
            onFailureImpl(throwable);
        } catch (Exception e) {
            onUnhandledException(e);
        }
    }

    @Override
    public synchronized void onCancellation() {
        try {
            onCancellationImpl();
        } catch (Exception e) {
            onUnhandledException(e);
        }
    }

    public Mat getNowResult() {
        return mNowResult;
    }

    public void setNowResult(Mat nowResult) {
        this.mNowResult = nowResult;
    }

    public abstract void copy(BaseMyConsumer baseMyConsumer);

    protected abstract Mat onNewResultImpl(Mat oldResult);

    protected abstract void onFailureImpl(Throwable t);

    protected abstract void onCancellationImpl();


    protected void onUnhandledException(Exception e) {
        MyLog.d(TAG, "onUnhandledException", "class:e.getMessage():e.toString():" , this.getClass().getName() , e.getMessage() , e.toString());
    }

}
