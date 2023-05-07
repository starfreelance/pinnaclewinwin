package com.pinnacle.winwin.ui.signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.custom.DatePickerDialogFragment;
import com.pinnacle.winwin.listener.DatePickerDialogListener;
import com.pinnacle.winwin.network.model.CountryCodeData;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.SignUpData;
import com.pinnacle.winwin.network.model.SignUpRequest;
import com.pinnacle.winwin.network.model.SignUpResponse;
import com.pinnacle.winwin.ui.otp.VerifyOtpScreenActivity;
import com.pinnacle.winwin.ui.signup.listener.CountryCodeListFragmentListener;
import com.pinnacle.winwin.utils.DateUtils;
import com.pinnacle.winwin.utils.PermissionUtils;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;
import java.util.Date;

public class SignUpScreenActivity extends ASOnlineBaseActivity implements View.OnClickListener,
        DatePickerDialogListener, PermissionUtils.SettingListener, CountryCodeListFragmentListener {

    private static final String TAG = SignUpScreenActivity.class.getSimpleName();

    private AppCompatTextView textViewPasswordHint;

    private AppCompatEditText editTextFirstName;
    private AppCompatEditText editTextLastName;
    private AppCompatEditText editTextCountryCode;
    private AppCompatEditText editTextMobileNumber;
    private AppCompatEditText editTextEmailId;
    private AppCompatEditText editTextDob;
    private AppCompatEditText editTextPassword;
    private AppCompatEditText editTextConfirmPassword;

    private Button btnSignup;

    private View parentLayout;

    private DatePickerDialogFragment datePickerDialogFragment;

    private String selectedDob;

    private PermissionUtils permissionUtils;
    private boolean neverAskAgain = false;
    private String countryCode = AppConstant.DEFAULT_COUNTRY_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        permissionUtils = PermissionUtils.sharedInstance();
        initViews();
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionUtils.REQUEST_PHONE_STATE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callUserSignUpApi();
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
                callUserSignUpApi();
            }
        }
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextFirstName.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextFirstName.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextLastName = findViewById(R.id.editTextLastName);
        editTextLastName.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextLastName.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextCountryCode = findViewById(R.id.editTextCountryCode);
        editTextCountryCode.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextCountryCode.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));
        editTextCountryCode.setOnClickListener(this);
        editTextCountryCode.setText(countryCode);

        editTextMobileNumber = findViewById(R.id.editTextMobileNumber);
        editTextMobileNumber.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextMobileNumber.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextEmailId = findViewById(R.id.editTextEmailId);
        editTextEmailId.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextEmailId.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextDob = findViewById(R.id.editTextDob);
        editTextDob.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextDob.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));
        editTextDob.setOnClickListener(this);

        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextPassword.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextConfirmPassword.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextConfirmPassword.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        textViewPasswordHint = findViewById(R.id.textViewPasswordHint);
        textViewPasswordHint.setTypeface(Utils.getTypeFaceBodoni72(this));

        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setTypeface(Utils.getTypeFaceBodoni72(this));
        btnSignup.setOnClickListener(this);

    }

    private boolean validateSignUpForm() {
        if (TextUtils.isEmpty(editTextFirstName.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_first_name_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (TextUtils.isEmpty(editTextLastName.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_last_name_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (TextUtils.isEmpty(editTextMobileNumber.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_mobile_no_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (editTextMobileNumber.getText().toString().trim().length() < 10) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_valid_mobile_no_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        }
//        else if (editTextEmailId.getText().toString().trim().isEmpty()) {
//            Utils.showCustomSnackBarMessageView(this, parentLayout,
//                    getResources().getString(R.string.enter_email_error), Snackbar.LENGTH_LONG,
//                    getResources().getString(R.string.btn_okay)).show();
//            return false;
//        }
//        else if (!Utils.isEmailValid(editTextEmailId.getText().toString().trim())) {
//            Utils.showCustomSnackBarMessageView(this, parentLayout,
//                    getResources().getString(R.string.enter_valid_email), Snackbar.LENGTH_LONG,
//                    getResources().getString(R.string.btn_okay)).show();
//            return false;
//        }
//        else if (TextUtils.isEmpty(editTextDob.getText().toString().trim())) {
//            Utils.showCustomSnackBarMessageView(this, parentLayout,
//                    getResources().getString(R.string.select_dob_error), Snackbar.LENGTH_LONG,
//                    getResources().getString(R.string.btn_okay)).show();
//            return false;
//        }
        else if (TextUtils.isEmpty(editTextPassword.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_new_password_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (!Utils.isPasswordValid(editTextPassword.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.invalid_password_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (TextUtils.isEmpty(editTextConfirmPassword.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.confirm_password_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (!editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.password_match_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else {
            return true;
        }
    }

    private void checkPhoneStatePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionUtils.requestReadPhoneState(neverAskAgain, this,
                        parentLayout, this);
            } else {
                callUserSignUpApi();
            }
        } else {
            callUserSignUpApi();
        }
    }

    private String getIMEINumber() {
        String IMEINumber = "";
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            IMEINumber = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        return IMEINumber;
    }

    private void saveSignUpData(SignUpData signUpData) {
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_TOKEN, signUpData.getUserToken());
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_MOBILE_NO, signUpData.getMobileNo());
        ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_CUST_ID, signUpData.getCustId());
        ASOnlinePreferenceManager.saveBoolean(this, AppConstant.KEY_USER_IS_VERIFIED, signUpData.isVerified());
    }

    private void navigateToVerifyOtpScreen() {
        Intent intent = new Intent(this, VerifyOtpScreenActivity.class);
        intent.putExtra(AppConstant.KEY_MOBILE_NUMBER, editTextMobileNumber.getText().toString().trim());
        startActivity(intent);
    }

    private void showDatePickerDialogFragment() {
        dismissDatePickerDialog();
        datePickerDialogFragment = new DatePickerDialogFragment();
        datePickerDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_DOB);
    }

    private void dismissDatePickerDialog() {
        if (datePickerDialogFragment != null &&
                datePickerDialogFragment.isVisible()) {
            datePickerDialogFragment.dismissAllowingStateLoss();
            datePickerDialogFragment = null;
        }
    }

    private void showCountryCodeListFragment() {
        CountryCodeListFragment countryCodeListFragment = CountryCodeListFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerLayout, countryCodeListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void callUserSignUpApi() {
        if (ASOnlinePreferenceManager.getString(this, AppConstant.KEY_IMEI_NUMBER, "") == null ||
                ASOnlinePreferenceManager.getString(this, AppConstant.KEY_IMEI_NUMBER, "").isEmpty()) {
            ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_IMEI_NUMBER, getIMEINumber());
        }
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_SIGN_UP_REQUEST, getSignUpRequest());
        callAppServer(AppConstant.REQ_API_TYPE_USER_SIGN_UP, intent, true);
    }

    private SignUpRequest getSignUpRequest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName(editTextFirstName.getText().toString().trim());
        signUpRequest.setLastName(editTextLastName.getText().toString().trim());
        signUpRequest.setDeviceId(ASOnlinePreferenceManager.getString(this, AppConstant.KEY_IMEI_NUMBER, ""));
        signUpRequest.setMobileNo(editTextMobileNumber.getText().toString().trim());
        signUpRequest.setPassword(editTextPassword.getText().toString().trim());
        signUpRequest.setDob("");
//        signUpRequest.setDob(DateUtils.getStringFormattedDate(new Date(), DateUtils.DATE_FORMAT_24_hrs));
//        if (!editTextEmailId.getText().toString().trim().isEmpty()) {
//            signUpRequest.setEmail(editTextEmailId.getText().toString().trim());
            signUpRequest.setEmail(editTextMobileNumber.getText().toString().trim()+"@pinnacle.com");
//        }
        signUpRequest.setCountryCode(countryCode);

        return signUpRequest;
    }

    /**
     *
     * @param response
     */
    @Override
    protected void onSuccess(Object response) {
        if (response instanceof SignUpResponse) {
            SignUpResponse signUpResponse = (SignUpResponse) response;
            if (signUpResponse.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                if (signUpResponse.getSignUpData() != null) {
                    SignUpData signUpData = signUpResponse.getSignUpData();
                    saveSignUpData(signUpData);
                    Log.d(TAG, String.valueOf(signUpData.isVerified()));
                    navigateToVerifyOtpScreen();
                }
            }
        }
    }

    /**
     *
     * @param response
     */
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

    /**
     *
     */
    @Override
    protected void showInternetError() {
        Utils.showCustomSnackBarMessageView(this, parentLayout,
                getResources().getString(R.string.internet_unavailable_error),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
    }

    /**
     *
     * @param selectedDate
     */
    @Override
    public void onDateSelectedListener(Date selectedDate) {
        if (selectedDate != null) {
            selectedDob = DateUtils.getStringFormattedDate(selectedDate, DateUtils.DOB_DATE_FORMAT);
            editTextDob.setText(DateUtils.getStringFormattedDate(selectedDate, DateUtils.DISPLAY_DOB_DATE_FORMAT));
        }
    }

    /**
     *
     */
    @Override
    public void openSettingActivity() {
        navigateToAppSettingsScreen();
    }

    @Override
    public void onCountrySelected(CountryCodeData countryCodeData) {
        if (countryCodeData != null) {
            countryCode = countryCodeData.getCountryCode();
            editTextCountryCode.setText(countryCode);
        }
    }

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSignup) {
            if (validateSignUpForm()) {
                checkPhoneStatePermission();
            }
        } else if (id == R.id.editTextDob) {
            showDatePickerDialogFragment();
        } else if (id == R.id.editTextCountryCode) {
            showCountryCodeListFragment();
        }
    }















/*
*    http://103.150.186.40:3000/auth/signup
*
*
* request = {
* "country_code":"+91",
* "device_id":"9c6f3fb7d48d22df",
* "dob":"2022-12-11 15:49:46",
* "email":"test@gmail.com",
* "first_name":"Test",
* "last_name":"User",
* "mobile_no":"1234567890",
* "password":"Test@1234"}
*
* response = {"statusCode":400,"statusMessage":"FAILED","error":"Validation Error"}
**/












}