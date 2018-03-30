package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction;

import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;

import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/30.
 */

public class NormalFilterAction implements FilterAction {
    private static NormalFilterAction INSTANCE = new NormalFilterAction();
    private static final String NAME = "正常";

    static {
        FilterAction.addFilterAction(INSTANCE);
    }

    private NormalFilterAction() {
    }

    public static NormalFilterAction getInstance() {
        return INSTANCE;
    }

    @Override
    public void filter(Mat oldMat, Mat newMat) {
        Mat normalMat = StringConsumerChain.getInstance().getPreviousMat();
        if (normalMat == null) {
            normalMat = StringConsumerChain.getInstance().getFirstMat();
        }
        normalMat.copyTo(newMat);
        normalMat.release();
    }

    @Override
    public String getFilterName() {
        return NAME;
    }

}
