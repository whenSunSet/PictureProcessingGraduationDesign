package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by whensunset on 2018/3/28.
 */

public interface FilterAction {
    String TAG = "何时夕:FilterAction";

    List<FilterAction> FILTER_ACTION_LIST = new ArrayList<>();

    static void init() {
        FilterAction.addFilterAction(CarvingFilterAction.getInstance());
        FilterAction.addFilterAction(ReliefFilterAction.getInstance());
        FilterAction.addFilterAction(NostalgiaFilterAction.getInstance());
        FilterAction.addFilterAction(ComicBooksFilterAction.getInstance());
    }

    static void addFilterAction(FilterAction filterAction) {
        FILTER_ACTION_LIST.add(filterAction);
    }

    static List<FilterAction> getAllFilterAction() {
        return FILTER_ACTION_LIST;
    }

    void filter(Mat oldMat , Mat newMat);
    String getFilterName();
}
