package com.example.whensunset.pictureprocessinggraduationdesign.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.databinding.ActivityPictureProcessingPictureTextParamDialogBinding;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM.PictureTextParamDialogVM;

/**
 * Created by whensunset on 2018/3/26.
 */

public class PictureTextParamSelectDialog extends DialogFragment {
    public static final String TAG = "何时夕:PictureTextParamSelectDialog";

    private PictureTextParamDialogVM mPictureTextParamDialogVM;
    private ActivityPictureProcessingPictureTextParamDialogBinding mActivityPictureProcessingPictureTextParamDialogBinding;

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "ViewDialogFragment");
    }

    public void setPictureTextParamDialogVM(PictureTextParamDialogVM pictureTextParamDialogVM) {
        mPictureTextParamDialogVM = pictureTextParamDialogVM;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mActivityPictureProcessingPictureTextParamDialogBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater() , R.layout.activity_picture_processing_picture_text_param_dialog , null , false);
        mActivityPictureProcessingPictureTextParamDialogBinding.setViewModel(mPictureTextParamDialogVM);
        mPictureTextParamDialogVM.resume();
        builder.setView(mActivityPictureProcessingPictureTextParamDialogBinding.getRoot())
                .setPositiveButton("确定", (dialog, which) -> {
                    mPictureTextParamDialogVM.stop();
                    MyLog.d(TAG, "onClick", "状态:", "更改字体大小和颜色");
                }).setNegativeButton("取消", (dialog, which) -> {
                    MyLog.d(TAG, "onClick", "状态:", "取消更改字体大小和颜色");
                });
        return builder.create();
    }
}
