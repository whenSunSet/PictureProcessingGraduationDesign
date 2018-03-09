package com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by whensunset on 2018/3/3.
 */

public class ItemDecoration {
    public static RecyclerView.ItemDecoration defaultItemDecoration() {
        return new RecyclerView.ItemDecoration(){
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            }
        };
    }
}
