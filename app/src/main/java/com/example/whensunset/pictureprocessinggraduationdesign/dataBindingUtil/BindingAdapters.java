package com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.whensunset.pictureprocessinggraduationdesign.base.ITypefaceFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyUtil;
import com.example.whensunset.pictureprocessinggraduationdesign.mete.ColorPickerView;
import com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView;
import com.example.whensunset.pictureprocessinggraduationdesign.mete.MoveFrameLayout;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import static com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView.CUT_MODEL;
import static com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView.INSERT_IMAGE_MODEL;
import static com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView.INSERT_TEXT_MODEL;

/**
 * Created by Administrator on 2016/12/6 0006.
 */
public class BindingAdapters {
    public static String TAG = "何时夕:BindingAdapters";

    @BindingAdapter(value = {"itemView", "items","itemAnimator","itemDecor","itemHeight","itemWidth"}, requireAll = false)
    public static <T> void setAdapter(final RecyclerView recyclerView, ItemViewArg<T> arg, final List<T> items, RecyclerView.ItemAnimator animator, RecyclerView.ItemDecoration decor , int itemHeight , int itemWidth) {
        if (arg == null) {
            throw new IllegalArgumentException("itemView must not be null");
        }
        BindingRecyclerViewAdapter<T> adapter = new BindingRecyclerViewAdapter<>(arg);
        if (items!=null)adapter.setItems(items);
        if (animator!=null)recyclerView.setItemAnimator(animator);
        if (decor!=null)recyclerView.addItemDecoration(decor);

        if (itemHeight != 0 || itemWidth != 0) {
            ViewGroup.LayoutParams itemParams = new ViewGroup.LayoutParams(MyUtil.dip2px(itemWidth) , MyUtil.dip2px(itemHeight));
            adapter.setItemLayoutParams(itemParams);
        }
        MyLog.d(TAG, "setAdapter", "状态:itemHeight:itemWidth:", "" , itemHeight , itemWidth);
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManager.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }

    @BindingAdapter("bitmap")
    public static void setBitMap(ImageView view , Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @BindingAdapter("typeface")
    public static void setTypeface(TextView view , String typeface) {
        if (typeface != null ) {
            view.setTypeface(ITypefaceFetch.getTypefaceFromAll(typeface));
            MyLog.d(TAG, "setTypeface", "状态:typeface", "" , typeface);
        }
    }

    @BindingAdapter("textColor")
    public static void setTextColor(TextView view , int textColor) {
        view.setTextColor(textColor);
        MyLog.d(TAG, "setTextColor", "状态:textColor", "" , textColor);
    }

    @BindingAdapter("isWb")
    public static void setIsWB(ColorPickerView colorPickerView, Boolean isWB) {
        colorPickerView.setWB(isWB);
        MyLog.d(TAG, "setIsWB", "状态:isWB", "" , isWB);
    }

    @BindingAdapter(value = {"viewHeight","viewWidth" , "viewLeftMargin" , "viewRightMargin"}, requireAll = false)
    public static void setLayoutParam(View view , int viewHeight , int viewWidth , int viewLeftMargin , int viewRightMargin) {
        if (viewHeight == 0 || viewWidth == 0 || view == null) {
            return;
        }

        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(viewWidth , viewHeight);
        viewParams.leftMargin = viewLeftMargin;
        viewParams.rightMargin = viewRightMargin;
        view.setLayoutParams(viewParams);
    }

    @BindingAdapter("model")
    public static void setModel(CutView cutView , int model) {
        MyLog.d(TAG, "setModel", "状态:model:", "重新设置cutview的 模式" , model);

        cutView.setModel(model);
        if (model == CUT_MODEL || model == INSERT_IMAGE_MODEL || model == INSERT_TEXT_MODEL) {
            MyLog.d(TAG, "setModel", "状态:", "重新进入了 transform 和 frame 和 text 需要重新初始化cutView的限制条件");
            cutView.setInit(true);
        }
        cutView.post(cutView::invalidate);
    }

    @BindingAdapter("insertImagePath")
    public static void setInsertImagePath(CutView cutView , String insertImagePath) {
        MyLog.d(TAG, "setInsertImagePath", "状态:insertImagePath:", "进入设置Cut在frame下的 insertImagePath的设置" , insertImagePath);
        cutView.setInsertImagePath(insertImagePath);
    }

    @BindingAdapter("progressMax")
    public static void setProgressMax(SeekBar seekBar , int progressMax) {
        MyLog.d(TAG, "setProgressMax", "状态:progressMax:", "进入设置progressBar的最大值" , progressMax);
        seekBar.setMax(progressMax);
    }

    @BindingAdapter("selectPosition")
    public static void setSelectPosition(RecyclerView recyclerView , int selectPosition) {
        MyLog.d(TAG, "setSelectPosition", "状态:selectPosition:", "" , selectPosition);
        if (selectPosition >= 0 ){
            recyclerView.smoothScrollToPosition(selectPosition);
        }
    }

    @BindingAdapter("onLimitRectChangedListener")
    public static void setOnLimitRectChangedListener(CutView cutView , CutView.OnLimitRectChangedListener onLimitRectChangedListener) {
        cutView.setOnLimitRectChangedListener(onLimitRectChangedListener);
    }

    @BindingAdapter("onLimitMaxRectChangedListener")
    public static void setOnLimitMaxRectChangedListener(CutView cutView , CutView.OnLimitMaxRectChangeListener onLimitMaxRectChangeListener) {
        cutView.setOnLimitMaxRectChangeListener(onLimitMaxRectChangeListener);
    }

    @BindingAdapter("onPlaceChangedListener")
    public static void setOnPlaceChangedListener(MoveFrameLayout moveFrameLayout , MoveFrameLayout.OnPlaceChangedListener onPlaceChangedListener) {
        moveFrameLayout.setOnPlaceChangedListener(onPlaceChangedListener);
    }

    @BindingAdapter(value = {"resizeWidth" , "resizeHeight" , "imageUri"} , requireAll = false)
    public static void setImageResize(SimpleDraweeView simpleDraweeView , int resizeWidth , int resizeHeight , String imageUri) {
        if (resizeHeight == 0) {
            simpleDraweeView.setImageURI(imageUri);
        } else {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUri))
                    .setResizeOptions(new ResizeOptions(resizeWidth , resizeHeight))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setOldController(simpleDraweeView.getController())
                    .setImageRequest(request)
                    .build();
            simpleDraweeView.setController(controller);
        }
    }

    @BindingConversion
    public static ItemViewArg toItemViewArg(ItemViewArg.ItemView itemView) {
        return ItemViewArg.of(itemView);
    }

    @BindingConversion
    public static ItemViewArg toItemViewArg(ItemViewArg.ItemViewSelector<?> selector) {
        return ItemViewArg.of(selector);
    }

}
