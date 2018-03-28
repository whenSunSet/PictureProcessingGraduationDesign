package com.example.whensunset.pictureprocessinggraduationdesign.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseActivity;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.MainActivityVM;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivityBinding = DataBindingUtil.setContentView(this , R.layout.activity_main);
        mMainActivityVM = new MainActivityVM();
        mMainActivityBinding.setViewModel(mMainActivityVM);

        uiActionInit();
    }

    public void uiActionInit() {
        // 监听bar上面目录切换时候的toast显示
        showToast(mMainActivityVM.mDirectorySpinnerItemManagerVM);

        // 监听列表中item的点击事件
        BaseVM.initListener(mMainActivityVM.mPictureItemManager , (observable, i) -> {
            String imageUri = ObserverParamMap.staticGetValue(observable , PictureItemManagerVM_mImageUri);
            Intent intent = new Intent(MainActivity.this , PictureProcessingActivity.class);
            intent.putExtra("imageUri" , imageUri);
            MainActivity.this.startActivity(intent);

            MyLog.d(TAG, "onPropertyChanged", "状态:imageUri:", "监听列表中item的点击事件" , imageUri);
        }, CLICK_ITEM);

        // 监听bar上面的目录切换事件
        BaseVM.initListener(mMainActivityVM.mDirectorySpinnerItemManagerVM , (observable, i) -> {
            String directoryName = ObserverParamMap.staticGetValue(observable, DirectorySpinnerItemManagerVM_directoryName);
            mMainActivityVM.mPictureItemManager.freshPictureList(directoryName);
            MyLog.d(TAG, "onPropertyChanged", "状态:directoryName:", "监听bar上面的目录切换事件" , directoryName);
        }, CLICK_ITEM);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
//                    Log.i("xuekelindun", "OpenCV loaded successfully");
//                    Log.i("xuekelindun", OpenCVApi.INSTANCE.stringFromJNI());
//                    ImageView iv_image = findViewById(R.id.image);
//                    Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(
//                            R.mipmap.image)).getBitmap();
//                    int w = bitmap.getWidth(), h = bitmap.getHeight();
//                    int[] pix = new int[w * h];
//                    bitmap.getPixels(pix, 0, w, 0, 0, w, h);
//                    int [] resultPixes= MyClass.gray(pix,w,h);
//                    Bitmap result = Bitmap.createBitmap(w,h, Bitmap.Config.RGB_565);
//                    result.setPixels(resultPixes, 0, w, 0, 0,w, h);
//                    iv_image.setImageBitmap(result);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

}
