package com.example.whensunset.pictureprocessinggraduationdesign.viewModel;

import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseSeekBarRecycleViewVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ParentBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureFrameMenuVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTextMenuVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import static com.example.whensunset.pictureprocessinggraduationdesign.base.BaseSeekBarRecycleViewVM.LEAVE_BSBRV_VM_LISTENER;
import static com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView.CUT_MODEL;
import static com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView.INSERT_IMAGE_MODEL;
import static com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView.INSERT_TEXT_MODEL;
import static com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView.SCALE_MODEL;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureFrameItemVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureParamMenuVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTextItemVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTransformMenuVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTransformMenuVM_position;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureFrameMenuVM.FRAME_PROGRESS_CHANGE;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM.PARAM_PROGRESS_CHANGE;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM.SELECT_BRIGHTNESS;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM.SELECT_CONTRAST;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM.SELECT_SATURATION;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM.SELECT_TONAL;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM.LEAVE_TRANSFORM_LISTENER;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM.SELECT_PICTURE_CUT;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM.SELECT_PICTURE_HORIZONTAL_FLIP;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM.SELECT_PICTURE_ROTATE;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM.SELECT_PICTURE_VERTICAL_FLIP;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM.SELECT_PICTURE_WHITE_BALANCE;

/**
 * Created by whensunset on 2018/3/5.
 */

public class PictureProcessingActivityVM extends ParentBaseVM {
    public static final String TAG = "何时夕:PictureProcessingActivityVM";

    public static final int THROTTLE_MILLISECONDS = 200;
    public static final int MENU_MAX_HEIGHT = PictureTransformMenuVM.MENU_HEIGHT;

    public static final int SELECT_PICTURE_FILTER = 0;
    public static final int SELECT_PICTURE_TRANSFORM = 1;
    public static final int SELECT_PICTURE_PARAM = 2;
    public static final int SELECT_PICTURE_FRAME = 3;
    public static final int SELECT_PICTURE_TEXT = 4;
    public static final int CLICK_UNDO = 5;
    public static final int CLICK_REDO = 6;
    public static final int CLICK_BACK = 7;

    public final ObservableField<Bitmap> mImageBitMap = new ObservableField<>();
    public final ObservableField<Integer> mSelectTab = new ObservableField<>(0);
    public final ObservableField<Integer> mCutViewModel = new ObservableField<>(SCALE_MODEL);
    public final ObservableField<Boolean> mCanUndo = new ObservableField<>(false);
    public final ObservableField<Boolean> mCanRedo = new ObservableField<>(false);
    public final ObservableField<CutView.OnLimitRectChangedListener> mCutViewListener = new ObservableField<>();

    public final PictureTransformMenuVM mPictureTransformMenuVM;
    public final PictureParamMenuVM mPictureParamMenuVM;
    public final PictureFrameMenuVM mPictureFrameMenuVM;
    public final PictureTextMenuVM mPictureTextMenuVM;
    public final ObservableField<BaseSeekBarRecycleViewVM> mNowBaseSeekBarRecycleViewVM = new ObservableField<>();

    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    private String mImagePath;

    public PictureProcessingActivityVM(String imageUri) {
        super(8);
        initDefaultUIActionManager();

        mImagePath = Uri.parse(imageUri).getPath();
        mPictureTransformMenuVM = new PictureTransformMenuVM();
        mPictureParamMenuVM = new PictureParamMenuVM();
        mPictureFrameMenuVM = new PictureFrameMenuVM();
        mPictureTextMenuVM = new PictureTextMenuVM();
        mNowBaseSeekBarRecycleViewVM.set(mPictureFrameMenuVM);

        mStringConsumerChain.init(mImagePath);
        mStringConsumerChain
                .rxRunStartConvenient((BaseMyConsumer) null)
                .subscribe(this::showMat);

        initPictureAction();
        initClick();

        changeNowChildVM(mPictureTransformMenuVM);
        MyLog.d(TAG, "PictureProcessingActivityVM", "imageUri:", imageUri );
    }

    private void initPictureAction() {

        // 监听 图片变换 的操作以更新图片
        BaseVM.initListener(mPictureTransformMenuVM ,
                (observable, i) -> showMat(ObserverParamMap.staticGetValue(observable , PictureTransformMenuVM_mat)) ,
                SELECT_PICTURE_ROTATE ,
                SELECT_PICTURE_HORIZONTAL_FLIP ,
                SELECT_PICTURE_VERTICAL_FLIP ,
                SELECT_PICTURE_WHITE_BALANCE ,
                SELECT_PICTURE_CUT ,
                LEAVE_TRANSFORM_LISTENER);

        // 监听 图片参数变化 的操作以更新图片
        BaseVM.initListener(mPictureParamMenuVM ,
                (observable, i) -> showMat(ObserverParamMap.staticGetValue(observable , PictureParamMenuVM_mat)) ,
                SELECT_BRIGHTNESS ,
                SELECT_CONTRAST ,
                SELECT_SATURATION ,
                SELECT_TONAL ,
                PARAM_PROGRESS_CHANGE);

        // 监听 图片插入变化 的操作以更新图片
        BaseVM.initListener(mPictureFrameMenuVM ,
                (observable, i) -> showMat(ObserverParamMap.staticGetValue(observable , PictureFrameItemVM_mat)) ,
                LEAVE_BSBRV_VM_LISTENER,
                FRAME_PROGRESS_CHANGE);

        // 监听 文字插入变化 的操作以更新图片
        BaseVM.initListener(mPictureTextMenuVM, (observable, i) -> {
            Mat mat = ObserverParamMap.staticGetValue(observable , PictureTextItemVM_mat);
            if (mat != null) {
                showMat(mat);
            }
        }, LEAVE_BSBRV_VM_LISTENER);

        MyLog.d(TAG, "initPictureAction", "状态:", "监听图像变化初始化完毕");
    }

    private void initClick() {
        getDefaultClickThrottleFlowable()
                .filter(position -> {
                    if (position == CLICK_UNDO) {
                        clickUndo();
                        return false;
                    } else if (position == CLICK_REDO) {
                        clickRedo();
                        return false;
                    } else if (position == CLICK_BACK) {
                        clickBack(position);
                        return false;
                    }
                    return true;
                }).map(position -> {
                        mNowChildBaseVM.stop();
                        mSelectTab.set(position);
                    return position;
                }).subscribe(position -> {
                    switch (position) {
                        case SELECT_PICTURE_FILTER:
                            mNowChildBaseVM = mPictureTransformMenuVM;
                            mCutViewModel.set(SCALE_MODEL);
                            mNowBaseSeekBarRecycleViewVM.set(mPictureFrameMenuVM);

                            break;
                        case SELECT_PICTURE_TRANSFORM:
                            mNowChildBaseVM = mPictureTransformMenuVM;
                            mCutViewListener.set(mPictureTransformMenuVM);
                            mCutViewModel.set(CUT_MODEL);

                            break;
                        case SELECT_PICTURE_PARAM:
                            mNowChildBaseVM = mPictureParamMenuVM;
                            mCutViewModel.set(SCALE_MODEL);

                            break;
                        case SELECT_PICTURE_FRAME:
                            mNowChildBaseVM = mPictureFrameMenuVM;
                            mCutViewListener.set(mPictureFrameMenuVM);
                            mCutViewModel.set(INSERT_IMAGE_MODEL);
                            mNowBaseSeekBarRecycleViewVM.set(mPictureFrameMenuVM);

                            break;
                        case SELECT_PICTURE_TEXT:
                            mNowChildBaseVM = mPictureTextMenuVM;
                            mCutViewModel.set(INSERT_TEXT_MODEL);
                            mNowBaseSeekBarRecycleViewVM.set(mPictureTextMenuVM);

                            break;
                    }
            mNowChildBaseVM.resume();
            mEventListenerList.get(position).notifyChange();
        });
    }

    private void clickUndo() {
        mStringConsumerChain
                .rxUndoConvenient()
                .subscribe(this::showMat);

        if (mPictureParamMenuVM.isResume()) {
            mPictureParamMenuVM.fresh();
        }

        MyLog.d(TAG, "clickUndo", "状态:mPictureParamMenuVM.isResume():", "undo完毕" , mPictureParamMenuVM.isResume());
    }

    private void clickRedo() {
        mStringConsumerChain
                .rxRedoConvenient()
                .subscribe(this::showMat);

        if (mPictureParamMenuVM.isResume()) {
            mPictureParamMenuVM.fresh();
        }

        MyLog.d(TAG, "clickRedo", "状态:mPictureParamMenuVM.isResume():", "redo完毕" , mPictureParamMenuVM.isResume());
    }

    private void clickBack(int position) {
        mEventListenerList.get(position).set(ObserverParamMap.staticSet(PictureTransformMenuVM_position , CLICK_BACK));
    }

    private void showMat(Mat mat) {
        MyLog.d(TAG, "showMat", "状态:mat:", "进行图片展示" , mat);
        if (mat == null) {
            throw new RuntimeException("被展示的mat为null");
        }

        mCanUndo.set(mStringConsumerChain.canUndo());
        mCanRedo.set(mStringConsumerChain.canRedo());

        Bitmap oldBitmap = mImageBitMap.get();
        if (oldBitmap != null) {
            oldBitmap.recycle();
            oldBitmap = null;
            mImageBitMap.set(null);
        }

        Bitmap newBitMap = Bitmap.createBitmap(mat.cols() , mat.rows() , Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat , newBitMap);
        mImageBitMap.set(newBitMap);

        MyLog.d(TAG, "showMat", "状态:mCanUndo:mCanRedo:", "图片展示完毕" , mCanUndo.get() , mCanRedo.get());
    }
}
