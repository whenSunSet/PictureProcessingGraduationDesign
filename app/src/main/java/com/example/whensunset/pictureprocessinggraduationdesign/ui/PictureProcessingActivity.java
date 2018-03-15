package com.example.whensunset.pictureprocessinggraduationdesign.ui;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.os.Bundle;

import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseActivity;
import com.example.whensunset.pictureprocessinggraduationdesign.base.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.PictureProcessingActivityVM;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.PictureProcessingActivityVM.SELECT_PICTURE_FILTER;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.PictureProcessingActivityVM.SELECT_PICTURE_FRAME;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.PictureProcessingActivityVM.SELECT_PICTURE_PARAM;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.PictureProcessingActivityVM.SELECT_PICTURE_TEXT;


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
        // 监听各种需要显示toast的ViewModel
        showToast(mPictureProcessingActivityVM);
        showToast(mPictureProcessingActivityVM.mPictureParamMenuVM);
        showToast(mPictureProcessingActivityVM.mPictureTransformMenuVM);

        // 监听SELECT_PICTURE_FILTER , SELECT_PICTURE_PARAM , SELECT_PICTURE_FRAME , SELECT_PICTURE_TEXT 这几个tab的点击
        Flowable.fromArray(SELECT_PICTURE_FILTER , SELECT_PICTURE_PARAM , SELECT_PICTURE_FRAME , SELECT_PICTURE_TEXT)
                .map((Function<Integer, ObservableField<? super Object>>) integer -> mPictureProcessingActivityVM.getListener(integer))
                .subscribe(observableField -> {
                    observableField.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                        @Override
                        public void onPropertyChanged(Observable observable, int i) {
                            MyLog.d(TAG, "onPropertyChanged", "状态:", "在activity中监听 SELECT_PICTURE_FILTER , SELECT_PICTURE_PARAM , SELECT_PICTURE_FRAME , SELECT_PICTURE_TEXT 这几个tab的点击");
                        }
                    });
                });
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
