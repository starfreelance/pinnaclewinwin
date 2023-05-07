package com.pinnacle.winwin.ui.forgotpassword;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseFragment;
import com.pinnacle.winwin.listener.AddFragmentListener;
import com.pinnacle.winwin.network.model.ChangePasswordRequest;
import com.pinnacle.winwin.network.model.ForgotPasswordRequest;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.OtpData;
import com.pinnacle.winwin.network.model.OtpRequest;
import com.pinnacle.winwin.network.model.OtpResponse;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;

import retrofit2.Response;

public class NewPasswordFragment extends ASOnlineBaseFragment implements
        View.OnClickListener {

    private AppCompatTextView textViewOtp;
    private AppCompatTextView textViewPasswordHint;
    private AppCompatTextView textViewResendOtp;
    private AppCompatTextView textViewTimer;

    private AppCompatEditText editTextOtp;
    private AppCompatEditText editTextNewPassword;
    private AppCompatEditText editTextConfirmPassword;

    private Button btnSubmit;

    private ProgressBar progressBar;

    private Activity mActivity;

    private AddFragmentListener fragmentListener;
    private OtpData otpData;
    private String oldPassword;
    private String mobileNumber;
    private long leftMilliSeconds = 60 * 1000;
    private Handler handler;

    public void setFragmentListener(AddFragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    public static NewPasswordFragment newInstance(OtpData otpData) {

        Bundle args = new Bundle();
        args.putParcelable(AppConstant.KEY_OTP_DATA, otpData);

        NewPasswordFragment fragment = new NewPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static NewPasswordFragment newInstance(OtpData otpData, String mobileNumber) {

        Bundle args = new Bundle();
        args.putParcelable(AppConstant.KEY_OTP_DATA, otpData);
        args.putString(AppConstant.KEY_MOBILE_NUMBER, mobileNumber);

        NewPasswordFragment fragment = new NewPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NewPasswordFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
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
        return inflater.inflate(R.layout.fragment_new_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        /*if (otpData != null) {
            textViewOtp.setVisibility(View.VISIBLE);
            textViewOtp.setText("Your OTP is : " + otpData.getOtpValue());
        }*/

        handler = new Handler();
        handler.postDelayed(timerRunnable, 0);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(timerRunnable);
        super.onDestroy();
    }

    //Local Methods
    private void processBundleArgs(Bundle args) {
        if (args != null) {
            if (args.containsKey(AppConstant.KEY_OTP_DATA) &&
                    args.getParcelable(AppConstant.KEY_OTP_DATA) != null) {
                otpData = args.getParcelable(AppConstant.KEY_OTP_DATA);
            }
            /*if (args.containsKey(AppConstant.KEY_OLD_PASSWORD) &&
                    args.getString(AppConstant.KEY_OLD_PASSWORD) != null) {
                oldPassword = args.getString(AppConstant.KEY_OLD_PASSWORD);
            }*/
            if (args.containsKey(AppConstant.KEY_MOBILE_NUMBER) &&
                    args.getString(AppConstant.KEY_MOBILE_NUMBER) != null) {
                mobileNumber = args.getString(AppConstant.KEY_MOBILE_NUMBER);
            }
        }
    }

    private void initViews(View view) {

        textViewOtp = view.findViewById(R.id.textViewOtp);
        textViewOtp.setTypeface(Utils.getTypeFaceBodoni72(mActivity));

        editTextOtp = view.findViewById(R.id.editTextOtp);
        editTextOtp.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        editTextOtp.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);
        editTextNewPassword.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        editTextNewPassword.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);
        editTextConfirmPassword.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        editTextConfirmPassword.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        textViewPasswordHint = view.findViewById(R.id.textViewPasswordHint);
        textViewPasswordHint.setTypeface(Utils.getTypeFaceBodoni72(mActivity));

        textViewResendOtp = view.findViewById(R.id.textViewResendOtp);
        textViewResendOtp.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        textViewResendOtp.setOnClickListener(this);

        textViewTimer = view.findViewById(R.id.textViewTimer);
        textViewTimer.setTypeface(Utils.getTypeFaceBodoni72(mActivity));

        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        btnSubmit.setOnClickListener(this);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
    }

    private boolean validateNewPasswordForm() {
        if (TextUtils.isEmpty(editTextNewPassword.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                    getResources().getString(R.string.enter_new_password_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (!Utils.isPasswordValid(editTextNewPassword.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                    getResources().getString(R.string.invalid_password_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (TextUtils.isEmpty(editTextConfirmPassword.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                    getResources().getString(R.string.confirm_password_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (!editTextNewPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
            Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                    getResources().getString(R.string.password_match_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        }else {
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

    /*private void toggleProgressBar(boolean isVisible) {
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }*/

    private void callChangePasswordApi() {
        /*toggleProgressBar(true);*/
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_CHANGE_PASSWORD_REQUEST, getChangePasswordRequest());
        callAppServer(AppConstant.REQ_API_TYPE_CHANGE_PASSWORD, intent, false);
    }

    private void callForgotPasswordApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_FORGOT_PASSWORD_REQUEST, getForgotPasswordRequest());
        callAppServer(AppConstant.REQ_API_TYPE_FORGOT_PASSWORD, intent, true);
    }

    private void callGenerateOtpApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_GENERATE_OTP_REQUEST, getOtpRequest());
        callAppServer(AppConstant.REQ_API_TYPE_GENERATE_OTP, intent, false);
    }

    private ChangePasswordRequest getChangePasswordRequest() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        /*changePasswordRequest.setMobileNo(otpData.getUserData().getMobileNo());*/
        changePasswordRequest.setMobileNo(mobileNumber);
        changePasswordRequest.setOldPassword(oldPassword);
        changePasswordRequest.setNewPassword(editTextNewPassword.getText().toString().trim());

        return changePasswordRequest;
    }

    private ForgotPasswordRequest getForgotPasswordRequest() {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setMobileNo(otpData.getMobileNo());
        forgotPasswordRequest.setOtpValue(Integer.parseInt(editTextOtp.getText().toString().trim()));
        forgotPasswordRequest.setPassword(editTextNewPassword.getText().toString().trim());

        return forgotPasswordRequest;
    }

    private OtpRequest getOtpRequest() {
        OtpRequest otpRequest = new OtpRequest();
        otpRequest.setMobileNo(otpData.getMobileNo());

        return otpRequest;
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

    //Callbacks
    //Api Callback
    @Override
    protected void onSuccess(Object response) {
        /*toggleProgressBar(false);*/
        if (response instanceof GenericResponse) {
            GenericResponse genericResponse = (GenericResponse) response;
            if (genericResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                if (fragmentListener != null) {
                    fragmentListener.addFragmentWithType(AppConstant.NO_FRAGMENT_TYPE, null);
                }
            }
        } else if (response instanceof OtpResponse) {
            OtpResponse otpResponse = (OtpResponse) response;
            if (otpResponse.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                //TODO LATER
            }
        }
    }

    @Override
    protected void onFailure(Object response) {
        /*toggleProgressBar(false);*/
        if (response instanceof GenericResponse) {
            GenericResponse genericResponse = (GenericResponse) response;
            if (genericResponse.getError() != null && !genericResponse.getError().isEmpty()) {
                Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                        genericResponse.getError(),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            } else {
                Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                        getResources().getString(R.string.something_went_wrong_error),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            }
        } else if (response instanceof Response) {
            Response mResponse = (Response) response;
            switch (mResponse.code()) {
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                            getResources().getString(R.string.something_went_wrong_error),
                            Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    break;
                default:
                    Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                            getResources().getString(R.string.something_went_wrong_error),
                            Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    break;
            }
        }
    }

    @Override
    protected void showInternetError() {
        Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                getResources().getString(R.string.internet_unavailable_error),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
    }

    //Event Handlers
    //View.OnClickListener
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSubmit) {
            if (validateNewPasswordForm()) {
//                    callChangePasswordApi();
                callForgotPasswordApi();
            }
        } else if (id == R.id.textViewResendOtp) {
            LogUtils.d("RESEND", "OTP");
            callGenerateOtpApi();
            toggleResendOtpText(false);
            leftMilliSeconds  = 60 * 1000;
            handler.postDelayed(timerRunnable, 0);
        }
    }
}
