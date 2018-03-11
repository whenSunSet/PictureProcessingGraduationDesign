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
    private boolean isInCut = false;

    public PictureProcessingActivityVM(String imageUri) {
        mPictureTransformMenuVM = new PictureTransformMenuVM();
        mPictureParamMenuVM = new PictureParamMenuVM();
        mImageUri = imageUri;
        mImagePath = Uri.parse(imageUri).getPath();

        mStringConsumerChain.init(mImagePath);
        mStringConsumerChain
                .rxRunStartConvenient((BaseMyConsumer) null)
                .subscribe(this::showMat);

        //监听 图片变换 的操作以更新图片
        pictureTransformAction();

        MyLog.d(TAG, "PictureProcessingActivityVM", "imageUri:" , imageUri);
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

    }

    public void clickPictureFilterTab() {
        runCut();
        mSelectTab.set(SELECT_PICTURE_FILTER);
    }

    public void clickPictureTransformTab() {
        isInCut = true;
        mSelectTab.set(SELECT_PICTURE_TRANSFORM);
    }

    public void clickPictureParamTab() {
        runCut();
        mSelectTab.set(SELECT_PICTURE_PARAM);
    }

    public void clickPictureFrameTab() {
        runCut();
        mSelectTab.set(SELECT_PICTURE_FRAME);
    }

    public void clickPictureTextTab() {
        mSelectTab.set(SELECT_PICTURE_TEXT);
    }


    public void clickUndo() {
        mStringConsumerChain
                .rxUndoConvenient()
                .subscribe(this::showMat);
    }

    public void clickRedo() {
        mStringConsumerChain
                .rxRedoConvenient()
                .subscribe(this::showMat);
    }

    private void runCut() {
        if (isInCut) {
            CutMyConsumer cutMyConsumer = new CutMyConsumer(new Rect(0 , 0 , 200 , 200));
            mStringConsumerChain
                    .rxRunNextConvenient(cutMyConsumer)
                    .subscribe(this::showMat);
        }
        isInCut = false;
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

        MyLog.d(TAG, "showMat", "mat:", mat);
    }
}
