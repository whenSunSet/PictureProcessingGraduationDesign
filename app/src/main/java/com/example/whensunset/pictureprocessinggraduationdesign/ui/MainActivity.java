package com.example.whensunset.pictureprocessinggraduationdesign.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil.Utils;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.MainActivityVM;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {
    private com.example.whensunset.pictureprocessinggraduationdesign.ui.MainActivityBinding mMainActivityBinding;
    private MainActivityVM mMainActivityVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainActivityVM = new MainActivityVM();
        mMainActivityBinding.setViewModel(mMainActivityVM);

        uiActionInit();
    }

    public void uiActionInit() {
        // 监听列表中item的点击事件
        mMainActivityVM.mListItemManager.mClickedItemListener.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                String imageUri = (String) Utils.getValueFromObservable(observable , "mImageUri");
                Intent intent = new Intent(MainActivity.this , PictureProcessingActivity.class);
                intent.putExtra("imageUri" , imageUri);
                MainActivity.this.startActivity(intent);

                Log.d("何时夕:MainActivity", ("点击了图片：imageUri:" + imageUri));
            }
        });

        // 监听bar上面的目录切换事件
        mMainActivityVM.mDirectorySpinnerBarVM.mListItemManager.mClickedItemListener.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                String directoryName = (String) Utils.getValueFromObservable(observable , "mDirectoryName");
                mMainActivityVM.mListItemManager.freshPictureList(directoryName);

                Log.d("何时夕:MainActivity", ("切换了目录：directoryName:" + directoryName));
            }
        });
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
//                    Log.i("xuekelindun", OpencvApi.INSTANCE.stringFromJNI());
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

    public Bitmap toGrayByOpencv(Bitmap srcBitmap){
//        Mat mat = new Mat();
//        Utils.bitmapToMat(srcBitmap,mat);
//        Mat grayMat = new Mat();
//        Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGRA2GRAY, 1);
//        Utils.matToBitmap(grayMat,srcBitmap);
        return srcBitmap;
    }
}
