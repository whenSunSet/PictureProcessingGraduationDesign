package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM;

import android.databinding.ObservableField;
import android.net.Uri;

import com.example.whensunset.pictureprocessinggraduationdesign.BR;
import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseSeekBarRecycleViewVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ItemBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.PictureFilterMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.filteraction.FilterAction;

import org.opencv.core.Mat;

import java.io.File;
import java.util.List;

import io.reactivex.Flowable;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureFilterItemVM_mFilterAction;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureFilterItemVM_mat;


/**
 * Created by whensunset on 2018/3/6.
 */

public class PictureFilterMenuVM extends BaseSeekBarRecycleViewVM<PictureFilterMenuVM.PictureFilterItemVM> {
    public static final String TAG = "何时夕:PictureFilterMenuVM";

    private final StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    private final String mSampleImageUri;
    private Boolean isRunNow = false;

    public PictureFilterMenuVM() {
        super(3 , BR.viewModel , R.layout.activity_picture_processing_picture_filter_item);
        mSampleImageUri = Uri.fromFile(new File("/storage/emulated/0/Download/055123530.jpg")).toString();
        initItemVM();
        initClick();
    }

    @Override
    protected void initItemVM() {
        final int[] position = {0};
        Flowable.fromIterable(FilterAction.getAllFilterAction())
                .subscribe(filterAction -> mDataItemList.add(new PictureFilterItemVM(mEventListenerList , false , position[0]++ , filterAction , mSampleImageUri)));
    }

    @Override
    protected void initClick() {
        initListener(this, (observable, i) -> {
            Mat mat = ObserverParamMap.staticGetValue(observable , PictureFilterItemVM_mat);
            if (mat != null) {
                return;
            }
            FilterAction filterAction = ObserverParamMap.staticGetValue(observable , PictureFilterItemVM_mFilterAction);
            PictureFilterMyConsumer pictureFilterMyConsumer = new PictureFilterMyConsumer(filterAction);
            Flowable<Mat> flowable = null;
            if (isRunNow) {
                flowable = mStringConsumerChain.rxRunNowConvenient(pictureFilterMyConsumer);
            } else {
                flowable = mStringConsumerChain.rxRunNextConvenient(pictureFilterMyConsumer);
                isRunNow = true;
            }

            flowable.subscribe(mat1 -> {
                ObserverParamMap observerParamMap = ObserverParamMap.staticSet(PictureFilterItemVM_mat , mat1);
                mEventListenerList.get(CLICK_ITEM).set(observerParamMap);
                MyLog.d(TAG, "initClick", "状态:observerParamMap:", "" , observerParamMap);
            });
        }, CLICK_ITEM);
    }

    @Override
    protected void initProgressChanged() {

    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void stop() {
        super.stop();
        isRunNow = false;
    }


    public static class PictureFilterItemVM extends ItemBaseVM {
        public static final String TAG = "何时夕:PictureFilterItemVM";

        private boolean isAdd = false;
        public final FilterAction mFilterAction;
        public final String mSampleImageUri;

        public PictureFilterItemVM(List<ObservableField<? super Object>> clickItemListenerList , boolean isAdd , Integer position , FilterAction filterAction , String sampleImageUri) {
            this(clickItemListenerList , position , filterAction , sampleImageUri);
            this.isAdd = isAdd;
        }

        public PictureFilterItemVM(List<ObservableField<? super Object>> clickItemListenerList , Integer position , FilterAction filterAction , String sampleImageUri) {
            super(clickItemListenerList , position);
            initDefaultUIActionManager();

            mSampleImageUri = sampleImageUri;
            mFilterAction = filterAction;
            initClickAction();
        }

        private void initClickAction() {
            getDefaultClickThrottleFlowable()
                    .filter(clickPosition -> {
                        MyLog.d(TAG, "initClickAction", "状态:isAdd:mFilterAction", "判断当前的item是否是 add" , isAdd , mFilterAction);
                        return (!isAdd && mFilterAction != null);
                    })
                    .subscribe(clickPosition -> {
                        ObserverParamMap observerParamMap = getPositionParamMap()
                                .set(PictureFilterItemVM_mFilterAction , mFilterAction);
                        mEventListenerList.get(CLICK_ITEM).set(observerParamMap);
                        MyLog.d(TAG, "initClickAction", "状态:observerParamMap:clickPosition", "" , observerParamMap , clickPosition);
                    });
        }

        @Override
        public String toString() {
            return "PictureFilterItemVM{" +
                    "isAdd=" + isAdd +
                    ", mPosition=" + mPosition +
                    ", mSampleImageUri=" + mSampleImageUri +
                    ", mFilterAction=" + mFilterAction +
                    '}';
        }
    }
}
