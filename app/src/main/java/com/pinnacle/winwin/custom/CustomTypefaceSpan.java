package com.pinnacle.winwin.custom;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlineApplication;
import com.pinnacle.winwin.utils.Utils;

/*This class is used to set CustomTypeface for Spannable String*/
public class CustomTypefaceSpan extends TypefaceSpan {

    private Typeface typeface;
    private boolean isShader;

    public CustomTypefaceSpan(String family, Typeface typeface, boolean isShader) {
        super(family);
        this.typeface = typeface;
        this.isShader = isShader;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint);
    }

    private void applyCustomTypeFace(Paint paint) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~typeface.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(typeface);
        if (isShader) {
            paint.setShader(Utils.getTextGradient(new int[]{ASOnlineApplication.getInstance().getApplicationContext().getResources().getColor(R.color.colorStartGold),
                    ASOnlineApplication.getInstance().getApplicationContext().getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        }
    }
}
