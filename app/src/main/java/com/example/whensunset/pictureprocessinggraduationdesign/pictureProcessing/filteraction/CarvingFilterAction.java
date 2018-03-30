package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction;

import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/28.
 */

public class CarvingFilterAction implements FilterAction {
    private static CarvingFilterAction INSTANCE = new CarvingFilterAction();
    private static final String NAME = "雕刻";

    static {
        FilterAction.addFilterAction(INSTANCE);
    }

    private CarvingFilterAction() {
    }

    public static CarvingFilterAction getInstance() {
        return INSTANCE;
    }

    @Override
    public void filter(Mat oldMat, Mat newMat) {
        filterCarving(oldMat.getNativeObjAddr() , newMat.getNativeObjAddr());
    }

    private native void filterCarving(long in, long out);

    @Override
    public String getFilterName() {
        return NAME;
    }


}
