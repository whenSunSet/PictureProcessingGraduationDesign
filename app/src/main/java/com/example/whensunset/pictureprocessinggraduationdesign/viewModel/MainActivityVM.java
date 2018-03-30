package com.example.whensunset.pictureprocessinggraduationdesign.viewModel;

import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ParentBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.itemManagerVM.DirectorySpinnerItemManagerVM;
import com.example.whensunset.pictureprocessinggraduationdesign.viewModel.itemManagerVM.PictureItemManagerVM;

/**
 * Created by whensunset on 2018/3/2.
 */

public class MainActivityVM extends ParentBaseVM {
    public static final String TAG = "何时夕:MainActivityVM";

    public static final int CHILD_VM_PictureItemManagerVM = 0;
    public static final int CHILD_VM_DirectorySpinnerItemManagerVM = 1;

    public MainActivityVM() {
        initChildBaseVM(PictureItemManagerVM.class , CHILD_VM_PictureItemManagerVM);
        initChildBaseVM(DirectorySpinnerItemManagerVM.class , CHILD_VM_DirectorySpinnerItemManagerVM);
    }

    public PictureItemManagerVM getPictureItemManagerVM() {
        return getChildBaseVM(CHILD_VM_PictureItemManagerVM);
    }

    public DirectorySpinnerItemManagerVM getDirectorySpinnerItemManagerVM() {
        return getChildBaseVM(CHILD_VM_DirectorySpinnerItemManagerVM);
    }

}
