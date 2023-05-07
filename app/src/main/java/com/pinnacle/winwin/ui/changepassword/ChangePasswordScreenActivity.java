package com.pinnacle.winwin.ui.changepassword;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import com.google.android.material.snackbar.Snackbar;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.network.model.ChangePasswordRequest;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.ui.baazaar.BaazaarNewListScreenActivity;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;

public class ChangePasswordScreenActivity extends ASOnlineBaseActivity implements View.OnClickListener {

    private AppCompatEditText editTextOldPassword;
    private AppCompatEditText editTextNewPassword;
    private AppCompatEditText editTextConfirmPassword;

    private AppCompatTextView textViewPasswordHint;

    private Button btnSubmit;

    private View parentLayout;

    private int changePasswordType = -1;
    private String mobileNumber;
    private String oldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_screen);

        processIntentData();
        initViews();
        toggleOldPasswordVisibility();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Local Methods
    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        editTextOldPassword = findViewById(R.id.editTextOldPassword);
        editTextOldPassword.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextOldPassword.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextNewPassword.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextNewPassword.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextConfirmPassword.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextConfirmPassword.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        textViewPasswordHint = findViewById(R.id.textViewPasswordHint);
        textViewPasswordHint.setTypeface(Utils.getTypeFaceBodoni72(this));

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(Utils.getTypeFaceBodoni72(this));
        btnSubmit.setOnClickListener(this);
    }

    private void processIntentData() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstant.KEY_CHANGE_PASSWORD_TYPE)) {
                changePasswordType = getIntent().getIntExtra(AppConstant.KEY_CHANGE_PASSWORD_TYPE, -1);
            }
            if (getIntent().hasExtra(AppConstant.KEY_MOBILE_NUMBER) &&
                    getIntent().getStringExtra(AppConstant.KEY_MOBILE_NUMBER) != null) {
                mobileNumber = getIntent().getStringExtra(AppConstant.KEY_MOBILE_NUMBER);
            }
            if (getIntent().hasExtra(AppConstant.KEY_OLD_PASSWORD) &&
                    getIntent().getStringExtra(AppConstant.KEY_OLD_PASSWORD) != null) {
                oldPassword = getIntent().getStringExtra(AppConstant.KEY_OLD_PASSWORD);
            }
        }
    }

    private void toggleOldPasswordVisibility() {
        if (changePasswordType == AppConstant.CHANGE_PASSWORD_IS_FIRST_TIME) {
            editTextOldPassword.setVisibility(View.GONE);
        } else {
            editTextOldPassword.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateChangePasswordForm() {
        if (editTextOldPassword.getVisibility() == View.VISIBLE &&
                TextUtils.isEmpty(editTextOldPassword.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_old_password_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (TextUtils.isEmpty(editTextNewPassword.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_new_password_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (!Utils.isPasswordValid(editTextNewPassword.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.invalid_password_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (TextUtils.isEmpty(editTextConfirmPassword.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.confirm_password_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (!editTextNewPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.password_match_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else {
            return true;
        }
    }

    private void navigateToBaazaarGridScreen() {
//        Intent intent = new Intent(this, BaazaarGridScreenActivity.class);
        Intent intent = new Intent(this, BaazaarNewListScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void callChangePasswordApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_CHANGE_PASSWORD_REQUEST, getChangePasswordRequest());
        callAppServer(AppConstant.REQ_API_TYPE_CHANGE_PASSWORD, intent, true);
    }

    private ChangePasswordRequest getChangePasswordRequest() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setMobileNo(mobileNumber);
        if (changePasswordType == AppConstant.CHANGE_PASSWORD_IS_FIRST_TIME) {
            changePasswordRequest.setOldPassword(oldPassword);
        } else {
            changePasswordRequest.setOldPassword(editTextOldPassword.getText().toString());
        }
        changePasswordRequest.setNewPassword(editTextNewPassword.getText().toString().trim());

        return changePasswordRequest;
    }

    //Callbacks
    //Api Callback
    @Override
    protected void onSuccess(Object response) {
        if (response instanceof GenericResponse) {
            GenericResponse genericResponse = (GenericResponse) response;
            if (genericResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                if (changePasswordType == 1) {
                    ASOnlinePreferenceManager.saveBoolean(this, AppConstant.KEY_IS_LOGIN, true);
                    navigateToBaazaarGridScreen();
                } else {
                    finish();
                }
            }
        }
    }

    @Override
    protected void onFailure(Object response) {
        if (response instanceof GenericResponse) {
            GenericResponse genericResponse = (GenericResponse) response;
            if (genericResponse.getError() != null && !genericResponse.getError().isEmpty()) {
                Utils.showCustomSnackBarMessageView(this, findViewById(R.id.parentLayout),
                        genericResponse.getError(),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            } else {
                Utils.showCustomSnackBarMessageView(this, findViewById(R.id.parentLayout),
                        getResources().getString(R.string.something_went_wrong_error),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            }
        } else {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.something_went_wrong_error),
                    Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
        }
    }

    @Override
    protected void showInternetError() {
        Utils.showCustomSnackBarMessageView(this, parentLayout,
                getResources().getString(R.string.internet_unavailable_error),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
    }

    //Event Handlers
    //View.OnClickListener
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                if (validateChangePasswordForm()) {
                    callChangePasswordApi();
                }
                break;
        }
    }
}
