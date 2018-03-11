package com.example.whensunset.pictureprocessinggraduationdesign.impl;

import org.jetbrains.annotations.NotNull;

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

    /**
     * {@link ConsumerChain removeAfterUndoRedoPointConsumer}
     * 中调用，其中只会出现 previousConsumer，nextConsumer 为 null,null 和 非null,null 这两种情况
     * @param linkedMyConsumer 不可为null
     */
    public static void remove(@NotNull LinkedMyConsumer linkedMyConsumer) {
        LinkedMyConsumer previousConsumer = linkedMyConsumer.mPreviousConsumer;
        LinkedMyConsumer nextConsumer = linkedMyConsumer.mNextConsumer;

        if (previousConsumer != null) {
            previousConsumer.mNextConsumer = nextConsumer;
            linkedMyConsumer.mPreviousConsumer = null;
        }

        if (nextConsumer != null) {
            nextConsumer.mPreviousConsumer = previousConsumer;
            linkedMyConsumer.mNextConsumer = null;
        }
    }
}
