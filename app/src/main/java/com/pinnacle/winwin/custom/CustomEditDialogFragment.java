package com.pinnacle.winwin.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.listener.CustomEditDialogListener;
import com.pinnacle.winwin.network.model.CustomerDetailsUpdateRequest;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

public class CustomEditDialogFragment extends DialogFragment implements View.OnClickListener {

    private AppCompatTextView textViewTitle;

    private AppCompatEditText editTextFirstName;
    private AppCompatEditText editTextLastName;

    /*private Button btnDone;*/
    private CustomProgressButtonView btnDone;
    private Button btnCancel;

    private Activity mActivity;

    private String title;

    private CustomEditDialogListener customEditDialogListener;

    public static CustomEditDialogFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString(AppConstant.KEY_DIALOG_TITLE, title);

        CustomEditDialogFragment fragment = new CustomEditDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
            customEditDialogListener = (CustomEditDialogListener) mActivity;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        processBundleArgs(getArguments());
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = null;
        if (window != null) {
            display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);
            window.setLayout((int) (size.x * 0.70), WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_edit, container, false);
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
            LogUtils.e("DIALOG FRAGMENT", "Exception", e);
        }
    }

    //Local Methods
    private void initViews(View view) {

        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        textViewTitle.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        textViewTitle.setText(title);

        editTextFirstName = view.findViewById(R.id.editTextFirstName);
        editTextFirstName.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        editTextFirstName.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        editTextFirstName.setText(ASOnlinePreferenceManager.getString(mActivity, AppConstant.KEY_USER_FIRST_NAME, ""));

        editTextLastName = view.findViewById(R.id.editTextLastName);
        editTextLastName.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        editTextLastName.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        editTextLastName.setText(ASOnlinePreferenceManager.getString(mActivity, AppConstant.KEY_USER_LAST_NAME, ""));

        /*btnDone = view.findViewById(R.id.btnDone);
        btnDone.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        btnDone.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        btnDone.setOnClickListener(this);*/

        btnDone = view.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        btnCancel.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        btnCancel.setOnClickListener(this);
    }

    private void processBundleArgs(Bundle args) {
        if (args != null) {
            if (args.containsKey(AppConstant.KEY_DIALOG_TITLE) &&
                    args.getString(AppConstant.KEY_DIALOG_TITLE) != null) {
                title = args.getString(AppConstant.KEY_DIALOG_TITLE);
            }
        }
    }

    public void toggleProgressBtn(boolean isVisible) {
        if (isVisible) {
            btnDone.startLoading();
        } else {
            btnDone.stopLoading();
        }
    }

    private CustomerDetailsUpdateRequest getCustomerDetailsUpdateRequest() {
        CustomerDetailsUpdateRequest customerDetailsUpdateRequest = new CustomerDetailsUpdateRequest();
        customerDetailsUpdateRequest.setCustId(ASOnlinePreferenceManager.getInteger(mActivity, AppConstant.KEY_USER_CUST_ID, -1));
        customerDetailsUpdateRequest.setFirstName(editTextFirstName.getText().toString().trim());
        customerDetailsUpdateRequest.setLastName(editTextLastName.getText().toString().trim());

        return customerDetailsUpdateRequest;
    }

    //Event Handlers
    //View.OnClickListener
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDone:
                if (customEditDialogListener != null) {
                    customEditDialogListener.onClickDoneButtonListener(getCustomerDetailsUpdateRequest());
                    /*btnDone.startLoading();*/
                }
                break;
            case R.id.btnCancel:
                if (customEditDialogListener != null) {
                    customEditDialogListener.onClickCancelButtonListener();
                }
                break;
        }
    }
}
