package com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyUtil;

/**
 * Created by whensunset on 2018/3/6.
 */

public class LayoutParamAdapter {
    public static final String TAG = "何时夕:LayoutParamAdapter";

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
        if (bindingWidth != 0)layoutParams.width = MyUtil.dip2px(bindingWidth);
        if (bindingHeight != 0)layoutParams.height = MyUtil.dip2px(bindingHeight);
        if ((bindingBottomMargin | bindingTopMargin | bindingLeftMargin | bindingRightMargin) != 0 && (layoutParams instanceof ViewGroup.MarginLayoutParams)){
            ((ViewGroup.MarginLayoutParams)layoutParams).setMargins(MyUtil.dip2px(bindingLeftMargin) , MyUtil.dip2px(bindingTopMargin) , MyUtil.dip2px(bindingRightMargin) , MyUtil.dip2px(bindingBottomMargin));
        }
        MyLog.d(TAG, "setLayoutParam", "状态:view:bindingWidth:bindingHeight:bindingLeftMargin:bindingTopMargin:bindingRightMargin:bindingBottomMargin:layoutParams:",
                "" , view.getClass().getName() , bindingWidth , bindingHeight , bindingLeftMargin , bindingTopMargin , bindingRightMargin , bindingBottomMargin , layoutParams);
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter(value = {
            "bindingWeight" ,
            "bindingGravity"}, requireAll = false)
    public static void setLinearLayoutLayoutParam(View view ,
                                      int bindingWeight ,
                                      int bindingGravity) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (bindingWeight != 0)layoutParams.weight = MyUtil.dip2px(bindingWeight);
        if (bindingGravity != 0)layoutParams.gravity = MyUtil.dip2px(bindingGravity);
        MyLog.d(TAG, "setLinearLayoutLayoutParam", "状态:view:bindingWeight:bindingGravity:layoutParams:",
                bindingWeight , bindingGravity , layoutParams);
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
        if ((bindingBottomPadding | bindingTopPadding | bindingLeftPadding | bindingRightPadding) != 0) {
            view.setPadding(MyUtil.dip2px(bindingLeftPadding) , MyUtil.dip2px(bindingTopPadding) , MyUtil.dip2px(bindingRightPadding) , MyUtil.dip2px(bindingBottomPadding));
        }
        MyLog.d(TAG, "setPadding", "状态:view:bindingLeftPadding:bindingTopPadding:bindingBottomPadding:bindingRightPadding:",
                view.getClass().getName() , bindingLeftPadding , bindingTopPadding , bindingBottomPadding , bindingRightPadding);
    }

}
