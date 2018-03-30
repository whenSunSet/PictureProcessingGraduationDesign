package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction;

import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/28.
 */

public class ReliefFilterAction implements FilterAction {
    public static final String TAG = "何时夕:ReliefFilterAction";
    private static final String NAME = "浮雕";

    private static ReliefFilterAction INSTANCE = new ReliefFilterAction();

    static {
        FilterAction.addFilterAction(INSTANCE);
    }

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
