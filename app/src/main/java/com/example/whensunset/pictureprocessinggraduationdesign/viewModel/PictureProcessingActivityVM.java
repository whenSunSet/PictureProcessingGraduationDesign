package com.example.whensunset.pictureprocessinggraduationdesign.viewModel;

import android.databinding.Observable;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil.BindingUtils;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.BaseMyConsumer;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureParamMenuVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTransformMenuVM;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

/**
 * Created by whensunset on 2018/3/5.
 */

public class PictureProcessingActivityVM extends BaseVM {
    public static final int SELECT_PICTURE_FILTER = 0;
    public static final int SELECT_PICTURE_TRANSFORM = 1;
    public static final int SELECT_PICTURE_PARAM = 2;
    public static final int SELECT_PICTURE_FRAME = 3;
    public static final int SELECT_PICTURE_TEXT = 4;

    public final ObservableField<Bitmap> mImageBitMap = new ObservableField<>();
    public final ObservableField<Integer> mSelectTab = new ObservableField<>(0);

    public final ObservableField<? super Object> mClickPictureFilterListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureTransformListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureParamListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureFrameListener = new ObservableField<>();
    public final ObservableField<? super Object> mClickPictureTextListener = new ObservableField<>();

    public static final int MENU_MAX_HEIGHT = PictureTransformMenuVM.MENU_HEIGHT;

    public final PictureTransformMenuVM mPictureTransformMenuVM;
    public final PictureParamMenuVM mPictureParamMenuVM;

    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    private String mImagePath;
    private String mImageUri;
    public PictureProcessingActivityVM(String imageUri) {
        mPictureTransformMenuVM = new PictureTransformMenuVM();
        mPictureParamMenuVM = new PictureParamMenuVM();
        mImageUri = imageUri;
        mImagePath = Uri.parse(imageUri).getPath();
        mStringConsumerChain
                .rxRunStartConvenient(mImagePath , (BaseMyConsumer) null)
                .subscribe(this::showMat);

//        mPictureTransformMenuVM.mClickPictureCutListener.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable observable, int i) {
//                CutMyConsumer cutMyConsumer = new CutMyConsumer(new Rect(0 , 0 , 100 , 100));
//                mStringConsumerChain
//                        .rxRunStart(Uri.parse(imageUri).getPath() , cutMyConsumer)
//                        .observeOn(Schedulers.io())
//                        .subscribeOn(AndroidSchedulers.mainThread())
//                        .subscribe(mat -> {
//                            Bitmap bitmap = Bitmap.createBitmap(mat.cols() , mat.rows() , Bitmap.Config.ARGB_8888);
//                            Utils.matToBitmap(mat , bitmap);
//                            mImageBitMap.set(bitmap);
//                        });
//            }
//        });

        mPictureTransformMenuVM.mClickPictureRotateListener.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                showMat(observable);
            }
        });

        mPictureTransformMenuVM.mClickPictureHorizontalFlipListener.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                showMat(observable);
            }
        });

        mPictureTransformMenuVM.mClickPictureVerticalFlipListener.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                showMat(observable);
            }
        });
    }


    private void showMat(Observable observable) {
        Mat mat = (Mat) BindingUtils.getValueFromObservable(observable , "mat");
        showMat(mat);
    }

    private void showMat(Mat mat) {
        if (mat == null) {
            Log.d("何时夕:PictureProcessingA", ("Mat 为null"));
            return;
        }

        Bitmap oldBitmap = mImageBitMap.get();
        if (oldBitmap != null) {
            oldBitmap.recycle();
            oldBitmap = null;
            mImageBitMap.set(null);
        }

        Bitmap newBitMap = Bitmap.createBitmap(mat.cols() , mat.rows() , Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat , newBitMap);
        mImageBitMap.set(newBitMap);
    }

    public void clickPictureFilterTab() {
        mSelectTab.set(SELECT_PICTURE_FILTER);
    }

    public void clickPictureTransformTab() {
        mSelectTab.set(SELECT_PICTURE_TRANSFORM);
    }

    public void clickPictureParamTab() {
        mSelectTab.set(SELECT_PICTURE_PARAM);
    }

    public void clickPictureFrameTab() {
        mSelectTab.set(SELECT_PICTURE_FRAME);
    }

    public void clickPictureTextTab() {
        mSelectTab.set(SELECT_PICTURE_TEXT);
    }
}
