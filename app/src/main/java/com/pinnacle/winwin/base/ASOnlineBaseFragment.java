package com.pinnacle.winwin.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.pinnacle.winwin.BuildConfig;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.custom.CustomProgressDialog;
import com.pinnacle.winwin.network.ApiCalls;
import com.pinnacle.winwin.network.RetrofitUtils;
import com.pinnacle.winwin.network.model.ChangePasswordRequest;
import com.pinnacle.winwin.network.model.CountryCodeResponse;
import com.pinnacle.winwin.network.model.ForgotPasswordRequest;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.OtpRequest;
import com.pinnacle.winwin.network.model.OtpResponse;
import com.pinnacle.winwin.utils.NetworkUtils;
import com.pinnacle.winwin.utils.Utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_CHANGE_PASSWORD;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_FORGOT_PASSWORD;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_GENERATE_OTP;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_GET_COUNTRY_CODE_LIST;

public abstract class ASOnlineBaseFragment<T> extends Fragment implements Callback<T> {

    private CustomProgressDialog customProgressDialog;

    private Activity mActivity;

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
    }

    private void showProgressDialog(boolean isVisible, String message) {
        if (isVisible() && isAdded()) {
            if (isVisible) {
                customProgressDialog = new CustomProgressDialog(mActivity);
                customProgressDialog.setCanceledOnTouchOutside(false);
                customProgressDialog.setCancelable(false);
                customProgressDialog.show();
            } else {
                if (customProgressDialog != null) {
                    customProgressDialog.dismiss();
                }
            }
        }

    }

    protected void callAppServer(int requestType, Intent intent, boolean isProgressVisible) {

        if (!NetworkUtils.isNetworkConnected(mActivity)) {
            showInternetError();
            return;
        }

        if (isProgressVisible) {
            showProgressDialog(true, "Please Wait...");
        }

        RetrofitUtils retrofitUtils = RetrofitUtils.getRetrofitInstance(BuildConfig.BASE_URL);
        ApiCalls apiCalls = retrofitUtils.getApiCalls();

        Gson gson;
        String jsonString;
        String requestString;

        switch (requestType) {
            case REQ_API_TYPE_GENERATE_OTP:
                OtpRequest otpRequest = intent.getParcelableExtra(AppConstant.KEY_GENERATE_OTP_REQUEST);
                apiCalls.generateOtp(otpRequest).enqueue((Callback<OtpResponse>) this);
                break;
//            case REQ_API_TYPE_VALIDATE_OTP:
//                OtpRequest validatOtpRequest = intent.getParcelableExtra(AppConstant.KEY_VALIDATE_OTP_REQUEST);
//                apiCalls.validateOtp(validatOtpRequest).enqueue((Callback<OtpResponse>) this);
//                break;
            case REQ_API_TYPE_CHANGE_PASSWORD:
                ChangePasswordRequest changePasswordRequest = intent.getParcelableExtra(AppConstant.KEY_CHANGE_PASSWORD_REQUEST);
                gson = new Gson();
                jsonString = gson.toJson(changePasswordRequest);
                /*requestString = RSAUtility.encrypt(jsonString, RSAUtility.publicKey);*/
                requestString = ASOnlineBaseActivity.getEncryptedString(jsonString);
                /*apiCalls.changePassword(Utils.getHeaderToken(mActivity), Utils.getAsDataRequest(requestString)).enqueue((Callback<GenericResponse>) this);*/
                apiCalls.changePassword(Utils.getHeaderToken(mActivity), changePasswordRequest).enqueue((Callback<GenericResponse>) this);
                break;
            case REQ_API_TYPE_FORGOT_PASSWORD:
                ForgotPasswordRequest forgotPasswordRequest = intent.getParcelableExtra(AppConstant.KEY_FORGOT_PASSWORD_REQUEST);
                apiCalls.forgotPassword(forgotPasswordRequest).enqueue((Callback<GenericResponse>) this);
                break;
            case REQ_API_TYPE_GET_COUNTRY_CODE_LIST:
                apiCalls.getCountryCodeList().enqueue((Callback<CountryCodeResponse>) this);
                break;
        }
    }

    protected abstract <T> void onSuccess(T response);

    protected abstract <T> void onFailure(T response);

    protected abstract void showInternetError();

    //API Callback
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        showProgressDialog(false, "");
        if (response.isSuccessful() && response.body() != null) {
            onSuccess(response.body());
        } else {
            /*onFailure(response);*/
            ResponseBody responseBody = response.errorBody();
            if (responseBody != null) {
                try {
                    Gson gson = new Gson();
                    GenericResponse genericResponse = gson.fromJson(responseBody.string(), GenericResponse.class);
                    onFailure(genericResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(response);
                }
            } else {
                onFailure(response);
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        showProgressDialog(false, "");
        onFailure(t);
    }
}
