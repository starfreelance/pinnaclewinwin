package com.pinnacle.winwin.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class SyncHorizontalScrollView extends HorizontalScrollView {

    private OnScrollViewListener onScrollViewListener;

    public void setOnScrollViewListener(OnScrollViewListener onScrollViewListener) {
        this.onScrollViewListener = onScrollViewListener;
    }

    public SyncHorizontalScrollView(Context context) {
        super(context);
    }

    public SyncHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SyncHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollViewListener != null) {
            onScrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public interface OnScrollViewListener {
        void onScrollChanged(SyncHorizontalScrollView syncHorizontalScrollView, int l, int t, int oldl, int oldt);
    }
}
