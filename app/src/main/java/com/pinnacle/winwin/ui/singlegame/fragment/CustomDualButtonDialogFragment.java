package com.pinnacle.winwin.ui.singlegame.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.ui.singlegame.listener.CustomDualButtonDialgoListener;
import com.pinnacle.winwin.utils.Utils;

public class CustomDualButtonDialogFragment extends DialogFragment implements
        View.OnClickListener {

    private AppCompatTextView textViewTitle;

    private Button btnYes;
    private Button btnNo;

    private Activity mActivity;

    private String message;

    private CustomDualButtonDialgoListener customDualButtonDialgoListener;

    public static CustomDualButtonDialogFragment newInstance(String message) {

        Bundle args = new Bundle();
        args.putString(AppConstant.KEY_DUAL_BTN_DIALOG_MSG, message);

        CustomDualButtonDialogFragment fragment = new CustomDualButtonDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CustomDualButtonDialogFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
            customDualButtonDialgoListener = (CustomDualButtonDialgoListener) mActivity;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        processBundleArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_with_two_buttons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            Log.e("DIALOG FRAGMENT", "Exception", e);
        }
    }

    //Local Methods
    private void initViews(View view) {

        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        textViewTitle.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        textViewTitle.setText(message);

        btnYes = view.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(this);

        btnNo = view.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(this);

    }

    private void processBundleArgs(Bundle args) {
        if (args != null) {
            if (args.containsKey(AppConstant.KEY_DUAL_BTN_DIALOG_MSG) &&
                    args.getString(AppConstant.KEY_DUAL_BTN_DIALOG_MSG) != null) {
                message = args.getString(AppConstant.KEY_DUAL_BTN_DIALOG_MSG);
            }
        }
    }

    //Event Handlers
    //View.OnClickListener
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnYes:
                if (customDualButtonDialgoListener != null) {
                    customDualButtonDialgoListener.onClickYesButtonListener();
                }
                break;
            case R.id.btnNo:
                if (customDualButtonDialgoListener != null) {
                    customDualButtonDialgoListener.onClickNoButtonListener();
                }
                break;
        }
    }
}
