package com.pinnacle.winwin.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.utils.Utils;

public class CustomProgressButtonView extends RelativeLayout {

    private TextView mTextView;
    private ProgressBar mProgressBar;
    private String mText;
    private int color;

    public CustomProgressButtonView(Context context) {
        super(context);
        init();
    }

    public CustomProgressButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
        init();
    }

    public CustomProgressButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(context, attrs);
        init();
    }

    private void init() {
        setClickable(true);
        setFocusable(true);
        setBackgroundResource(R.drawable.border_gold);
        LayoutParams textViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        LayoutParams progressBarParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        progressBarParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTextView = new AppCompatTextView(getContext());
        mTextView.setLayoutParams(textViewParams);
        mTextView.setText(mText.toUpperCase());
        mTextView.setTypeface(Utils.getTypeFaceBodoni72(getContext()));
        mTextView.getPaint().setShader(Utils.getTextGradient(new int[]{getContext().getResources().getColor(R.color.colorStartGold),
                getContext().getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        mProgressBar = new ProgressBar(getContext());
        mProgressBar.setLayoutParams(progressBarParams);
        mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        mProgressBar.setVisibility(View.INVISIBLE);
        addView(mTextView);
        addView(mProgressBar);

    }

    private void setAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressButtonView, 0, 0);
        try {
            mText = ta.getString(R.styleable.CustomProgressButtonView_text);
            /*color = ta.getColor(R.styleable.CustomProgressButtonView_textColor, 0);*/
        } finally {
            ta.recycle();
        }
    }

    public void startLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.INVISIBLE);
    }

    public void stopLoading() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.VISIBLE);
    }

}
