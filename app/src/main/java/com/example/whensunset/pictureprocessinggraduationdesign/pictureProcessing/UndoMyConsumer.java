package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing;

import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;

import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/8.
 */

public abstract class UndoMyConsumer extends BaseMyConsumer {

    public abstract Mat undo(Mat oldResult);
}
