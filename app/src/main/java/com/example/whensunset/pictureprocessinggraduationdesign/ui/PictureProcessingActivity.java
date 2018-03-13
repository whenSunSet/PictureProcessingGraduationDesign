package com.example.whensunset.pictureprocessinggraduationdesign.ui;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;

import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseActivity;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil.MyExceptionOnPropertyChangedCallback;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.PictureProcessingActivityVM;

import org.opencv.core.Rect;


public class PictureProcessingActivity extends BaseActivity {
    public static final String TAG = "何时夕:PictureProcessingActivity";

    private com.example.whensunset.pictureprocessinggraduationdesign.ui.PictureProcessingActivityBinding mPictureProcessingActivityBinding;
    private PictureProcessingActivityVM mPictureProcessingActivityVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPictureProcessingActivityBinding = DataBindingUtil.setContentView(this , R.layout.activity_picture_processing);
        mPictureProcessingActivityVM = new PictureProcessingActivityVM(getIntent().getStringExtra("imageUri"));
        mPictureProcessingActivityBinding.setViewModel(mPictureProcessingActivityVM);


        uiActionInit();
    }

    public void uiActionInit() {
        // 监听最外层VM的toast显示
        mPictureProcessingActivityVM.mShowToast.addOnPropertyChangedCallback(showToast());

        // 监听滤镜tab的点击
        mPictureProcessingActivityVM.mClickPictureFilterListener.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                runCut();
                MyLog.d(TAG, "onPropertyChanged", "状态", "在activity中监听点击 滤镜tab");
            }
        });

        // 监听图片变换tab的点击
        mPictureProcessingActivityVM.mClickPictureTransformListener.addOnPropertyChangedCallback(new MyExceptionOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                MyLog.d(TAG, "onPropertyChanged", "状态", "在activity中监听点击 图片变换tab");
            }
        }, e -> {
            showToast("切换到图片变换失败");
        }));

        // 监听图片参数变换tab的点击
        mPictureProcessingActivityVM.mClickPictureParamListener.addOnPropertyChangedCallback(new MyExceptionOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                runCut();
                MyLog.d(TAG, "onPropertyChanged", "状态:", "在activity中监听点击 图片参数变换tab");
            }
        }, e -> {
            showToast("切换到图片参数变换失败");
        }));

        // 监听添加图片边框tab的点击
        mPictureProcessingActivityVM.mClickPictureFrameListener.addOnPropertyChangedCallback(new MyExceptionOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                runCut();
                MyLog.d(TAG, "onPropertyChanged", "状态:", "在activity中监听点击 添加图片边框tab");
            }
        }, e -> {
            showToast("切换到添加图片边框失败");
        }));

        // 监听添加图片文字tab的点击
        mPictureProcessingActivityVM.mClickPictureTextListener.addOnPropertyChangedCallback(new MyExceptionOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                runCut();
                MyLog.d(TAG, "onPropertyChanged", "状态:", "在activity中监听点击 添加图片文字tab");
            }
        }, e -> {
            showToast("切换到添加图片文字失败");
        }));
    }

    private void runCut() {
        Rect rect = mPictureProcessingActivityBinding.pic.getOpencvCutRect();
        mPictureProcessingActivityVM.runCut(rect);
        MyLog.d(TAG, "runCut", "状态:rect", "在activity中运行 图片剪切" , rect);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StringConsumerChain.getInstance().destroy();
    }
}
