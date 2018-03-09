package com.example.whensunset.pictureprocessinggraduationdesign.base;

import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.CutMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.UndoMyConsumer;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by whensunset on 2018/3/8.
 */

public abstract class ConsumerChain<T> {
    private List<BaseMyConsumer> mConsumerList;
    private int mUndoRedoPoint;
    private Mat mLastMat;
    private Mat mFirstMat;
    protected CutMyConsumer mFirstCutMyConsumer;

    protected ConsumerChain() {
        mConsumerList = new ArrayList<>();
        mUndoRedoPoint = 0;
        mFirstCutMyConsumer = new CutMyConsumer(null , null , new Rect(0 , 0 , 0 , 0)) {
            @Override
            protected Mat onNewResultImpl(Mat oldResult) {
                return mFirstMat;
            }
        };
        addConsumer(mFirstCutMyConsumer);
    }

    public Flowable<Mat> rxRunStartConvenient(T startParam , BaseMyConsumer... consumers) {
        return rxRunStart(startParam , consumers)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Mat> rxRunStart(T startParam , BaseMyConsumer... consumers) {
        return Flowable.just(runStart(startParam , consumers));
    }

    private Mat runStart(T startParam , BaseMyConsumer... consumers) {
        clear();
        if (consumers != null && consumers.length != 0 && consumers[0] != null) {
            addConsumers(consumers);
        }

        Mat t = getStartResult(startParam);
        mFirstMat = t;
        for (int i = 0; i < mConsumerList.size(); i++) {
            BaseMyConsumer consumer = mConsumerList.get(i);
            if (t == null) {
                consumer.onFailure(new Exception("图像处理调用链路出错"));
                t = null;
            } else  {
                t = consumer.onNewResult(t);
            }
        }
        mLastMat = t;
        return t;
    }

    public Flowable<Mat> rxRunNextConvenient(BaseMyConsumer consumer) {
        return rxRunNext(consumer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Mat> rxRunNext(BaseMyConsumer consumer) {
        return Flowable.just(runNext(consumer));
    }

    private Mat runNext(BaseMyConsumer baseMyConsumer) {
        if (mConsumerList.size() == 0) {
            baseMyConsumer.onFailure(new Exception("图像调用链路中没有消费者，不可继续调用"));
            return null;
        }

        if (mLastMat == null) {
            baseMyConsumer.onFailure(new Exception("图像调用链路中最后一个图像为null，不可继续调用"));
            return null;
        }

        addConsumer(baseMyConsumer);
        Mat t = baseMyConsumer.onNewResult(mLastMat);
        mLastMat = t;
        return t;
    }

    public Flowable<Mat> rxRunNowConvenient(BaseMyConsumer consumer) {
        return rxRunNow(consumer)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Mat> rxRunNow(BaseMyConsumer consumer) {
        return Flowable.just(runNow(consumer));
    }

    private Mat runNow(BaseMyConsumer baseMyConsumer) {
        if (mConsumerList.size() == 0) {
            baseMyConsumer.onFailure(new Exception("图像调用链路中没有消费者，不可调用当前消费者"));
            return null;
        }

        if (mLastMat == null) {
            baseMyConsumer.onFailure(new Exception("图像调用链路中最后一个图像为null，不可调用当前消费者"));
            return null;
        }

        undo();
        return runNext(baseMyConsumer);
    }

    public Mat undo() {
        if (mUndoRedoPoint <= 0) {
            return null;
        }

        BaseMyConsumer nowConsumer = mConsumerList.get(mUndoRedoPoint);
        if (nowConsumer instanceof UndoMyConsumer) {
            mLastMat = ((UndoMyConsumer) nowConsumer).undo(mLastMat);
        } else if (nowConsumer instanceof CutMyConsumer) {
            mLastMat = ((CutMyConsumer) nowConsumer).undo(mFirstMat);
        }
        mUndoRedoPoint--;
        return mLastMat;
    }

    public Mat redo() {
        if (mUndoRedoPoint >= (mConsumerList.size() - 1)) {
            return null;
        }
        mUndoRedoPoint++;
        return runNext(mConsumerList.get(mUndoRedoPoint));
    }

    public CutMyConsumer getFirstCutMyConsumer() {
        return mFirstCutMyConsumer;
    }

    public void destroy() {
        mConsumerList.clear();
        mUndoRedoPoint = 0;
        mFirstMat = null;
        mLastMat = null;
    }

    private void clear() {
        BaseMyConsumer firstConsumer = mConsumerList.get(0);
        destroy();
        mConsumerList.add(firstConsumer);
    }

    private void addConsumer(BaseMyConsumer consumer) {
        addConsumers(new BaseMyConsumer[]{consumer});
    }

    private void addConsumers(BaseMyConsumer[] consumers) {
        removeAfterUndoRedoPointConsumer();
        mConsumerList.addAll(Arrays.asList(consumers));
        mUndoRedoPoint = mConsumerList.size() - 1;
    }

    private void removeAfterUndoRedoPointConsumer() {
        Iterator<BaseMyConsumer> iteratorBaseMyConsumer = mConsumerList.listIterator();
        for (int i = 0; i < mConsumerList.size(); i++) {
            if (i <= mUndoRedoPoint) {
                if (iteratorBaseMyConsumer.hasNext()) {
                    iteratorBaseMyConsumer.next();
                }
            } else {
                if (mConsumerList.get(i) instanceof CutMyConsumer) {
                    ((CutMyConsumer) mConsumerList.get(i)).setPreviousConsumer(null);
                }
                iteratorBaseMyConsumer.remove();
            }
        }
    }

    protected abstract Mat getStartResult(T startParam);
}
