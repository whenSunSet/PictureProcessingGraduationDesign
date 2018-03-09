package com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by whensunset on 2018/3/6.
 */

public class LayoutParamAdapter {

    @BindingAdapter(value = {
            "bindingWidth" ,
            "bindingHeight" ,
            "bindingLeftMargin" ,
            "bindingTopMargin" ,
            "bindingBottomMargin" ,
            "bindingRightMargin"}, requireAll = false)
    public static void setLayoutParam(View view ,
                                      int bindingWidth ,
                                      int bindingHeight ,
                                      int bindingLeftMargin ,
                                      int bindingTopMargin ,
                                      int bindingBottomMargin ,
                                      int bindingRightMargin) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (bindingWidth != 0)layoutParams.width = BindingUtils.dip2px(bindingWidth);
        if (bindingHeight != 0)layoutParams.height = BindingUtils.dip2px(bindingHeight);
        if ((bindingBottomMargin | bindingTopMargin | bindingLeftMargin | bindingRightMargin) != 0 && (layoutParams instanceof ViewGroup.MarginLayoutParams))
            ((ViewGroup.MarginLayoutParams)layoutParams).setMargins(BindingUtils.dip2px(bindingLeftMargin) , BindingUtils.dip2px(bindingTopMargin) , BindingUtils.dip2px(bindingRightMargin) , BindingUtils.dip2px(bindingBottomMargin));
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter(value = {
            "bindingWeight" ,
            "bindingGravity"}, requireAll = false)
    public static void setLinearLayoutLayoutParam(View view ,
                                      int bindingWeight ,
                                      int bindingGravity) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (bindingWeight != 0)layoutParams.weight = BindingUtils.dip2px(bindingWeight);
        if (bindingGravity != 0)layoutParams.gravity = BindingUtils.dip2px(bindingGravity);
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter(value = {
            "bindingLeftPadding" ,
            "bindingTopPadding" ,
            "bindingBottomPadding" ,
            "bindingRightPadding"}, requireAll = false)
    public static void setPadding(View view ,
                                  int bindingLeftPadding ,
                                  int bindingTopPadding ,
                                  int bindingBottomPadding ,
                                  int bindingRightPadding) {
        if ((bindingBottomPadding | bindingTopPadding | bindingLeftPadding | bindingRightPadding) != 0)
            view.setPadding(BindingUtils.dip2px(bindingLeftPadding) , BindingUtils.dip2px(bindingTopPadding) , BindingUtils.dip2px(bindingRightPadding) , BindingUtils.dip2px(bindingBottomPadding));
    }

}
