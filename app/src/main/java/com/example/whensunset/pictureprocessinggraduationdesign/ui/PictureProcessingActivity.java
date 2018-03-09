package com.example.whensunset.pictureprocessinggraduationdesign.ui;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.PictureProcessingActivityVM;

public class PictureProcessingActivity extends AppCompatActivity {
    private com.example.whensunset.pictureprocessinggraduationdesign.ui.PictureProcessingActivityBinding mPictureProcessingActivityBinding;
    private PictureProcessingActivityVM mPictureProcessingActivityVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPictureProcessingActivityBinding = DataBindingUtil.setContentView(this , R.layout.activity_picture_processing);
        mPictureProcessingActivityVM = new PictureProcessingActivityVM(getIntent().getStringExtra("imageUri"));
        mPictureProcessingActivityBinding.setViewModel(mPictureProcessingActivityVM);

        mPictureProcessingActivityVM.mPictureTransformMenuVM.mClickPictureCutListener.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Toast.makeText(PictureProcessingActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPictureProcessingActivityBinding.pictureTransformMenu.pictureTransformCutImageView.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PictureProcessingActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mPictureProcessingActivityVM.mImageBitMap.get().recycle();
    }

}
