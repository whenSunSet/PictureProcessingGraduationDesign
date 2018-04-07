package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction;

import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/28.
 */

public class ReliefFilterAction implements FilterAction {
    private static ReliefFilterAction INSTANCE = new ReliefFilterAction();
    private static final String NAME = "浮雕";

    private ReliefFilterAction() {
    }

    public static ReliefFilterAction getInstance() {
        return INSTANCE;
    }

    @Override
    public void filter(Mat oldMat, Mat newMat) {
        filterRelief(oldMat.getNativeObjAddr() , newMat.getNativeObjAddr());
    }

    @Override
    public String getFilterName() {
        return NAME;
    }

    private native void filterRelief(long in, long out);
}
