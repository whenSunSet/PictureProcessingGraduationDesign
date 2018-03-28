package com.example.whensunset.pictureprocessinggraduationdesign.ui;

import android.annotation.TargetApi;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseActivity;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.PictureProcessingActivityVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTextParamDialogVM;

import java.util.Objects;

import static com.example.whensunset.pictureprocessinggraduationdesign.base.BaseSeekBarRecycleViewVM.LEAVE_BSBRV_VM_LISTENER;
import static com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ItemManagerBaseVM.CLICK_ITEM;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTextItemVM_mPictureTextParamDialogVM;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTransformMenuVM_position;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.FONT_EDIT_VIEW_IMAGE;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.PictureProcessingActivityVM.CLICK_BACK;
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void uiActionInit() {
        // 监听各种需要显示toast的ViewModel
        showToast(mPictureProcessingActivityVM);
        showToast(mPictureProcessingActivityVM.mPictureParamMenuVM);
        showToast(mPictureProcessingActivityVM.mPictureTransformMenuVM);
        showToast(mPictureProcessingActivityVM.mPictureTextMenuVM);

        // 监听SELECT_PICTURE_FILTER , SELECT_PICTURE_PARAM , SELECT_PICTURE_FRAME , SELECT_PICTURE_TEXT CLICK_BACK这几个tab的点击
        BaseVM.initListener(mPictureProcessingActivityVM, (observable, i) -> {
            Integer position = ObserverParamMap.staticGetValue(observable , PictureTransformMenuVM_position);
            if (Objects.equals(position , CLICK_BACK)) {
                PictureProcessingActivity.this.finish();
            }
            MyLog.d(TAG, "onPropertyChanged", "状态:", "在activity中监听 SELECT_PICTURE_FILTER , SELECT_PICTURE_PARAM , SELECT_PICTURE_FRAME , SELECT_PICTURE_TEXT 这几个tab的点击");
        } , SELECT_PICTURE_FILTER , SELECT_PICTURE_PARAM , SELECT_PICTURE_FRAME , SELECT_PICTURE_TEXT , CLICK_BACK);

        // 监听 PictureText中点击字体列表
        BaseVM.initListener(mPictureProcessingActivityVM.mPictureTextMenuVM, (observable, i) -> {
            PictureTextParamDialogVM pictureTextParamDialogVM = ObserverParamMap.staticGetValue(observable , PictureTextItemVM_mPictureTextParamDialogVM);
            PictureTextParamSelectDialog pictureTextParamSelectDialog = new PictureTextParamSelectDialog();
            pictureTextParamSelectDialog.setPictureTextParamDialogVM(pictureTextParamDialogVM);
            pictureTextParamSelectDialog.show(getFragmentManager());
            MyLog.d(TAG, "uiActionInit", "状态:pictureTextParamDialogVM:", "" , pictureTextParamDialogVM);
        }, CLICK_ITEM);

        // 监听离开PictureText
        BaseVM.initListener(mPictureProcessingActivityVM.mPictureTextMenuVM, (observable, i) -> {
            Bitmap bitmap = mPictureProcessingActivityBinding.pictureTextFontEditView.getCacheBitmapFromView();
            String path = FONT_EDIT_VIEW_IMAGE;
            MyUtil.saveBitmap(bitmap , path);
            MyLog.d(TAG, "uiActionInit", "状态:path:", "" , path);
        }, LEAVE_BSBRV_VM_LISTENER);

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
