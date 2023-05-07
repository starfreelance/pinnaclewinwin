package com.pinnacle.winwin.custom;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlineApplication;
import com.pinnacle.winwin.utils.Utils;

public class CustomProgressDialog extends AlertDialog {

    public CustomProgressDialog(@NonNull Context context) {
        super(context);
    }

    protected CustomProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.progress_dialog_custom);

        AppCompatTextView textViewMessage = findViewById(R.id.textViewMessage);
        if (textViewMessage != null) {
            textViewMessage.setTypeface(Utils.getTypeFaceBodoni72(ASOnlineApplication.getInstance().getApplicationContext()));
            textViewMessage.getPaint().setShader(Utils.getTextGradient(new int[]{ASOnlineApplication.getInstance().getApplicationContext().getResources().getColor(R.color.colorStartGold),
                    ASOnlineApplication.getInstance().getApplicationContext().getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        }

    }
}
