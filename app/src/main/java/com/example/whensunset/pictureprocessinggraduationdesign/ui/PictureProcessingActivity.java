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
import com.example.whensunset.pictureprocessinggraduationdesign.pictureProcessing.StringConsumerChain;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.PictureProcessingActivityVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTextParamDialogVM;

import static com.example.whensunset.pictureprocessinggraduationdesign.base.BaseSeekBarRecycleViewVM.LEAVE_BSBRV_VM_LISTENER;
import static com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ItemManagerBaseVM.CLICK_ITEM;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureTextItemVM_mPictureTextParamDialogVM;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.StaticParam.FONT_EDIT_VIEW_IMAGE;
import static com.example.whensunset.pictureprocessinggraduationdesign.viewModel.PictureProcessingActivityVM.CLICK_BACK;


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

        registeredViewModelFiledsObserver();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void registeredViewModelFiledsObserver() {
        // 监听各种需要显示toast的ViewModel
        showToast(mPictureProcessingActivityVM);
        showToast(mPictureProcessingActivityVM.getPictureParamMenuVM());
        showToast(mPictureProcessingActivityVM.getPictureTransformMenuVM());
        showToast(mPictureProcessingActivityVM.getPictureTextMenuVM());

        // 监听 CLICK_BACK 的点击，以退出当前activity
        initListener(mPictureProcessingActivityVM, (observable, i) -> {
            PictureProcessingActivity.this.finish();
            MyLog.d(TAG, "onPropertyChanged", "状态:", "在activity中监听 CLICK_BACK 的点击");
        } , CLICK_BACK);

        // 监听 PictureText中点击字体列表，以显示dialog
        initListener(mPictureProcessingActivityVM.getPictureTextMenuVM(), (observable, i) -> {
            PictureTextParamDialogVM pictureTextParamDialogVM = ObserverParamMap.staticGetValue(observable , PictureTextItemVM_mPictureTextParamDialogVM);
            PictureTextParamSelectDialog pictureTextParamSelectDialog = new PictureTextParamSelectDialog();
            pictureTextParamSelectDialog.setPictureTextParamDialogVM(pictureTextParamDialogVM);
            pictureTextParamSelectDialog.show(getFragmentManager());
            MyLog.d(TAG, "registeredViewModelFiledsObserver", "状态:pictureTextParamDialogVM:", "监听 PictureText中点击字体列表，以显示dialog" , pictureTextParamDialogVM);
        } , CLICK_ITEM);

        // 监听离开PictureText，以获取当前fontView的图像
        initListener(mPictureProcessingActivityVM.getPictureTextMenuVM(), (observable, i) -> {
            Bitmap bitmap = mPictureProcessingActivityBinding.pictureTextFontEditView.getCacheBitmapFromView();
            String path = FONT_EDIT_VIEW_IMAGE;
            MyUtil.saveBitmap(bitmap , path);
            MyLog.d(TAG, "registeredViewModelFiledsObserver", "状态:path:", "监听离开PictureText，以获取当前fontView的图像" , path);
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
