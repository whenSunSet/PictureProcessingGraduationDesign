package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction;

import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/28.
 */

public class ComicBooksFilterAction implements FilterAction {
    private static ComicBooksFilterAction INSTANCE = new ComicBooksFilterAction();
    private static final String NAME = "连环画";

    private ComicBooksFilterAction() {
    }

    public static ComicBooksFilterAction getInstance() {
        return INSTANCE;
    }

    @Override
    public void filter(Mat oldMat, Mat newMat) {
        filterComicBooks(oldMat.getNativeObjAddr() , newMat.getNativeObjAddr());
    }

    private native void filterComicBooks(long in, long out);

    @Override
    public String getFilterName() {
        return NAME;
    }


}
