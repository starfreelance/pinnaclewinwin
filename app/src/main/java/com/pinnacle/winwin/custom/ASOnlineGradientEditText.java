package com.pinnacle.winwin.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

public class ASOnlineGradientEditText extends AppCompatEditText {

    private LinearGradient linearGradient;

    public ASOnlineGradientEditText(Context context) {
        super(context);
    }

    public ASOnlineGradientEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ASOnlineGradientEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        getPaint().setShader(getLinearGradient());
    }

    public LinearGradient getLinearGradient() {
        return linearGradient;
    }

    public void setLinearGradient(LinearGradient linearGradient) {
        this.linearGradient = linearGradient;
    }
}
