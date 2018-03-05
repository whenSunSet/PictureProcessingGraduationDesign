package com.example.whensunset.pictureprocessinggraduationdesign.viewModel;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessinggraduationdesign.BR;
import com.example.whensunset.pictureprocessinggraduationdesign.R;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemContainerVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.BaseItemManager;
import com.example.whensunset.pictureprocessinggraduationdesign.base.IImageUriFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil.ItemViewArg;
import com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil.Utils;
import com.example.whensunset.pictureprocessinggraduationdesign.impl.SystemImageUriFetch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by whensunset on 2018/3/2.
 */

public class MainActivityVM extends BaseItemContainerVM<MainActivityVM.PictureItemVM , MainActivityVM.PictureItemManager> {
    public final ObservableField<Integer> mPictureItemHeight = new ObservableField<>();
    public final ItemViewArg.ItemViewSelector<PictureItemVM> mViewSelector;
    public final DirectorySpinnerBarVM mDirectorySpinnerBarVM;

    public MainActivityVM() {
        mViewSelector = new ItemViewArg.ItemViewSelectorDefault<PictureItemVM>() {
            public void select(ItemViewArg.ItemView itemView, int position, PictureItemVM item) {
                itemView.set(BR.viewModel , R.layout.main_activity_picture_item);
            }
        };
        mListItemManager = new PictureItemManager();
        mDirectorySpinnerBarVM = new DirectorySpinnerBarVM();
        mPictureItemHeight.set(Utils.getDisplayWidth() / 3);
    }

    public static class PictureItemManager extends BaseItemManager<PictureItemVM> {
        public static final int mItemPictureResizeWidth = 100;
        public static final int mItemPictureResizeHeight = 100;
        public final IImageUriFetch mIImageUriFetch;

        public PictureItemManager() {
            mIImageUriFetch = SystemImageUriFetch.getInstance();
            freshPictureList(mIImageUriFetch.getAllImageUriList());
        }

        public void freshPictureList(String directoryName) {
            freshPictureList(mIImageUriFetch.getALlImageUriListFromTag(directoryName));
        }

        public void freshPictureList(List<String> imageUriList) {
            dataItems.clear();
            final int[] nowPosition = {0};
            Flowable.fromIterable(imageUriList)
                    .map(imageUri -> new PictureItemVM(mClickedItemListener, imageUri , nowPosition[0]++))
                    .subscribe(dataItems::add);
        }
    }

    public static class PictureItemVM extends BaseItemVM{
        public final ObservableField<String> mImageUri=new ObservableField<>();
        public final Integer mPosition;
        public PictureItemVM(ObservableField<? super Object> clickedItemListener, String imageUri , Integer position) {
            super(clickedItemListener);
            mImageUri.set(imageUri);
            mPosition = position;
        }

        public void clickPicture() {
            Map<String , Object> valueMap = new HashMap<>();
            valueMap.put("mPosition" , mPosition);
            valueMap.put("mImageUri" , mImageUri.get());
            mClickedItemListener.set(valueMap);
        }
    }
}
