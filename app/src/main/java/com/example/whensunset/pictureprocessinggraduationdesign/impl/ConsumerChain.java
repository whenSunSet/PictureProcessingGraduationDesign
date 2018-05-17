package com.example.whensunset.pictureprocessinggraduationdesign.impl;

import com.example.whensunset.pictureprocessinggraduationdesign.base.Chain;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.CutMyConsumer;

import org.jetbrains.annotations.NotNull;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by whensunset on 2018/3/8.
 */

public abstract class ConsumerChain<T> implements Chain<T , Mat> {
    public static final String TAG = "何时夕:ConsumerChain";

    private List<BaseMyConsumer> mConsumerList;// 储存 consumer的列表
    private int mConsumerPoint;// 指向当前consumer的指针

    private Mat mFirstMat;// 初始的图像
    private Mat mPreviousMat;// 当前图像的前一个图像
    private Mat mNowMat;// 当前的图像
    private Rect nowRect;

    private boolean isStarted;// 同一个 Chain 可以重新调用restart重新启动
    private boolean isInited;// 同一个 Chain 只能初始化一次

    protected ConsumerChain() {
        mConsumerList = new ArrayList<>();
        mConsumerPoint = -1;
        mFirstMat = null;
        mPreviousMat = null;
        mNowMat = null;
        isStarted = false;
        isInited = false;
        MyLog.d(TAG, "ConsumerChain", "状态:mConsumerPoint:", "初始化" , mConsumerPoint);
    }

    /**
     * 设置第一个图像，一个对象只能被初始化一次
     * @param startParam
     */
    @Override
    public void init(@NotNull T startParam) {
        if (isInited) {
            throw new RuntimeException("该 Chain 已经初始化了，不能再重新初始化");
        }

        mFirstMat = getStartResult(startParam);
        mPreviousMat = null;
        mNowMat = mFirstMat;
        isInited = true;
        nowRect = new Rect(0 , 0 , mFirstMat.width() , mFirstMat.height());
        MyLog.d(TAG, "getBaseVM", "状态:startParam:", "初始化" , startParam);
    }

    /**
     * 运行当前传入的 consumer ， 需要在初始化完毕之后调用，一个对象只能运行一次
     * @param consumers
     * @return
     */
    @Override
    public Mat runStart(@NotNull BaseMyConsumer... consumers) {
        MyLog.d(TAG, "runStart", "状态:consumers:", "开始运行" ,  Arrays.toString(consumers));

        if (!isInited) {
            throw new RuntimeException("该 Chain 还未初始化");
        }

        if (isStarted) {
            throw new RuntimeException("该 Chain 已经启动");
        }

        if (consumers.length == 0) {
            MyLog.d(TAG, "runStart", "状态:" , "传入的 consumer 为空");
            return mNowMat;
        }

        if (mPreviousMat != null && mPreviousMat != mFirstMat){
            mPreviousMat.release();
        }
        mPreviousMat = mNowMat;
        mNowMat = runConsumers(Arrays.asList(consumers) , mFirstMat);
        addConsumers(consumers);

        isStarted = true;
        return mNowMat;
    }

    /**
     * 在 Chain 初始化 并且 开始运行之后 ， 继续运行单个 consumer ，传入的 consumer 可为null， 这样会返回当前的图像
     * @param nextMyConsumer
     * @return
     */
    @Override
    public Mat runNext(BaseMyConsumer nextMyConsumer) {

        MyLog.d(TAG, "runStart", "状态:", "运行下一个" , nextMyConsumer);

        checkState();

        if (nextMyConsumer == null) {
            MyLog.d(TAG, "runNext", "状态:" , "传入的 consumer 为空");
            return mNowMat;
        }

        BaseMyConsumer nowConsumer = getNowConsumer();
        boolean isNeedRun = nowConsumer.isNeedRun(nextMyConsumer);
        if (isNeedRun) {
            if (mPreviousMat != null && mPreviousMat != mFirstMat) {
                mPreviousMat.release();
            }
            mPreviousMat = mNowMat;
            mNowMat = runConsumers(Collections.singletonList(nextMyConsumer) , mNowMat);

            addConsumer(nextMyConsumer);
        }
        MyLog.d(TAG, "runNext", "状态:isNeedRun:nowConsumer:mNowMat:mPreviousMat:", ""  , isNeedRun , nowConsumer , mNowMat , mPreviousMat);
        return mNowMat;
    }

    /**
     * 修改当前 consumer 的参数 然后重新运行一遍，这里不会增加 consumer的数量 , 不能修改第一个 consumer的参数
     * @param changeMyConsumer
     * @return
     */
    @Override
    public Mat runNow(BaseMyConsumer changeMyConsumer) {
        MyLog.d(TAG, "runNow", "状态:consumer:", "重新运行当前的" , changeMyConsumer);

        if (!canRunNow(changeMyConsumer)) {
            throw new RuntimeException("不能重新运行当前consumer");
        }

        BaseMyConsumer nowConsumer = getNowConsumer();
        boolean isNeedRun = nowConsumer.isNeedRun(changeMyConsumer);
        if (isNeedRun) {
            nowConsumer.copy(changeMyConsumer);
            mNowMat = runConsumers(Collections.singletonList(nowConsumer) , mPreviousMat);
        }
        MyLog.d(TAG, "runNow", "状态:isNeedRun:nowConsumer:changeMyConsumer:mNowMat:mPreviousMat:", "" , isNeedRun , nowConsumer , changeMyConsumer , mNowMat , mPreviousMat);
        return mNowMat;
    }

   /**
     * 撤销当前 consumer 对图片的影响，具体的方法是 每次都undo 都将前面的 consumer 都运行一遍 获取到 mPreviousMat ，为下次undo做准备
     * @return
     */
    @Override
    public Mat undo() {
        MyLog.d(TAG, "undo", "状态:mConsumerPoint:currentConsumer:" , "进入undo" , mConsumerPoint , mConsumerList.get(mConsumerPoint));

        checkState();

        if (!canUndo()) {
            throw new RuntimeException("当前状态不可以 undo");
        }

        if (mNowMat != null && mNowMat != mFirstMat) {
            mNowMat.release();
        }
        mNowMat = mPreviousMat;
        if (mConsumerPoint <= 1) {
            // 如果当前 consumer 指针指向 第二个consumer的时候，当撤销了之后，mPreviousMat 应该为null
            mPreviousMat = null;
        } else if (mConsumerPoint == 2) {
            mPreviousMat = mFirstMat;
        } else {
            // 如果当前 consumer 指针指向 第二个之后的 consumer ，那么应该将 mFistMat 连续被 第二个 consumer 到 第mConsumerPoint-1 个consumer 消费，以得到mPreviousMat
            int start = 1 , end = mConsumerPoint - 1;
            List<BaseMyConsumer> subList = mConsumerList.subList(start , end);
            mPreviousMat = runConsumers(subList , mFirstMat);
            MyLog.d(TAG, "undo", "状态:subList:", "当前mConsumerPoint大于2" , subList);
        }

        mConsumerPoint--;
        BaseMyConsumer nowConsumer = getNowConsumer();
        if (nowConsumer instanceof CutMyConsumer) {
            nowRect = ((CutMyConsumer) nowConsumer).getRect();
        }

        MyLog.d(TAG, "undo", "状态:mConsumerPoint:mNowMat:mPreviousMat:nowConsumer:", "undo完毕" , mConsumerPoint , mNowMat , mPreviousMat , nowConsumer);
        return mNowMat;
    }

    /**
     * 重做后一个 consumer
     * @return
     */
    @Override
    public Mat redo() {
        MyLog.d(TAG, "redo", "状态:mConsumerPoint:nextConsumer:" , "进入redo" , mConsumerPoint , mConsumerList.get(mConsumerPoint + 1));

        checkState();

        if (!canRedo()) {
            throw new RuntimeException("当前状态不可以 redo");
        }

        mConsumerPoint++;
        if (mPreviousMat != null && mPreviousMat != mFirstMat) {
            mPreviousMat.release();
        }
        mPreviousMat = mNowMat;
        mNowMat = runConsumers(Collections.singletonList(getNowConsumer()), mNowMat);

        MyLog.d(TAG, "redo", "状态:mConsumerPoint:mNowMat:mPreviousMat", "redo完成" , mConsumerPoint , mNowMat , mPreviousMat);
        return mNowMat;
    }

    /**
     * 取消当前的 consumer
     * @return
     */
    public Mat cancelNowConsumer() {
        if (!canUndo()) {
            throw new RuntimeException("不可以取消当前的 consumer");
        }
        Mat mat = undo();
        removeAfterUndoRedoPointConsumer();
        return mat;
    }

    public boolean canUndo() {
        return mConsumerPoint > 0;
    }

    public boolean canRedo() {
        return mConsumerPoint < (mConsumerList.size() - 1);
    }

    public boolean canRunNow(BaseMyConsumer changeMyConsumer) {
        checkState();
        return getNowConsumer().canRunNow(changeMyConsumer);
    }

    /**
     * 按顺序运行 consumer ,不接收为null 的consumer，遇见了会跳过
     * @param consumers
     * @param mat
     * @return
     */
    private Mat runConsumers(@NotNull List<BaseMyConsumer> consumers , @NotNull Mat mat) {
        MyLog.d(TAG, "runConsumers", "状态:consumers:mat:", "运行一系列consumer" , consumers , mat);
        Mat nowMat = mat;
        for (int i = 0; i < consumers.size(); i++) {
            BaseMyConsumer consumer = consumers.get(i);

            if (consumer == null) {
                MyLog.d(TAG, "runConsumers", "状态:" , "consumer 为null，略过该消费者");
                continue;
            }

            if (nowMat == null) {
                consumer.onFailure(new Exception(TAG + ":consumer 返回结果为null"));
                nowMat = null;
            } else  {
                nowMat = consumer.onNewResult(nowMat);
            }

            if (consumer instanceof CutMyConsumer) {
                nowRect = ((CutMyConsumer) consumer).getRect();
            }
        }
        return nowMat;
    }

    protected void addConsumer(BaseMyConsumer consumer) {
        addConsumers(new BaseMyConsumer[]{consumer});
    }

    /**
     * 先移除 consumer指针 后面的的 consumer，然后按顺序将 consumer 添加到列表中，并且为 链表consumer 接上，如果有为null 的consumer 就跳过
     * 最后重新将 consumer 指针指向最后一个 consumer
     * @param consumers
     */
    private void addConsumers(@NotNull BaseMyConsumer[] consumers) {
        MyLog.d(TAG, "addConsumers", "状态:consumers:mConsumerPoint:", "添加consumer" , Arrays.toString(consumers) , mConsumerPoint);
        removeAfterUndoRedoPointConsumer();

        for (BaseMyConsumer b : consumers) {
            if (b != null) {
                mConsumerList.add(b);
            } else {
                MyLog.d(TAG, "addConsumers", "状态:", "被添加的consumer为null，跳过");
            }
        }

        mConsumerPoint = mConsumerList.size() - 1;
        MyLog.d(TAG, "addConsumers", "状态:mConsumerPoint:", "添加consumer完毕" , mConsumerPoint);
    }

    /**
     * 如果当前的 consumer指针 指向的不是最后列表中最后一个consumer 那么将其后的 consumer 全部移除
     * 即处于 undo的时候 进行继续变化图像的时候 被undo的图像变化全部抛弃
     */
    private void removeAfterUndoRedoPointConsumer() {
        if ((mConsumerPoint + 1) == mConsumerList.size()) {
            MyLog.d(TAG, "removeAfterUndoRedoPointConsumer", "状态:" , "当前不处于undo状态，不需要移除消费者");
            return;
        }

        BaseMyConsumer nowConsumer = getNowConsumer();
        int needRemove = 0;
        for (int i = 0; i < mConsumerList.size(); i++) {
            if (nowConsumer == mConsumerList.get(i)) {
                needRemove = i + 1;
                MyLog.d(TAG, "removeAfterUndoRedoPointConsumer", "状态:needRemove", "接下来的consumer都需要被remove" , needRemove);
                break;
            }
        }

        int needRemoveSize = mConsumerList.size() - needRemove;
        for (int i = 0; i < needRemoveSize; i++) {
            MyLog.d(TAG, "removeAfterUndoRedoPointConsumer", "状态:needRemoveSize:i" , "移除一个consumer" , needRemoveSize , i);
            BaseMyConsumer removedConsumer = mConsumerList.remove(needRemove);
            removedConsumer.destroy();
        }
    }

    private void checkState() {
        if (!isInited) {
            throw new RuntimeException("该 Chain 还未初始化");
        }

        if (!isStarted) {
            throw new RuntimeException("该 Chain 还未启动");
        }
    }

    @Override
    public void destroy() {
        for (BaseMyConsumer removedConsumer : mConsumerList) {
            removedConsumer.destroy();
        }

        mConsumerList.clear();
        mConsumerPoint = -1;
        mFirstMat = null;
        mPreviousMat = null;
        mNowMat = null;
        isStarted = false;
        isInited = false;

        MyLog.d(TAG, "destroy", "状态:mConsumerPoint" , "销毁" , mConsumerPoint);
    }

    public Flowable<Mat> rxRunStartConvenient(BaseMyConsumer... consumers) {
        return rxRunStart(consumers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Mat> rxRunStart(BaseMyConsumer... consumers) {
        return Flowable.just(consumers)
                .map(this::runStart);
    }

    public Flowable<Mat> rxRunNextConvenient(BaseMyConsumer consumer) {
        return rxRunNext(consumer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Flowable<Mat> rxRunNext(BaseMyConsumer consumer) {
        return Flowable.just(consumer)
                .map(this::runNext);
    }


    public Flowable<Mat> rxRunNowConvenient(BaseMyConsumer consumer) {
        return rxRunNow(consumer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Mat> rxRunNow(BaseMyConsumer consumer) {
        return Flowable.just(consumer)
                .map(this::runNow);
    }


    public Flowable<Mat> rxUndoConvenient() {
        return rxUndo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Mat> rxUndo() {
        return Flowable.just("1")
                .map(new Function<Object, Mat>() {
                    @Override
                    public Mat apply(Object o) throws Exception {
                        return undo();
                    }
                });
    }

    public Flowable<Mat> rxRedoConvenient() {
        return rxRedo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Mat> rxRedo() {
        return Flowable.just("1")
                .map(new Function<String, Mat>() {
                    @Override
                    public Mat apply(String s) throws Exception {
                        return redo();
                    }
                });
    }

    public List<BaseMyConsumer> getConsumerList() {
        return mConsumerList;
    }

    public Rect getNowRect() {
        return nowRect;
    }

    public int getConsumerPoint() {
        return mConsumerPoint;
    }

    public BaseMyConsumer getNowConsumer() {
        checkState();
        return mConsumerList.get(mConsumerPoint);
    }

    public Mat getPreviousMat() {
        // 为了防止被外部释放掉 所以需要进行克隆
        return mPreviousMat == null ? null : mPreviousMat.clone();
    }

    public Mat getFirstMat() {
        // 为了防止被外部释放掉 所以需要进行克隆
        return mFirstMat == null ? null : mFirstMat.clone();
    }

    protected abstract Mat getStartResult(T startParam);
}
