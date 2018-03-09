package com.example.whensunset.pictureprocessinggraduationdesign.impl;

import android.util.Log;

import com.example.whensunset.pictureprocessinggraduationdesign.base.MyConsumer;

import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/8.
 */

public abstract class BaseMyConsumer implements MyConsumer<Mat , Mat> {
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

    protected abstract Mat onNewResultImpl(Mat oldResult);

    protected abstract void onFailureImpl(Throwable t);

    protected abstract void onCancellationImpl();

    protected void onUnhandledException(Exception e) {
        Log.d("何时夕:BaseMyConsumer" + this.getClass() , ("Consumer 抛出异常:" + e.getMessage() + " " + e.toString()));
    }
}
