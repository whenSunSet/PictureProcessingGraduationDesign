package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM;

import android.databinding.Observable;
import android.databinding.ObservableField;
import android.net.Uri;
import android.text.TextUtils;

import com.example.whensunset.pictureprocessinggraduationdesign.BR;
import com.example.whensunset.pictureprocessinggraduationdesign.PictureProcessingApplication;
import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemManagerVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.IImageUriFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.LocalFrameImageUriFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.PictureFrameMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureFrameItemVM_frameImagePath;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureFrameItemVM_mPosition;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTransformMenuVM_mat;


/**
 * Created by whensunset on 2018/3/6.
 */

public class PictureFrameMenuVM extends BaseItemManagerVM<PictureFrameMenuVM.PictureFrameItemVM> {
    public static final String TAG = "何时夕:PictureFrameMenuVM";

    public static final int MENU_PADDING = 18;
    public static final int MENU_HEIGHT = PictureTransformMenuVM.MENU_ITEM_WIDTH;
    public static final int CLICK_IMAGE = 0;
    public static final int LEAVE_FRAME_LISTENER = 1;

    public final int mItemHeight = MyUtil.dip2px(PictureTransformMenuVM.MENU_ITEM_WIDTH) ;
    public final int mItemWidth = mItemHeight;

    public final ObservableField<Integer> mSelectedPosition=new ObservableField<>(0);
    public final ObservableField<String> mInsertImagePath = new ObservableField<>();

    private final IImageUriFetch mLocalFrameImageUriFetch = LocalFrameImageUriFetch.getInstance();
    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();

    public PictureFrameMenuVM() {
        super(2 , BR.viewModel , R.layout.activity_picture_processing_picture_frame_item);
        initListener();
        showFrameImage();
    }

    private void initListener() {
        // 监听 item 的点击，以切换选中的item
        Flowable.fromArray(CLICK_IMAGE)
                .map((Function<Integer, ObservableField<? super Object>>) this::getListener)
                .subscribe(observableField -> observableField.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        Integer selectPosition = ObserverParamMap.staticGetValue(observable , PictureFrameItemVM_mPosition);
                        String frameImagePath = ObserverParamMap.staticGetValue(observable , PictureFrameItemVM_frameImagePath);
                        if (!mSelectedPosition.get().equals(selectPosition)) {
                            mDataItemList.get(mSelectedPosition.get()).isSelected.set(false);
                            mSelectedPosition.set(selectPosition);
                            mInsertImagePath.set(frameImagePath);
                        }
                        MyLog.d(TAG, "initListener", "状态:selectPosition:frameImagePath:", "" , selectPosition , frameImagePath);
                    }
                }));

    }

    private void showFrameImage() {
        mDataItemList.clear();

        PictureFrameItemVM firstPictureFrameItemVM = new PictureFrameItemVM(getClickListenerList() ,
                "android.resource://" + PictureProcessingApplication.getAppContext().getPackageName() + "/" +R.drawable.picture_frame_add , true , 0);
        mDataItemList.add(firstPictureFrameItemVM);

        final int[] nowPosition = {1};
        Flowable.fromIterable(mLocalFrameImageUriFetch.getAllImageUriList())
                .map(frameImageUri -> new PictureFrameMenuVM.PictureFrameItemVM(getClickListenerList() , frameImageUri , nowPosition[0]++))
                .subscribe(mDataItemList::add);
        MyLog.d(TAG, "showFrameImage", "状态:mDataItemList", "" , mDataItemList);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataItemList.get(mSelectedPosition.get()).isSelected.set(false);
        mInsertImagePath.set("");
        mSelectedPosition.set(0);
    }

    @Override
    public void onStop() {
        super.onStop();
        runInsertImage();
    }


    private void runInsertImage() {
        if (!TextUtils.isEmpty(mInsertImagePath.get())) {
            PictureFrameMyConsumer consumer = new PictureFrameMyConsumer(mInsertImagePath.get() , null);
            mStringConsumerChain
                    .rxRunNextConvenient(consumer)
                    .subscribe(mat -> getListener(LEAVE_FRAME_LISTENER).set(ObserverParamMap.staticSet(PictureTransformMenuVM_mat, mat)));

        }

        MyLog.d(TAG, "runInsertImage", "状态:mInsertImagePath:", "在离开frame的时候进行图片插入" , mInsertImagePath.get());
    }

    public static class PictureFrameItemVM extends BaseItemVM {
        public static final String TAG = "何时夕:PictureFrameItemVM";

        public static final int ITEM_PICTURE_RESIZE_WIDTH = 80;
        public static final int ITEM_PICTURE_RESIZE_HEIGHT = 80;

        private boolean isAdd = false;
        private final Integer mPosition;

        public final ObservableField<String> mImageUri=new ObservableField<>();
        public final ObservableField<Boolean> isSelected=new ObservableField<>();

        public PictureFrameItemVM(List<ObservableField<? super Object>> clickItemListenerList , String imageUri , boolean isAdd , Integer position) {
            this(clickItemListenerList , imageUri , position);
            this.isAdd = isAdd;
        }

        public PictureFrameItemVM(List<ObservableField<? super Object>> clickItemListenerList , String imageUri , Integer position) {
            super(clickItemListenerList);
            mImageUri.set(imageUri);
            mPosition = position;
            isAdd = false;
            isSelected.set(false);
            initClickAction();
        }

        protected void initClickAction() {
            getDefaultClickFlowable()
                    .filter(position -> {
                        MyLog.d(TAG, "initClickAction", "状态:isAdd", "判断当前的item是否是 add" , isAdd);
                        return !isAdd;
                    }).map(position -> {
                        String frameImagePath = Uri.parse(mImageUri.get()).getPath();
                        MyLog.d(TAG, "apply", "状态:position:frameImagePath:", "" , position , frameImagePath);
                        return frameImagePath;
                    }).subscribe(frameImagePath -> {
                        MyLog.d(TAG, "accept", "状态:frameImagePath:mPosition:", "结束了为图片添加图片框" , frameImagePath , mPosition);
                        isSelected.set(true);
                        getListener(CLICK_IMAGE).set(ObserverParamMap
                                        .staticSet(PictureFrameItemVM_mPosition , mPosition)
                                        .set(PictureFrameItemVM_frameImagePath , frameImagePath));
                    });
        }

        @Override
        public String toString() {
            return "PictureFrameItemVM{" +
                    "isAdd=" + isAdd +
                    ", mImageUri=" + mImageUri.get() +
                    ", mPosition=" + mPosition +
                    '}';
        }
    }
}
