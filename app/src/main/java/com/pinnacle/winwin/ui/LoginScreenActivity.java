package com.pinnacle.winwin.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.custom.CustomDownloadDialogFragment;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.LoginRequest;
import com.pinnacle.winwin.network.model.LoginResponse;
import com.pinnacle.winwin.network.model.UserData;
import com.pinnacle.winwin.ui.baazaar.BaazaarNewListScreenActivity;
import com.pinnacle.winwin.ui.changepassword.ChangePasswordScreenActivity;
import com.pinnacle.winwin.ui.forgotpassword.ForgotPasswordScreenActiviy;
import com.pinnacle.winwin.ui.otp.VerifyOtpScreenActivity;
import com.pinnacle.winwin.ui.signup.SignUpScreenActivity;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.PermissionUtils;
import com.pinnacle.winwin.utils.Utils;

public class LoginScreenActivity extends ASOnlineBaseActivity implements
        View.OnClickListener, PermissionUtils.SettingListener {

    private static final String TAG = LoginScreenActivity.class.getSimpleName();

    private AppCompatTextView textViewForgotPassword;
    private AppCompatTextView contactOnWhatsApp;
    private AppCompatTextView textViewSignUp;

    private AppCompatEditText editTextUsername;
    private AppCompatEditText editTextPassword;

    private Button btnLogin;

    private ProgressBar progressBar;

    /*private RelativeLayout layoutForgotPassword;*/
    private ConstraintLayout layoutForgotPassword;

    private View parentLayout;

    private PermissionUtils permissionUtils;
    private boolean neverAskAgain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        permissionUtils = PermissionUtils.sharedInstance();
        initViews();
    }

    //Local Methods
    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextUsername.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextUsername.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextPassword.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setTypeface(Utils.getTypeFaceBodoni72(this));
        btnLogin.setOnClickListener(this);

        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewForgotPassword.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewForgotPassword.setOnClickListener(this);

//        contactOnWhatsApp = findViewById(R.id.contactOnWhatsApp);
//        contactOnWhatsApp.setOnClickListener(this);

        textViewSignUp = findViewById(R.id.textViewSignUp);
        textViewSignUp.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewSignUp.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        /*layoutForgotPassword = findViewById(R.id.layoutForgotPassword);
        layoutForgotPassword.setOnClickListener(this);*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionUtils.REQUEST_PHONE_STATE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callUserLoginApi();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                    permissionUtils.requestReadPhoneState(neverAskAgain, this,
                            parentLayout, this);
                } else {
                    neverAskAgain = true;
                    permissionUtils.requestReadPhoneState(neverAskAgain, this,
                            parentLayout, this);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.REQ_CODE_APP_SETTINGS) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                callUserLoginApi();
            }
        }
    }

    private boolean validateLoginForm() {
        if (TextUtils.isEmpty(editTextUsername.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_username_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (TextUtils.isEmpty(editTextPassword.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_password_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else {
            return true;
        }
    }

    private void navigateToForgotPasswordScreen(int changePasswordType) {
        Intent intent = new Intent(this, ForgotPasswordScreenActiviy.class);
        intent.putExtra(AppConstant.KEY_CHANGE_PASSWORD_TYPE, changePasswordType);
        startActivity(intent);
    }

    private void navigateToBaazaarListScreen() {
//        Intent intent = new Intent(this, BaazaarGridScreenActivity.class);
        Intent intent = new Intent(this, BaazaarNewListScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void navigateToChangePasswordScreen(int changePasswordType) {
        Intent intent = new Intent(this, ChangePasswordScreenActivity.class);
        intent.putExtra(AppConstant.KEY_CHANGE_PASSWORD_TYPE, changePasswordType);
        intent.putExtra(AppConstant.KEY_MOBILE_NUMBER, editTextUsername.getText().toString());
        intent.putExtra(AppConstant.KEY_OLD_PASSWORD, editTextPassword.getText().toString());
        startActivity(intent);
    }

    private void navigateToSignUpScreenActivity() {
        Intent intent = new Intent(this, SignUpScreenActivity.class);
        startActivity(intent);
    }

    private void navigateToVerifyOtpScreen() {
        Intent intent = new Intent(this, VerifyOtpScreenActivity.class);
        intent.putExtra(AppConstant.KEY_MOBILE_NUMBER, editTextUsername.getText().toString().trim());
        startActivity(intent);
    }

    /*private void toggleProgressBar(boolean isVisible) {
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }*/

    private void checkPhoneStatePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionUtils.requestReadPhoneState(neverAskAgain, this,
                        parentLayout, this);
            } else {
                callUserLoginApi();
            }
        } else {
            callUserLoginApi();
        }
    }

    private String getIMEINumber() {
        String IMEINumber = "";
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            IMEINumber = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                IMEINumber = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            } else {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                if (telephonyManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        IMEINumber = telephonyManager.getImei();
                    } else {
                        IMEINumber = telephonyManager.getDeviceId();
                    }
                }
            }*/
        }
        return IMEINumber;
    }

    private void callUserLoginApi() {
        /*toggleProgressBar(true);*/
        if (ASOnlinePreferenceManager.getString(this, AppConstant.KEY_IMEI_NUMBER, "") == null ||
                ASOnlinePreferenceManager.getString(this, AppConstant.KEY_IMEI_NUMBER, "").isEmpty()) {
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_IMEI_NUMBER, getIMEINumber());
        }
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_LOGIN_REQUEST, getLoginRequest());
        callAppServer(AppConstant.REQ_API_TYPE_USER_LOGIN, intent, true);
    }

    private LoginRequest getLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAuthUserId(editTextUsername.getText().toString().trim());
        loginRequest.setAuthUserPassword(editTextPassword.getText().toString().trim());
        loginRequest.setDeviceId(ASOnlinePreferenceManager.getString(this, AppConstant.KEY_IMEI_NUMBER, ""));

        return loginRequest;
    }

    private void saveUserData(UserData userData) {
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_FIRST_NAME, userData.getFirstName());
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_LAST_NAME, userData.getLastName());
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_MOBILE_NO, userData.getMobileNo());
        ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_POINTS, userData.getPoints());
        ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_CUST_ID, userData.getCustId());
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_TOKEN, userData.getUserToken());
        ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_ADMIN_ID, userData.getAdminId());
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_PROFILE_IMAGE, userData.getProfileImage());
    }

    private void showDownloadAppUpdateDialog() {
        CustomDownloadDialogFragment customDownloadDialogFragment = CustomDownloadDialogFragment.newInstance();
        customDownloadDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_DOWNLOAD_APP_UPDATE);
    }

    private void openWhatsApp() {
        String url = "https://api.whatsapp.com/send?phone="+"919967456776"+"&text="+"Hi Admin,\n " +
                "Please create my Username and Password.";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    protected void onSuccess(Object response) {
        /*toggleProgressBar(false);*/
        if (response instanceof LoginResponse) {
            LoginResponse loginResponse = (LoginResponse) response;
            LogUtils.e(TAG, String.valueOf(loginResponse.getStatusCode()));
            if (loginResponse.getUserData() != null) {
                UserData userData = loginResponse.getUserData();
                saveUserData(userData);
                if (userData.isVerified()) {
                    ASOnlinePreferenceManager.saveBoolean(this, AppConstant.KEY_IS_LOGIN, true);
                    navigateToBaazaarListScreen();
                } else {
                    navigateToVerifyOtpScreen();
                }
            }
        }

    }

    @Override
    protected void onFailure(Object response) {
        /*toggleProgressBar(false);*/
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
        /*else if (response instanceof Response) {
            Response mResponse = (Response) response;
            switch (mResponse.code()) {
                default:
                    Utils.showCustomSnackBarMessageView(this, parentLayout,
                            getResources().getString(R.string.something_went_wrong_error),
                            Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    break;
            }
        }*/
    }

    @Override
    protected void showInternetError() {
        Utils.showCustomSnackBarMessageView(this, parentLayout,
                getResources().getString(R.string.internet_unavailable_error),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
    }

    //Event Handlers
    //PermissionUtils.SettingListener
    @Override
    public void openSettingActivity() {
        navigateToAppSettingsScreen();
    }

    //View.OnClickListener
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutForgotPassword:
            case R.id.textViewForgotPassword:
                navigateToForgotPasswordScreen(AppConstant.CHANGE_PASSWORD_WITHOUT_LOGIN);
                break;
            case R.id.btnLogin:
                if (validateLoginForm()) {
                    /*ASOnlinePreferenceManager.saveBoolean(this, AppConstant.KEY_IS_LOGIN, true);
                    ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_WALLET_BALANCE, AppConstant.TEMP_WALLET_BALANCE);
                    navigateToBaazaarListScreen();*/
                    /*callUserLoginApi();*/
                    checkPhoneStatePermission();
                }
                break;
            case R.id.textViewSignUp:
                navigateToSignUpScreenActivity();
                break;
            /*case R.id.contactOnWhatsApp:
//                openWhatsApp();
                navigateToSignUpScreenActivity();
                break;*/
        }

    }
}
