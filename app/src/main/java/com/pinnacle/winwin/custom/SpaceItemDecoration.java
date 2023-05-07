package com.pinnacle.winwin.custom;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        // Add top margin only for the first item to avoid double space between items
        /*if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }*/
        /*if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = space + space;
            outRect.right = space;
        } else if (parent.getChildLayoutPosition(view) == parent.getChildCount() - 1) {
            LogUtils.e("POS", String.valueOf(parent.getChildLayoutPosition(view)));
            outRect.left = space;
            outRect.right = space + space;
        } else {
            outRect.left = space;
            outRect.right = space;
        }*/
    }
}
