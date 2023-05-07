package com.pinnacle.winwin.ui.otp;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.OtpData;
import com.pinnacle.winwin.network.model.OtpRequest;
import com.pinnacle.winwin.network.model.OtpResponse;
import com.pinnacle.winwin.network.model.UserData;
import com.pinnacle.winwin.network.model.ValidateOtpResponse;
import com.pinnacle.winwin.ui.baazaar.BaazaarNewListScreenActivity;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;

public class VerifyOtpScreenActivity extends ASOnlineBaseActivity implements View.OnClickListener {

    private static final String TAG = VerifyOtpScreenActivity.class.getSimpleName();

    private AppCompatTextView textViewOtp;
    private AppCompatTextView textViewResendOtp;
    private AppCompatTextView textViewTimer;

    private AppCompatEditText editTextOtp;

    private Button btnSubmit;

    private View parentLayout;

    private String mobileNumber;
    private int requestType = -1;
    private long leftMilliSeconds = 60 * 1000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp_screen);

        processIntentData();
        initViews();
        callGenerateOtpApi();
        handler = new Handler();
        handler.postDelayed(timerRunnable, 0);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(timerRunnable);
        super.onDestroy();
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        textViewOtp = findViewById(R.id.textViewOtp);
        textViewOtp.setTypeface(Utils.getTypeFaceBodoni72(this));

        editTextOtp = findViewById(R.id.editTextOtp);
        editTextOtp.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextOtp.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        textViewResendOtp = findViewById(R.id.textViewResendOtp);
        textViewResendOtp.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewResendOtp.setOnClickListener(this);

        textViewTimer = findViewById(R.id.textViewTimer);
        textViewTimer.setTypeface(Utils.getTypeFaceBodoni72(this));

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(Utils.getTypeFaceBodoni72(this));
        btnSubmit.setOnClickListener(this);
    }

    private void processIntentData() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstant.KEY_MOBILE_NUMBER) &&
                    getIntent().getStringExtra(AppConstant.KEY_MOBILE_NUMBER) != null) {
                mobileNumber = getIntent().getStringExtra(AppConstant.KEY_MOBILE_NUMBER);
            }
        }
    }

    private boolean validateVerifyOtpForm() {
        if (TextUtils.isEmpty(editTextOtp.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_otp_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else {
            return true;
        }
    }

    private void toggleResendOtpText(boolean isEnabled) {
        if (isEnabled) {
            textViewResendOtp.setEnabled(true);
            textViewResendOtp.setTextColor(getResources().getColor(R.color.colorWhite));
            textViewTimer.setVisibility(View.GONE);
        } else {
            textViewResendOtp.setEnabled(false);
            textViewResendOtp.setTextColor(getResources().getColor(R.color.colorDarkGray));
            textViewTimer.setVisibility(View.VISIBLE);
        }
    }

    private void processResendOtp() {
        LogUtils.d("RESEND", "OTP");
        callGenerateOtpApi();
        toggleResendOtpText(false);
        leftMilliSeconds  = 60 * 1000;
        handler.postDelayed(timerRunnable, 0);
    }

    private void saveUserData(UserData userData) {
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_FIRST_NAME, userData.getFirstName());
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_LAST_NAME, userData.getLastName());
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_MOBILE_NO, userData.getMobileNo());
        ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_POINTS, userData.getPoints());
        ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_CUST_ID, userData.getCustId());
        ASOnlinePreferenceManager.saveString(this, AppConstant.KEY_USER_PROFILE_IMAGE, userData.getProfileImage());
    }

    private void navigateToBaazaarListScreen() {
        Intent intent = new Intent(this, BaazaarNewListScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void callGenerateOtpApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_GENERATE_OTP_REQUEST, getOtpRequest());
        requestType = AppConstant.REQ_API_TYPE_GENERATE_OTP;
        callAppServer(AppConstant.REQ_API_TYPE_GENERATE_OTP, intent, true);
    }

    private OtpRequest getOtpRequest() {
        OtpRequest otpRequest = new OtpRequest();
        otpRequest.setMobileNo(mobileNumber);

        return otpRequest;
    }

    private void callValidateOtpApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_VALIDATE_OTP_REQUEST, getValidateOtpRequest());
        requestType = AppConstant.REQ_API_TYPE_VALIDATE_OTP;
        callAppServer(AppConstant.REQ_API_TYPE_VALIDATE_OTP, intent, true);
    }

    private OtpRequest getValidateOtpRequest() {
        OtpRequest validateOtpRequest = new OtpRequest();
        validateOtpRequest.setMobileNo(mobileNumber);
        validateOtpRequest.setOtpValue(Integer.parseInt(editTextOtp.getText().toString().trim()));

        return validateOtpRequest;
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (leftMilliSeconds == 0) {
                toggleResendOtpText(true);
            } else {
                leftMilliSeconds = leftMilliSeconds - 1000;

                long seconds = leftMilliSeconds / 1000 % 60;
                long minutes = leftMilliSeconds / (60 * 1000) % 60;

                textViewTimer.setText(String.format("%02d : %02d", minutes, seconds));
                handler.postDelayed(this, 1000);
            }
        }
    };

    /**
     * @param response
     */
    @Override
    protected void onSuccess(Object response) {
        if (response instanceof OtpResponse) {
            OtpResponse otpResponse = (OtpResponse) response;
            if (requestType == AppConstant.REQ_API_TYPE_GENERATE_OTP) {
                if (otpResponse.getOtpData() != null) {
                    OtpData otpData = otpResponse.getOtpData();
                    LogUtils.e(TAG, String.valueOf(otpData.getOtpValue()));
                    textViewOtp.setVisibility(View.VISIBLE);
                    textViewOtp.setText(String.format(getResources().getString(R.string.otp_value), String.valueOf(otpData.getOtpValue())));
                }
            }
        } else if (response instanceof ValidateOtpResponse) {
            ValidateOtpResponse validateOtpResponse = (ValidateOtpResponse) response;
            if (validateOtpResponse.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                if (validateOtpResponse.getUserData() != null) {
                    UserData userData = validateOtpResponse.getUserData();
                    saveUserData(userData);
                    ASOnlinePreferenceManager.saveBoolean(this, AppConstant.KEY_IS_LOGIN, true);
                    navigateToBaazaarListScreen();
                }
            }
        }
    }

    /**
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
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSubmit) {
            if (validateVerifyOtpForm()) {
                callValidateOtpApi();
            }
        } else if (id == R.id.textViewResendOtp) {
            processResendOtp();
        }
    }
}