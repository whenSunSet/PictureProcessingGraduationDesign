package com.example.whensunset.pictureprocessinggraduationdesign.viewModel;

import android.databinding.Observable;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil.MyExceptionOnPropertyChangedCallback;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.CutMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTransformMenuVM_mat;

/**
 * Created by whensunset on 2018/3/5.
 */

public class PictureProcessingActivityVM extends BaseVM {
    public static final String TAG = "何时夕:PictureProcessingActivityVM";

    public static final int SELECT_PICTURE_FILTER = 0;
    public static final int SELECT_PICTURE_TRANSFORM = 1;
    public static final int SELECT_PICTURE_PARAM = 2;
    public static final int SELECT_PICTURE_FRAME = 3;
    public static final int SELECT_PICTURE_TEXT = 4;

    public static final int MENU_MAX_HEIGHT = PictureTransformMenuVM.MENU_HEIGHT;

    public final ObservableField<Bitmap> mImageBitMap = new ObservableField<>();
    public final ObservableField<Integer> mSelectTab = new ObservableField<>(0);
    public final ObservableField<Boolean> mCanUndo = new ObservableField<>(false);
    public final ObservableField<Boolean> mCanRedo = new ObservableField<>(false);
    public final ObservableField<Boolean> isInCut = new ObservableField<>(false);

    public final ObservableField<? super Object> mClickPictureFilterListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureTransformListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureParamListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureFrameListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureTextListener = new ObservableField<>();

    public final ObservableField<? super Object> mClickUndoListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickRedoListener = new ObservableField<>();

    public final PictureTransformMenuVM mPictureTransformMenuVM;
    public final PictureParamMenuVM mPictureParamMenuVM;

    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    private String mImagePath;
    private String mImageUri;

    public PictureProcessingActivityVM(String imageUri) {
        mImageUri = imageUri;
        mImagePath = Uri.parse(imageUri).getPath();

        mPictureTransformMenuVM = new PictureTransformMenuVM();
        mPictureParamMenuVM = new PictureParamMenuVM();

        mStringConsumerChain.init(mImagePath);
        mStringConsumerChain
                .rxRunStartConvenient((BaseMyConsumer) null)
                .subscribe(this::showMat);

        //监听 图片变换 的操作以更新图片
        pictureTransformAction();

        MyLog.d(TAG, "PictureProcessingActivityVM", "imageUri:", imageUri );
    }

    private void pictureTransformAction() {

        // 监听旋转图片
        mPictureTransformMenuVM.mClickPictureRotateListener.addOnPropertyChangedCallback(showMat());

        // 监听水平翻转图片
        mPictureTransformMenuVM.mClickPictureHorizontalFlipListener.addOnPropertyChangedCallback(showMat());

        // 监听垂直翻转图片
        mPictureTransformMenuVM.mClickPictureVerticalFlipListener.addOnPropertyChangedCallback(showMat());

        // 监听剪裁比例变化
        mPictureTransformMenuVM.mClickPictureCutListener.addOnPropertyChangedCallback(showMat());

        MyLog.d(TAG, "pictureTransformAction", "状态:", "监听图像变换代码初始化完毕");
    }

    public void clickPictureFilterTab() {
        mClickPictureFilterListener.notifyChange();
        mSelectTab.set(SELECT_PICTURE_FILTER);

        MyLog.d(TAG, "clickPictureFilterTab", "状态:", "进入滤镜列表");
    }

    public void clickPictureTransformTab() {
        isInCut.set(true);
        mSelectTab.set(SELECT_PICTURE_TRANSFORM);

        MyLog.d(TAG, "clickPictureTransformTab", "状态:", "进入图像转换");
    }

    public void clickPictureParamTab() {
        mClickPictureParamListener.notifyChange();
        mSelectTab.set(SELECT_PICTURE_PARAM);

        MyLog.d(TAG, "clickPictureParamTab", "状态:", "进入图像参数变化");
    }

    public void clickPictureFrameTab() {
        mClickPictureFrameListener.notifyChange();
        mSelectTab.set(SELECT_PICTURE_FRAME);

        MyLog.d(TAG, "clickPictureFrameTab", "状态:", "进入图像边框添加列表");
    }

    public void clickPictureTextTab() {
        mClickPictureTextListener.notifyChange();
        mSelectTab.set(SELECT_PICTURE_TEXT);

        MyLog.d(TAG, "clickPictureFrameTab", "状态:", "进入图像文字添加");
    }


    public void clickUndo() {
        mStringConsumerChain
                .rxUndoConvenient()
                .subscribe(this::showMat);

        MyLog.d(TAG, "clickUndo", "状态:nowCutRect:", "undo完毕" , nowCutRect);
    }

    public void clickRedo() {
        mStringConsumerChain
                .rxRedoConvenient()
                .subscribe(this::showMat);

        MyLog.d(TAG, "clickRedo", "状态:nowCutRect", "redo完毕" , nowCutRect);
    }

    private Rect nowCutRect = new Rect();
    public void runCut(Rect cutRect) {
        MyLog.d(TAG, "runCut", "状态:cutRect", "进入图片剪裁" , cutRect);

        if (isInCut.get() && isImageSizeChanged(cutRect)) {
            nowCutRect = cutRect;
            CutMyConsumer cutMyConsumer = new CutMyConsumer(nowCutRect);
            mStringConsumerChain
                    .rxRunNextConvenient(cutMyConsumer)
                    .subscribe(this::showMat);

            MyLog.d(TAG, "runCut", "状态:nowCutRect:", "进行图片剪裁，并且剪裁完毕" , nowCutRect);
        }
        isInCut.set(false);
    }
    private boolean isImageSizeChanged(Rect cutRect) {
        if ((nowCutRect.width - cutRect.width) <= 2 && (nowCutRect.height - cutRect.height) <= 2) {
            return false;
        }
        return true;
    }

    private MyExceptionOnPropertyChangedCallback showMat() {
        return new MyExceptionOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                showMat(ObserverParamMap.staticGetValue(observable , PictureTransformMenuVM_mat));
            }
        } , e -> mShowToast.set(ObserverParamMap.setToastMessage(e.getMessage())));
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
}
