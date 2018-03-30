package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction;

import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/30.
 */

public class NostalgiaFilterAction implements FilterAction{
    private static NostalgiaFilterAction INSTANCE = new NostalgiaFilterAction();
    private static final String NAME = "雕刻";

    static {
        FilterAction.addFilterAction(INSTANCE);
    }

    private NostalgiaFilterAction() {
    }

    public static NostalgiaFilterAction getInstance() {
        return INSTANCE;
    }

    @Override
    public void filter(Mat oldMat, Mat newMat) {
        filterNostalgia(oldMat.getNativeObjAddr() , newMat.getNativeObjAddr());
    }

    private native void filterNostalgia(long in, long out);

    @Override
    public String getFilterName() {
        return NAME;
    }

}
