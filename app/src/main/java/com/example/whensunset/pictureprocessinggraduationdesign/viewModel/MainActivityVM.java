package com.example.whensunset.pictureprocessinggraduationdesign.viewModel;

import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.itemManagerVM.DirectorySpinnerItemManagerVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.itemManagerVM.PictureItemManagerVM;

/**
 * Created by whensunset on 2018/3/2.
 */

public class MainActivityVM extends BaseVM {
    public static final String TAG = "何时夕:MainActivityVM";

    public final PictureItemManagerVM mPictureItemManager;
    public final DirectorySpinnerItemManagerVM mDirectorySpinnerItemManagerVM;

    public MainActivityVM() {
        mPictureItemManager = new PictureItemManagerVM();
        mDirectorySpinnerItemManagerVM = new DirectorySpinnerItemManagerVM();
    }
}
