package com.example.whensunset.pictureprocessinggraduationdesign.viewModel;

import android.databinding.Observable;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.CutMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureParamMenuVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTransformMenuVM_mat;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM.SELECT_BRIGHTNESS;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM.SELECT_CONTRAST;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM.SELECT_SATURATION;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM.SELECT_TONAL;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM.SELECT_PICTURE_CUT;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM.SELECT_PICTURE_HORIZONTAL_FLIP;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM.SELECT_PICTURE_ROTATE;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM.SELECT_PICTURE_VERTICAL_FLIP;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM.SELECT_PICTURE_WHITE_BALANCE;

/**
 * Created by whensunset on 2018/3/5.
 */

public class PictureProcessingActivityVM extends BaseVM implements CutView.OnLimitRectChangedListener{
    public static final String TAG = "何时夕:PictureProcessingActivityVM";

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
    public final ObservableField<Boolean> mCanUndo = new ObservableField<>(false);
    public final ObservableField<Boolean> mCanRedo = new ObservableField<>(false);
    public final ObservableField<Boolean> isInCut = new ObservableField<>(false);

    public final PictureTransformMenuVM mPictureTransformMenuVM;
    public final PictureParamMenuVM mPictureParamMenuVM;

    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    private String mImagePath;
    private String mImageUri;
    private int mNowSelectListenerPosition = 0;
    private boolean isLeaveCut = false;

    public PictureProcessingActivityVM(String imageUri) {
        super(7);
        mImageUri = imageUri;
        mImagePath = Uri.parse(imageUri).getPath();

        mPictureTransformMenuVM = new PictureTransformMenuVM();
        mPictureParamMenuVM = new PictureParamMenuVM();

        mStringConsumerChain.init(mImagePath);
        mStringConsumerChain
                .rxRunStartConvenient((BaseMyConsumer) null)
                .subscribe(this::showMat);

        initPictureAction();

        MyLog.d(TAG, "PictureProcessingActivityVM", "imageUri:", imageUri );
    }

    private void initPictureAction() {

        // 监听 图片变换 的操作以更新图片
        Flowable.fromArray(SELECT_PICTURE_ROTATE , SELECT_PICTURE_HORIZONTAL_FLIP , SELECT_PICTURE_VERTICAL_FLIP , SELECT_PICTURE_WHITE_BALANCE , SELECT_PICTURE_CUT)
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
    public void onClick(int position) {
        super.onClick(position);

        if (position != CLICK_REDO && position != CLICK_UNDO) {
            mSelectTab.set(position);

            if (mNowSelectListenerPosition == SELECT_PICTURE_TRANSFORM && position != SELECT_PICTURE_TRANSFORM){
                MyLog.d(TAG, "onClick", "状态:mNowSelectListenerPosition:position:",
                        "离开transform，如果剪裁框有变化，那么接下来需要进行图片剪裁" , mNowSelectListenerPosition , position);
                isLeaveCut = true;
            }

            mNowSelectListenerPosition = position;
        }

        switch (position) {
            case CLICK_UNDO:
                clickUndo();
                break;
            case CLICK_REDO:
                clickRedo();
                break;
            case SELECT_PICTURE_FILTER:
                runCut();
                getListener(position).notifyChange();
                break;
            case SELECT_PICTURE_TRANSFORM:
                isInCut.set(true);
                break;
            case SELECT_PICTURE_PARAM:
                runCut();
                mPictureParamMenuVM.onResume();
                getListener(position).notifyChange();
                break;
            case SELECT_PICTURE_FRAME:
                runCut();
                getListener(position).notifyChange();
                break;
            case SELECT_PICTURE_TEXT:
                runCut();
                getListener(position).notifyChange();
                break;
        }

    }

    private void runCut() {
        if (isLeaveCut && isImageSizeChanged(nowCutRect , mStringConsumerChain.getNowRect())) {
            CutMyConsumer cutMyConsumer = new CutMyConsumer(nowCutRect);
            mStringConsumerChain
                    .rxRunNextConvenient(cutMyConsumer)
                    .subscribe(this::showMat);

            isLeaveCut = false;

            MyLog.d(TAG, "runCut", "状态:nowCutRect:mStringConsumerChain.getNowRect()",
                    "离开了transform，而且剪裁框有了变化，所以进行了图片剪裁" , nowCutRect , mStringConsumerChain.getNowRect());
        }
        isInCut.set(false);
    }

    private void clickUndo() {
        mStringConsumerChain
                .rxUndoConvenient()
                .subscribe(this::showMat);

        if (mPictureParamMenuVM.getState() == BaseVM.RESUME) {
            mPictureParamMenuVM.fresh();
        }

        MyLog.d(TAG, "clickUndo", "状态:nowCutRect:", "undo完毕" , nowCutRect);
    }

    private void clickRedo() {
        mStringConsumerChain
                .rxRedoConvenient()
                .subscribe(this::showMat);

        if (mPictureParamMenuVM.getState() == BaseVM.RESUME) {
            mPictureParamMenuVM.fresh();
        }

        MyLog.d(TAG, "clickRedo", "状态:nowCutRect", "redo完毕" , nowCutRect);
    }

    private boolean isImageSizeChanged(Rect nowRect , Rect lastCut) {
        if (Math.abs(lastCut.width - nowRect.width) <= 2 && Math.abs(lastCut.height - nowRect.height) <= 2) {
            return false;
        }
        return true;
    }

    private void showMat(Mat mat) {
        MyLog.d(TAG, "showMat", "状态:mat:", "进行图片展示" , mat);
        if (mat == null) {
            throw new RuntimeException("被展示的mat为null");
        }

        mCanUndo.set(mStringConsumerChain.canUndo());
        mCanRedo.set(mStringConsumerChain.canRedo());
        nowCutRect = mStringConsumerChain.getNowRect();

        Bitmap oldBitmap = mImageBitMap.get();
        if (oldBitmap != null) {
            oldBitmap.recycle();
            oldBitmap = null;
            mImageBitMap.set(null);
        }

        Bitmap newBitMap = Bitmap.createBitmap(mat.cols() , mat.rows() , Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat , newBitMap);
        mImageBitMap.set(newBitMap);

        MyLog.d(TAG, "showMat", "状态:mCanUndo:mCanRedo:nowCutRect:", "图片展示完毕" , mCanUndo.get() , mCanRedo.get() , nowCutRect);
    }

    private Rect nowCutRect = new Rect();
    @Override
    public void onLimitRectChanged(Rect cutRect) {
        if (isImageSizeChanged(cutRect , nowCutRect)) {
            nowCutRect = cutRect;
            MyLog.d(TAG, "onLimitRectChanged", "状态:cutRect", "图片限制框发生改变" , cutRect);
        }
    }
}
