package com.example.whensunset.pictureprocessinggraduationdesign.viewModel;

import android.databinding.Observable;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureFrameMenuVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

import static com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView.CUT_MODEL;
import static com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView.INSERT_IMAGE_MODEL;
import static com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView.SCALE_MODEL;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureParamMenuVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTransformMenuVM_mat;
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

public class PictureProcessingActivityVM extends BaseVM {
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

    public final ObservableField<Bitmap> mImageBitMap = new ObservableField<>();
    public final ObservableField<Integer> mSelectTab = new ObservableField<>(0);
    public final ObservableField<Integer> mCutViewModel = new ObservableField<>(SCALE_MODEL);
    public final ObservableField<Boolean> mCanUndo = new ObservableField<>(false);
    public final ObservableField<Boolean> mCanRedo = new ObservableField<>(false);

    public final PictureTransformMenuVM mPictureTransformMenuVM;
    public final PictureParamMenuVM mPictureParamMenuVM;
    public final PictureFrameMenuVM mPictureFrameMenuVM;
    private BaseVM mNowResumeChildVM = null;

    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    private String mImagePath;
    private String mImageUri;

    public PictureProcessingActivityVM(String imageUri) {
        super(7);
        mImageUri = imageUri;
        mImagePath = Uri.parse(imageUri).getPath();

        mPictureTransformMenuVM = new PictureTransformMenuVM();
        mPictureParamMenuVM = new PictureParamMenuVM();
        mPictureFrameMenuVM = new PictureFrameMenuVM();

        mStringConsumerChain.init(mImagePath);
        mStringConsumerChain
                .rxRunStartConvenient((BaseMyConsumer) null)
                .subscribe(this::showMat);

        initPictureAction();
        initClickAction();

        mNowResumeChildVM = mPictureTransformMenuVM;
        mNowResumeChildVM.onResume();
        MyLog.d(TAG, "PictureProcessingActivityVM", "imageUri:", imageUri );
    }

    private void initPictureAction() {

        // 监听 图片变换 的操作以更新图片
        Flowable.fromArray(SELECT_PICTURE_ROTATE , SELECT_PICTURE_HORIZONTAL_FLIP , SELECT_PICTURE_VERTICAL_FLIP , SELECT_PICTURE_WHITE_BALANCE , SELECT_PICTURE_CUT , LEAVE_TRANSFORM_LISTENER)
                .map((Function<Integer, ObservableField<? super Object>>) mPictureTransformMenuVM::getListener)
                .subscribe(observableField -> observableField.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        showMat(ObserverParamMap.staticGetValue(observable , PictureTransformMenuVM_mat));
                    }
                }));

        // 监听 图片参数变化 的操作以更新图片
        Flowable.fromArray(SELECT_BRIGHTNESS , SELECT_CONTRAST , SELECT_SATURATION , SELECT_TONAL)
                .map((Function<Integer, ObservableField<? super Object>>) mPictureParamMenuVM::getListener)
                .subscribe(observableField -> observableField.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        showMat(ObserverParamMap.staticGetValue(observable , PictureParamMenuVM_mat));
                    }
                }));

        MyLog.d(TAG, "initPictureAction", "状态:", "监听图像变化初始化完毕");

    }

    @Override
    protected void initClickAction() {
        getDefaultClickFlowable(THROTTLE_MILLISECONDS)
                .filter(position -> {
                    if (position == CLICK_UNDO) {
                        clickUndo();
                    } else if (position == CLICK_REDO) {
                        clickRedo();
                    }
                    return (position != CLICK_REDO && position != CLICK_UNDO);
                }).map(position -> {
                        mNowResumeChildVM.onStop();

                        mSelectTab.set(position);
                    return position;
                }).subscribe(position -> {
                    switch (position) {
                        case SELECT_PICTURE_FILTER:
                            mNowResumeChildVM = mPictureTransformMenuVM;
                            mCutViewModel.set(SCALE_MODEL);

                            break;
                        case SELECT_PICTURE_TRANSFORM:
                            mNowResumeChildVM = mPictureTransformMenuVM;
                            mCutViewModel.set(CUT_MODEL);

                            break;
                        case SELECT_PICTURE_PARAM:
                            mNowResumeChildVM = mPictureParamMenuVM;
                            mCutViewModel.set(SCALE_MODEL);

                            break;
                        case SELECT_PICTURE_FRAME:
                            mNowResumeChildVM = mPictureFrameMenuVM;
                            mCutViewModel.set(INSERT_IMAGE_MODEL);

                            break;
                        case SELECT_PICTURE_TEXT:
                            mNowResumeChildVM = mPictureTransformMenuVM;
                            mCutViewModel.set(SCALE_MODEL);

                            break;
                    }
            mNowResumeChildVM.onResume();
            getListener(position).notifyChange();
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
