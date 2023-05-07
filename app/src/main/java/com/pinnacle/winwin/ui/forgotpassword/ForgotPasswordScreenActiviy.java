package com.pinnacle.winwin.ui.forgotpassword;

import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.listener.AddFragmentListener;
import com.pinnacle.winwin.network.model.OtpData;
import com.pinnacle.winwin.ui.baazaar.BaazaarGridScreenActivity;
import com.pinnacle.winwin.utils.Utils;

public class ForgotPasswordScreenActiviy extends ASOnlineBaseActivity implements
        AddFragmentListener {

    private static final String TAG = ForgotPasswordScreenActiviy.class.getSimpleName();

    private View parentLayout;

    private FragmentTransaction fragmentTransaction;

    private OtpData otpData;
    private int changePasswordType = -1;
    private String oldPassword;
    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen_activiy);

        processIntentData();
        initViews();
        addForgotPasswordFragment();
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);
    }

    @Override
    protected void onFailure(Object response) {

    }

    @Override
    protected void onSuccess(Object response) {

    }

    @Override
    protected void showInternetError() {
        Utils.showCustomSnackBarMessageView(this, parentLayout,
                getResources().getString(R.string.internet_unavailable_error),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    //Local Methods
    private void processIntentData() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstant.KEY_CHANGE_PASSWORD_TYPE)) {
                changePasswordType = getIntent().getIntExtra(AppConstant.KEY_CHANGE_PASSWORD_TYPE, -1);
            }
        }

    }

    private void addForgotPasswordFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ForgotPasswordFragment forgotPasswordFragment = ForgotPasswordFragment.newInstance();
        forgotPasswordFragment.setFragmentListener(this);
        fragmentTransaction.add(R.id.container, forgotPasswordFragment, ForgotPasswordFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    private void addVerifyOtpFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        VerifyOtpFragment verifyOtpFragment = VerifyOtpFragment.newInstance(otpData);
        verifyOtpFragment.setFragmentListener(this);
        fragmentTransaction.add(R.id.container, verifyOtpFragment, VerifyOtpFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addNewPasswordFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        /*NewPasswordFragment newPasswordFragment = NewPasswordFragment.newInstance(otpData);*/
        NewPasswordFragment newPasswordFragment = NewPasswordFragment.newInstance(otpData, mobileNumber);
        newPasswordFragment.setFragmentListener(this);
        fragmentTransaction.add(R.id.container, newPasswordFragment, NewPasswordFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void navigateToBaazaarListScreen() {
        Intent intent = new Intent(this, BaazaarGridScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Event Handlers
    //AddFragmentListener
    @Override
    public void addFragmentWithType(int fragmentType, Object data) {
        switch (fragmentType) {
            case AppConstant.FRAGMENT_TYPE_VERIFY_OTP:
                /*if (data instanceof OtpData) {
                    otpData = (OtpData) data;
                }*/
                if (data instanceof String) {
                    mobileNumber = (String) data;
                }
                addVerifyOtpFragment();
                break;
            case AppConstant.FRAGMENT_TYPE_NEW_PASSWORD:
                /*if (data instanceof String) {
                    oldPassword = (String) data;
                }*/
                if (data instanceof OtpData) {
                    otpData = (OtpData) data;
                }
                addNewPasswordFragment();
                break;
            case AppConstant.NO_FRAGMENT_TYPE:
                if (changePasswordType == 1) {
                    ASOnlinePreferenceManager.saveBoolean(this, AppConstant.KEY_IS_LOGIN, true);
                    navigateToBaazaarListScreen();
                } else {
                    finish();
                }
                break;
        }
    }
}
