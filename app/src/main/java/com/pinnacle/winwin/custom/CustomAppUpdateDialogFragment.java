package com.pinnacle.winwin.custom;

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
import com.pinnacle.winwin.listener.CustomAppUpdateDialogListener;
import com.pinnacle.winwin.utils.Utils;

public class CustomAppUpdateDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = CustomAppUpdateDialogFragment.class.getSimpleName();

    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewMessage;

    private Button btnOkay;

    private Activity mActivity;

    private CustomAppUpdateDialogListener customAppUpdateDialogListener;

    public static CustomAppUpdateDialogFragment newInstance() {

        Bundle args = new Bundle();

        CustomAppUpdateDialogFragment fragment = new CustomAppUpdateDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
            customAppUpdateDialogListener = (CustomAppUpdateDialogListener) mActivity;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_app_update, container, false);
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

    private void initViews(View view) {

        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        textViewTitle.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewMessage = view.findViewById(R.id.textViewMessage);
        textViewMessage.setTypeface(Utils.getTypeFaceBodoni72(mActivity));

        btnOkay = view.findViewById(R.id.btnOkay);
        btnOkay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOkay:
                if (customAppUpdateDialogListener != null) {
                    customAppUpdateDialogListener.onClickOkay();
                }
                break;
        }
    }
}
