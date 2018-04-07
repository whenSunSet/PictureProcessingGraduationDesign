package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM;

import android.databinding.ObservableField;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;

import com.example.whensunset.pictureprocessinggraduationdesign.BR;
import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseSeekBarRecycleViewVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ITypefaceFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.ProgressChangedUIAction;
import com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.TextChangedUIAction;
import com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.UIActionManager;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ItemBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView;
import com.example.whensunset.pictureprocessinggraduationdesign.mete.MoveFrameLayout;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.PictureFrameMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;

import java.util.List;

import io.reactivex.Flowable;

import static com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.UIActionManager.PROGRESS_CHANGED_ACTION;
import static com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.UIActionManager.TEXT_CHANGED_ACTION;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTextItemVM_mPictureTextParamDialogVM;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTextItemVM_mTypefaceName;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTextItemVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.FONT_EDIT_VIEW_IMAGE;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTextParamDialogVM.STOP_TEXT_PARAM_DIALOG_VM;


/**
 * Created by whensunset on 2018/3/6.
 */

public class PictureTextMenuVM extends BaseSeekBarRecycleViewVM<PictureTextMenuVM.PictureTextItemVM>  implements CutView.OnLimitMaxRectChangeListener , MoveFrameLayout.OnPlaceChangedListener {
    public static final String TAG = "何时夕:PictureTextMenuVM";

    public static final int TEXT_CHANGE = 3;

    private final StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    private PictureTextParamDialogVM mPictureTextParamDialogVM = new PictureTextParamDialogVM("默认");
    public final ObservableField<String> mText = new ObservableField<>();
    public final ObservableField<String> mNowTypeface = new ObservableField<>("默认");
    public final ObservableField<Integer> mTextColor = new ObservableField<>(Color.BLACK);
    public final ObservableField<Integer> mTextSize = new ObservableField<>(20);

    public PictureTextMenuVM() {
        super(4 , BR.viewModel , R.layout.activity_picture_processing_picture_text_item);

        initItemVM();
        initClick();
        initTextChanged();
        initProgressChanged();
    }

    @Override
    protected void initDefaultUIActionManager() {
        mUIActionManager = new UIActionManager(this , PROGRESS_CHANGED_ACTION , TEXT_CHANGED_ACTION);
    }

    @Override
    protected void initItemVM() {
        final int[] position = {0};
        Flowable.fromIterable(ITypefaceFetch.getAllTypefaceNameFromAll())
                .subscribe(typefaceName -> mDataItemList.add(new PictureTextItemVM(mEventListenerList , false , position[0]++  , typefaceName , mPictureTextParamDialogVM)));
    }

    @Override
    protected void initClick() {
        initListener(this, (observable, i) -> {
            String typefaceName = ObserverParamMap.staticGetValue(observable , PictureTextItemVM_mTypefaceName);
            mNowTypeface.set(typefaceName);

            MyLog.d(TAG, "initClick", "状态:typefaceName:", "更新typeface" , typefaceName);
        } , CLICK_ITEM);

        initListener(mPictureTextParamDialogVM, (observable, i) -> {
            buildTextColor();
            mTextSize.set(mPictureTextParamDialogVM.getFinalTextSize());
            MyLog.d(TAG, "initClick", "状态:mTextColor:mTextSize:", "重新设置被插入文字的颜色和大小" , mTextColor.get() , mTextSize.get());
        } , STOP_TEXT_PARAM_DIALOG_VM);
    }

    @Override
    protected void initProgressChanged() {
        mUIActionManager
                .<ProgressChangedUIAction>getDefaultThrottleFlowable(PROGRESS_CHANGED_ACTION)
                .map(ProgressChangedUIAction::getProgress)
                .subscribe(progress -> {
                    mSelectParam.set(progress);
                    buildTextColor();
                    MyLog.d(TAG, "initProgressChanged", "状态:", "滑动了");
                });
    }

    private void initTextChanged() {
        mUIActionManager
                .<TextChangedUIAction>getDefaultThrottleFlowable(TEXT_CHANGED_ACTION)
                .filter(textChangedUIAction -> {
                    CharSequence charSequence = textChangedUIAction.getNowText();
                    return charSequence.length() > 0;
                }).subscribe(s -> mEventListenerList.get(TEXT_CHANGE).notifyChange());
    }

    @Override
    public void resume() {
        super.resume();
        mSelectParam.set(PROGRESS_MAX);
        buildTextColor();
    }

    private void buildTextColor() {
        int realAlpha = (int)(mSelectParam.get() * 2.55);
        int nowTextColor = mPictureTextParamDialogVM.getFinalTextColor() - 0xFF000000 + 0x01000000 * realAlpha;
        mTextColor.set(nowTextColor);
        MyLog.d(TAG, "buildTextColor", "状态:progress:nowTextColor:realAlpha:", "滑动了" , mSelectParam.get() , Integer.toHexString(nowTextColor) ,realAlpha);
    }

    public void runInsertText() {
        if (!mNowLimitMaxRect.contains(mEditTextRect)) {
            showToast("被添加的文字超出图片界限，无法插入文字！");
            return;
        }
        if (TextUtils.isEmpty(mText.get())) {
            MyLog.d(TAG, "runInsertText", "状态:", "要插入的文字为空，不需要插入");
            return;
        }

        ObservableField<? extends Object> observableField = mEventListenerList.get(LEAVE_BSBRV_VM_LISTENER);
        observableField.set(null);
        observableField.notifyChange();

        org.opencv.core.Rect opencvRect = new org.opencv.core.Rect();
        opencvRect.x = (int) ((mEditTextRect.left - mNowLimitMaxRect.left) / mZoomCoefficient);
        opencvRect.y = (int) ((mEditTextRect.top - mNowLimitMaxRect.top) / mZoomCoefficient);
        opencvRect.width = (int) ((mEditTextRect.right - mEditTextRect.left) / mZoomCoefficient);
        opencvRect.height = (int) ((mEditTextRect.bottom - mEditTextRect.top) / mZoomCoefficient);

        PictureFrameMyConsumer consumer = new PictureFrameMyConsumer(FONT_EDIT_VIEW_IMAGE , opencvRect);
        MyLog.d(TAG, "runInsertText", "状态:mNowLimitMaxRect:mEditTextRect:mZoomCoefficient:opencvRect:", "插入文字" , mNowLimitMaxRect , mEditTextRect , mZoomCoefficient , opencvRect);

        mStringConsumerChain
                .rxRunNextConvenient(consumer)
                .subscribe(mat -> {
                    mText.set("");
                    mEventListenerList.get(LEAVE_BSBRV_VM_LISTENER).set(ObserverParamMap.staticSet(PictureTextItemVM_mat , mat));
                });

        MyLog.d(TAG, "runInsertText", "状态:mNowLimitMaxRect:mEditTextRect:mZoomCoefficient:opencvRect:", "插入文字" , mNowLimitMaxRect , mEditTextRect , mZoomCoefficient , opencvRect);
    }

    private Rect mNowLimitMaxRect = new Rect();
    private float mZoomCoefficient = 1;
    @Override
    public void onLimitMaxRectChanged(Rect rect , float zoomCoefficient) {
            mNowLimitMaxRect = rect;
            mZoomCoefficient = zoomCoefficient;
            MyLog.d(TAG, "onLimitMaxRectChanged", "状态:cutRect", "图片限制框" , rect);
    }

    private Rect mEditTextRect = new Rect();
    @Override
    public void onPlaceChanged(Rect rect) {
        mEditTextRect.set(rect);
        MyLog.d(TAG, "onPlaceChanged", "状态:mEditTextRect", "文字框发生变化" , mEditTextRect);
    }

    public static class PictureTextItemVM extends ItemBaseVM {
        public static final String TAG = "何时夕:PictureFilterItemVM";

        private boolean isAdd = false;
        public ObservableField<String> mTypefaceName = new ObservableField<>();
        private PictureTextParamDialogVM mPictureTextParamDialogVM;

        public PictureTextItemVM(List<ObservableField<? super Object>> clickItemListenerList , boolean isAdd , Integer position , String typefaceName , PictureTextParamDialogVM pictureTextParamDialogVM) {
            this(clickItemListenerList , position , typefaceName , pictureTextParamDialogVM);
            this.isAdd = isAdd;
        }

        public PictureTextItemVM(List<ObservableField<? super Object>> clickItemListenerList , Integer position , String typefaceName , PictureTextParamDialogVM pictureTextParamDialogVM) {
            super(clickItemListenerList , position);
            initDefaultUIActionManager();

            mPictureTextParamDialogVM = pictureTextParamDialogVM;
            mTypefaceName.set(typefaceName);
            initClick();
        }

        private void initClick() {
            getDefaultClickThrottleFlowable()
                    .filter(clickPosition -> {
                        MyLog.d(TAG, "initClick", "状态:isAdd", "判断当前的item是否是 add" , isAdd);
                        return !isAdd;
                    }).subscribe(clickPosition -> {
                        mPictureTextParamDialogVM.mTypefaceName.set(mTypefaceName.get());
                        ObserverParamMap observerParamMap = getPositionParamMap()
                                .set(PictureTextItemVM_mTypefaceName , mTypefaceName.get())
                                .set(PictureTextItemVM_mPictureTextParamDialogVM , mPictureTextParamDialogVM);
                        mEventListenerList.get(CLICK_ITEM).set(observerParamMap);
                        MyLog.d(TAG, "accept", "状态:observerParamMap:clickPosition:", "" , observerParamMap , clickPosition );
                    });
        }

        @Override
        public String toString() {
            return "PictureFilterItemVM{" +
                    "isAdd=" + isAdd +
                    ", mPosition=" + mPosition +
                    '}';
        }
    }
}
