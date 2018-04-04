package com.example.whensunset.pictureprocessinggraduationdesign.viewModel;

import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseSeekBarRecycleViewVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ChildBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ItemManagerBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ParentBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureFilterMenuVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureFrameMenuVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTextMenuVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import static com.example.whensunset.pictureprocessinggraduationdesign.base.BaseSeekBarRecycleViewVM.LEAVE_BSBRV_VM_LISTENER;
import static com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ItemManagerBaseVM.CLICK_ITEM;
import static com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView.CUT_MODEL;
import static com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView.INSERT_IMAGE_MODEL;
import static com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView.INSERT_TEXT_MODEL;
import static com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView.SCALE_MODEL;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureFilterItemVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureFrameItemVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureParamMenuVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTextItemVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTransformMenuVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTransformMenuVM_position;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM.PARAM_PROGRESS_CHANGE;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM.SELECT_BRIGHTNESS;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM.SELECT_CONTRAST;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM.SELECT_SATURATION;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM.SELECT_TONAL;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTextMenuVM.TEXT_CHANGE;
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

    public static final int SELECT_PICTURE_FILTER = 0;
    public static final int SELECT_PICTURE_TRANSFORM = 1;
    public static final int SELECT_PICTURE_PARAM = 2;
    public static final int SELECT_PICTURE_FRAME = 3;
    public static final int SELECT_PICTURE_TEXT = 4;
    public static final int CLICK_UNDO = 5;
    public static final int CLICK_REDO = 6;
    public static final int CLICK_BACK = 7;
    public static final int CLICK_YES = 8;
    public static final int CLICK_NO = 9;

    public static final int CHILD_VM_mPictureFilterMenuVM = 0;
    public static final int CHILD_VM_mPictureTransformMenuVM = 1;
    public static final int CHILD_VM_mPictureParamMenuVM = 2;
    public static final int CHILD_VM_mPictureFrameMenuVM = 3;
    public static final int CHILD_VM_mPictureTextMenuVM = 4;

    public final ObservableField<Bitmap> mImageBitMap = new ObservableField<>();
    public final ObservableField<Integer> mSelectTab = new ObservableField<>(0);
    public final ObservableField<Integer> mCutViewModel = new ObservableField<>(SCALE_MODEL);
    public final ObservableField<Boolean> mCanUndo = new ObservableField<>(false);
    public final ObservableField<Boolean> mCanRedo = new ObservableField<>(false);
    public final ObservableField<Boolean> mIsShowYesNo = new ObservableField<>(false);
    public final ObservableField<CutView.OnLimitRectChangedListener> mCutViewListener = new ObservableField<>();
    public final ObservableField<BaseSeekBarRecycleViewVM> mNowBaseSeekBarRecycleViewVM = new ObservableField<>();

    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    private String mImagePath;

    public PictureProcessingActivityVM(String imageUri) {
        super(10);
        initDefaultUIActionManager();

        mImagePath = Uri.parse(imageUri).getPath();
        initChildBaseVM(PictureFilterMenuVM.class , CHILD_VM_mPictureFilterMenuVM);
        initChildBaseVM(PictureTransformMenuVM.class , CHILD_VM_mPictureTransformMenuVM);
        initChildBaseVM(PictureParamMenuVM.class , CHILD_VM_mPictureParamMenuVM);
        initChildBaseVM(PictureFrameMenuVM.class , CHILD_VM_mPictureFrameMenuVM);
        initChildBaseVM(PictureTextMenuVM.class , CHILD_VM_mPictureTextMenuVM);
        mNowBaseSeekBarRecycleViewVM.set(getPictureFilterMenuVM());

        mStringConsumerChain.init(mImagePath);
        mStringConsumerChain.rxRunStartConvenient((BaseMyConsumer) null).subscribe(this::showMat);

        initPictureAction();
        initClick();

        changeNowChildVM(getPictureFilterMenuVM());
        MyLog.d(TAG, "PictureProcessingActivityVM", "imageUri:", imageUri );
    }

    private void initPictureAction() {

        // 监听 图片滤镜 的点击操作
        initListener(getPictureFilterMenuVM() , (observable, i) -> {
            Mat mat = ObserverParamMap.staticGetValue(observable , PictureFilterItemVM_mat);
            if (mat == null) {
                return;
            }
            mIsShowYesNo.set(true);
            showMat(mat);
            MyLog.d(TAG, "initPictureAction", "状态:isShowYesNo:mat:", "" , true , mat);
        }, CLICK_ITEM);

       // 监听 图片变换 的操作以更新图片
        initListener(getPictureTransformMenuVM() ,
                (observable, i) -> showMat(ObserverParamMap.staticGetValue(observable , PictureTransformMenuVM_mat)) ,
                SELECT_PICTURE_ROTATE ,
                SELECT_PICTURE_HORIZONTAL_FLIP ,
                SELECT_PICTURE_VERTICAL_FLIP ,
                SELECT_PICTURE_WHITE_BALANCE ,
                SELECT_PICTURE_CUT ,
                LEAVE_TRANSFORM_LISTENER);

        // 监听 图片参数变化 的操作以更新图片
        initListener(getPictureParamMenuVM() ,
                (observable, i) -> showMat(ObserverParamMap.staticGetValue(observable , PictureParamMenuVM_mat)) ,
                SELECT_BRIGHTNESS ,
                SELECT_CONTRAST ,
                SELECT_SATURATION ,
                SELECT_TONAL ,
                PARAM_PROGRESS_CHANGE);

        // 监听 图片插入 的点击操作
        initListener(getPictureFrameMenuVM(), (observable, i) -> mIsShowYesNo.set(true), CLICK_ITEM);

        // 监听 图片插入 的图片更新
        initListener(getPictureFrameMenuVM(), (observable, i) -> showMat(ObserverParamMap.staticGetValue(observable , PictureFrameItemVM_mat)) , LEAVE_BSBRV_VM_LISTENER);

        // 监听 文字插入 的输入文字操作
        initListener(getPictureTextMenuVM() , (observable, i) -> mIsShowYesNo.set(true), TEXT_CHANGE);

        // 监听 图片插入 的图片更新
        initListener(getPictureTextMenuVM(), (observable, i) -> {
            Mat mat = ObserverParamMap.staticGetValue(observable , PictureTextItemVM_mat);
            if (mat == null) {
                return;
            }
            showMat(mat);
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
                    } else if (position == CLICK_YES) {
                        clickYes();
                        return false;
                    } else if (position == CLICK_NO) {
                        clickNo();
                        return false;
                    }
                    return true;
                }).subscribe(position -> {
                    ChildBaseVM childBaseVM = getChildBaseVM(position);
                    switch (position) {
                        case SELECT_PICTURE_FILTER:
                            mCutViewModel.set(SCALE_MODEL);
                            mNowBaseSeekBarRecycleViewVM.set((BaseSeekBarRecycleViewVM) childBaseVM);

                            break;
                        case SELECT_PICTURE_TRANSFORM:
                            mCutViewListener.set((CutView.OnLimitRectChangedListener) childBaseVM);
                            mCutViewModel.set(CUT_MODEL);

                            break;
                        case SELECT_PICTURE_PARAM:
                            mCutViewModel.set(SCALE_MODEL);

                            break;
                        case SELECT_PICTURE_FRAME:
                            mCutViewListener.set((CutView.OnLimitRectChangedListener) childBaseVM);
                            mCutViewModel.set(INSERT_IMAGE_MODEL);
                            mNowBaseSeekBarRecycleViewVM.set((BaseSeekBarRecycleViewVM) childBaseVM);

                            break;
                        case SELECT_PICTURE_TEXT:
                            mCutViewModel.set(INSERT_TEXT_MODEL);
                            mNowBaseSeekBarRecycleViewVM.set((BaseSeekBarRecycleViewVM) childBaseVM);

                            break;
                    }
            mSelectTab.set(position);
            if (mIsShowYesNo.get()) {
                clickYes();
            }
            changeNowChildVM(childBaseVM);
        });
    }

    private void clickUndo() {
        mStringConsumerChain
                .rxUndoConvenient()
                .subscribe(this::showMat);

        getPictureParamMenuVM().fresh();

        MyLog.d(TAG, "clickUndo", "状态:", "undo完毕");
    }

    private void clickRedo() {
        mStringConsumerChain
                .rxRedoConvenient()
                .subscribe(this::showMat);

        getPictureParamMenuVM().fresh();

        MyLog.d(TAG, "clickRedo", "状态:", "redo完毕");
    }

    private void clickBack(int position) {
        mEventListenerList.get(position).set(ObserverParamMap.staticSet(PictureTransformMenuVM_position , CLICK_BACK));
    }

    private void clickYes() {
        if (mNowChildBaseVM == getPictureFilterMenuVM()) {
            getPictureFilterMenuVM().setRunNow(false);
        } else if (mNowChildBaseVM == getPictureFrameMenuVM()) {
            getPictureFrameMenuVM().runInsertImage();
            getPictureFrameMenuVM().mInsertImagePath.set("");
        } else if (mNowChildBaseVM == getPictureTextMenuVM()) {
            getPictureTextMenuVM().runInsertText();
        }

        if (mNowChildBaseVM instanceof ItemManagerBaseVM) {
            ((ItemManagerBaseVM) mNowChildBaseVM).initSelectedPosition();
        }
        mIsShowYesNo.set(false);
    }

    private void clickNo() {
        if (mNowChildBaseVM == getPictureFilterMenuVM()) {
            getPictureFilterMenuVM().setRunNow(false);
            if (mCanUndo.get()) {
                showMat(mStringConsumerChain.cancelNowConsumer());
            }
        } else if (mNowChildBaseVM == getPictureFrameMenuVM()) {
            getPictureFrameMenuVM().mInsertImagePath.set("");
        } else if (mNowChildBaseVM == getPictureTextMenuVM()) {
            getPictureTextMenuVM().mText.set("");;
        }

        if (mNowChildBaseVM instanceof ItemManagerBaseVM) {
            ((ItemManagerBaseVM) mNowChildBaseVM).initSelectedPosition();
        }
        mIsShowYesNo.set(false);
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

    public PictureFilterMenuVM getPictureFilterMenuVM() {
        return getChildBaseVM(CHILD_VM_mPictureFilterMenuVM);
    }

    public PictureTransformMenuVM getPictureTransformMenuVM() {
        return getChildBaseVM(CHILD_VM_mPictureTransformMenuVM);
    }

    public PictureParamMenuVM getPictureParamMenuVM() {
        return getChildBaseVM(CHILD_VM_mPictureParamMenuVM);
    }

    public PictureFrameMenuVM getPictureFrameMenuVM() {
        return getChildBaseVM(CHILD_VM_mPictureFrameMenuVM);
    }

    public PictureTextMenuVM getPictureTextMenuVM() {
        return getChildBaseVM(CHILD_VM_mPictureTextMenuVM);
    }

}
