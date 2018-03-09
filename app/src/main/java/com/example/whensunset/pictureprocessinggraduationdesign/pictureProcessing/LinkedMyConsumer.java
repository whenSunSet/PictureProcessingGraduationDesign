package com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing;

/**
 * Created by whensunset on 2018/3/8.
 */

public abstract class LinkedMyConsumer extends UndoMyConsumer {
    protected LinkedMyConsumer mPreviousConsumer;
    protected LinkedMyConsumer mNextConsumer;

    public LinkedMyConsumer(LinkedMyConsumer previousConsumer, LinkedMyConsumer nextConsumer) {
        mPreviousConsumer = previousConsumer;
        mNextConsumer = nextConsumer;
    }

    public LinkedMyConsumer getPreviousConsumer() {
        return mPreviousConsumer;
    }

    public LinkedMyConsumer getNextConsumer() {
        return mNextConsumer;
    }

    public void setPreviousConsumer(LinkedMyConsumer previousConsumer) {
        mPreviousConsumer = previousConsumer;
    }

    public void setNextConsumer(LinkedMyConsumer nextConsumer) {
        mNextConsumer = nextConsumer;
    }

    public static LinkedMyConsumer getFirstConsumer(LinkedMyConsumer linkedMyConsumer) {
        if (linkedMyConsumer == null) {
            return null;
        }

        if (linkedMyConsumer.mPreviousConsumer == null) {
            return linkedMyConsumer;
        }

        LinkedMyConsumer nowLinkedMyConsumer = linkedMyConsumer.mPreviousConsumer;
        while (nowLinkedMyConsumer.mPreviousConsumer != null) {
            nowLinkedMyConsumer = nowLinkedMyConsumer.mPreviousConsumer;
        }

        return nowLinkedMyConsumer;
    }

    public static LinkedMyConsumer getLastConsumer(LinkedMyConsumer linkedMyConsumer) {
        if (linkedMyConsumer == null) {
            return null;
        }

        if (linkedMyConsumer.mNextConsumer == null) {
            return linkedMyConsumer;
        }

        LinkedMyConsumer nowLinkedMyConsumer = linkedMyConsumer.mNextConsumer;
        while (nowLinkedMyConsumer.mNextConsumer != null) {
            nowLinkedMyConsumer = nowLinkedMyConsumer.mNextConsumer;
        }

        return nowLinkedMyConsumer;
    }
}
