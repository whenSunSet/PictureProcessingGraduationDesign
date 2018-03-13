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

import com.example.whensunset.pictureprocessinggraduationdesign.base.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.mete.CutView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

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

        ViewGroup.LayoutParams itemParams = new ViewGroup.LayoutParams(itemWidth , itemHeight);
        adapter.setItemLayoutParams(itemParams);
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

    @BindingAdapter("isInCut")
    public static void setIsInCut(CutView cutView , boolean isInCut) {
        MyLog.d(TAG, "buildLimitView", "状态:isInCut:", "进入设置cutView的限制" , isInCut);

        cutView.setInCut(isInCut);
        cutView.setInit(isInCut);
        cutView.post(() -> {
            cutView.invalidate();
        });
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
