package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.itemManagerVM;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessinggraduationdesign.BR;
import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.IImageUriFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.ClickUIAction;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ItemBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ItemManagerBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.SystemImageUriFetch;

import java.util.List;

import io.reactivex.Flowable;

import static com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.UIActionManager.CLICK_ACTION;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.ItemBaseVM_mPosition;
import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.PictureItemManagerVM_mImageUri;

/**
 * Created by whensunset on 2018/3/5.
 */

public class PictureItemManagerVM extends ItemManagerBaseVM<PictureItemManagerVM.PictureItemVM> {
    public static final String TAG = "何时夕:PictureItemManagerVM";

    public static final int ITEM_PICTURE_RESIZE_WIDTH = 100;
    public static final int ITEM_PICTURE_RESIZE_HEIGHT = 100;
    public static final int MENU_ITEM_WIDTH = MyUtil.getDisplayWidthDp() / 3;
    public static final int MENU_ITEM_HEIGHT = MENU_ITEM_WIDTH;

    private final IImageUriFetch mIImageUriFetch;

    public PictureItemManagerVM() {
        super(1 ,BR.viewModel , R.layout.activity_main_picture_item);
        mIImageUriFetch = SystemImageUriFetch.getInstance();
        initItemVM();
    }

    @Override
    protected void initItemVM() {
        freshPictureList(mIImageUriFetch.getAllImageUriList());
    }

    public void freshPictureList(String directoryName) {
        freshPictureList(mIImageUriFetch.getALlImageUriListFromTag(directoryName));
    }

    private void freshPictureList(List<String> imageUriList) {
        mDataItemList.clear();
        final int[] nowPosition = {0};
        Flowable.fromIterable(imageUriList)
                .map(imageUri -> new PictureItemVM(mEventListenerList , nowPosition[0]++ , imageUri))
                .subscribe(mDataItemList::add);
    }

    public static class PictureItemVM extends ItemBaseVM {
        public final ObservableField<String> mImageUri=new ObservableField<>();

        public PictureItemVM(List<ObservableField<? super Object>> clickListenerList, int position , String imageUri ) {
            super(clickListenerList, position);
            initDefaultUIActionManager();

            mImageUri.set(imageUri);
            initClick();
        }

        private void initClick() {
            mUIActionManager
                    .<ClickUIAction>getDefaultThrottleFlowable( 3000 , CLICK_ACTION)
                    .subscribe(clickUIAction -> {
                        ObserverParamMap paramMap = ObserverParamMap.staticSet(ItemBaseVM_mPosition , mPosition);
                        paramMap.set(PictureItemManagerVM_mImageUri , mImageUri.get());
                        mEventListenerList.get(clickUIAction.getLastEventListenerPosition()).set(paramMap);
                        MyLog.d(TAG, "onClick", "状态:clickUIAction:mPosition:mImageUri:"
                                , clickUIAction , mPosition ,  mImageUri.get());
                    });
        }
    }
}
