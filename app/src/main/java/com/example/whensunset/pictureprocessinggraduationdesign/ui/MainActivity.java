package com.example.whensunset.pictureprocessinggraduationdesign.ui;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseActivity;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.MainActivityVM;
import com.tbruyelle.rxpermissions2.RxPermissions;

import static com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ItemManagerBaseVM.CLICK_ITEM;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.DirectorySpinnerItemManagerVM_directoryName;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureItemManagerVM_mImageUri;

public class MainActivity extends BaseActivity {
    public static final String TAG = "何时夕:MainActivity";

    private com.example.whensunset.pictureprocessinggraduationdesign.ui.MainActivityBinding mMainActivityBinding;
    private MainActivityVM mMainActivityVM;

    static  {
        System.loadLibrary("native-lib");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(
                Manifest.permission.READ_EXTERNAL_STORAGE ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .filter(permission -> permission.granted)
                .subscribe(permission -> {

                    mMainActivityBinding = DataBindingUtil.setContentView(MainActivity.this , R.layout.activity_main);
                    mMainActivityVM = getViewModel(MainActivityVM.class);
                    mMainActivityBinding.setViewModel(mMainActivityVM);

                    registeredViewModelFiledsObserver();
                });
    }

    @Override
    protected void registeredViewModelFiledsObserver() {
        // 监听bar上面目录切换时候的toast显示
        showToast(mMainActivityVM.getDirectorySpinnerItemManagerVM());

        // 监听列表中item的点击事件
        initListener(mMainActivityVM.getPictureItemManagerVM() , (observable, i) -> {
            String imageUri = ObserverParamMap.staticGetValue(observable , PictureItemManagerVM_mImageUri);
            Intent intent = new Intent(MainActivity.this , PictureProcessingActivity.class);
            intent.putExtra("imageUri" , imageUri);
            MainActivity.this.startActivity(intent);

            MyLog.d(TAG, "onPropertyChanged", "状态:imageUri:", "监听列表中item的点击事件" , imageUri);
        } , CLICK_ITEM);

        // 监听bar上面的目录切换事件
        initListener(mMainActivityVM.getDirectorySpinnerItemManagerVM() , (observable, i) -> {
            String directoryName = ObserverParamMap.staticGetValue(observable, DirectorySpinnerItemManagerVM_directoryName);
            mMainActivityVM.getPictureItemManagerVM().freshPictureList(directoryName);
            MyLog.d(TAG, "onPropertyChanged", "状态:directoryName:", "监听bar上面的目录切换事件" , directoryName);
        } , CLICK_ITEM);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainActivityVM.onCleared();
    }

}
