package com.example.whensunset.pictureprocessinggraduationdesign.viewModel;

import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.itemManagerVM.DirectorySpinnerItemManagerVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.itemManagerVM.PictureItemManagerVM;

/**
 * Created by whensunset on 2018/3/2.
 */

public class MainActivityVM extends BaseVM {
    public final PictureItemManagerVM mPictureItemManager;
    public final DirectorySpinnerItemManagerVM mDirectorySpinnerItemManagerVM;

    public MainActivityVM() {
        mPictureItemManager = new PictureItemManagerVM();
        mDirectorySpinnerItemManagerVM = new DirectorySpinnerItemManagerVM();
    }
}
