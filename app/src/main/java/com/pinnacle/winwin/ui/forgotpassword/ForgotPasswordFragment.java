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
import com.pinnacle.winwin.network.model.OtpData;
import com.pinnacle.winwin.network.model.OtpRequest;
import com.pinnacle.winwin.network.model.OtpResponse;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;

import retrofit2.Response;

public class ForgotPasswordFragment extends ASOnlineBaseFragment implements
        View.OnClickListener {

    private static final String TAG = ForgotPasswordFragment.class.getSimpleName();

    private AppCompatEditText editTextMobileNumber;

    private Button btnSendOtp;

    private ProgressBar progressBar;

    private Activity mActivity;

    private AddFragmentListener fragmentListener;
    private OtpData otpData;

    public void setFragmentListener(AddFragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    public static ForgotPasswordFragment newInstance() {

        Bundle args = new Bundle();

        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ForgotPasswordFragment() {

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

        otpData = new OtpData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    //Local Methods
    private void initViews(View view) {

        editTextMobileNumber = view.findViewById(R.id.editTextMobileNumber);
        editTextMobileNumber.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        editTextMobileNumber.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        btnSendOtp = view.findViewById(R.id.btnSendOtp);
        btnSendOtp.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        btnSendOtp.setOnClickListener(this);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

    }

    private boolean validateGenerateOtpForm() {
        if (TextUtils.isEmpty(editTextMobileNumber.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                    getResources().getString(R.string.enter_mobile_number_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else {
            return true;
        }
    }

    /*private void toggleProgressBar(boolean isVisible) {
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }*/

    private void callGenerateOtpApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_GENERATE_OTP_REQUEST, getOtpRequest());
        callAppServer(AppConstant.REQ_API_TYPE_GENERATE_OTP, intent, true);
    }

    private OtpRequest getOtpRequest() {
        OtpRequest otpRequest = new OtpRequest();
        otpRequest.setMobileNo(editTextMobileNumber.getText().toString().trim());

        return otpRequest;
    }

    //Callbacks
    //Api Callback
    @Override
    protected void onSuccess(Object response) {
        /*toggleProgressBar(false);*/
        if (response instanceof OtpResponse) {
            OtpResponse otpResponse = (OtpResponse) response;
            if (otpResponse.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                if (fragmentListener != null) {
                    otpData.setMobileNo(editTextMobileNumber.getText().toString().trim());
                    fragmentListener.addFragmentWithType(AppConstant.FRAGMENT_TYPE_NEW_PASSWORD, otpData);
                }
            }
            /*if (otpResponse.getOtpData() != null) {
                OtpData otpData = otpResponse.getOtpData();
                LogUtils.e(TAG, String.valueOf(otpData.getOtpValue()));
                if (fragmentListener != null) {
                    otpData.setMobileNo(editTextMobileNumber.getText().toString().trim());
                    fragmentListener.addFragmentWithType(AppConstant.FRAGMENT_TYPE_NEW_PASSWORD, otpData);
                }
            }*/

        }

    }

    @Override
    protected void onFailure(Object response) {
        /*toggleProgressBar(false);*/
        if (response instanceof Response) {
            Response mResponse = (Response) response;
            switch (mResponse.code()) {
                case HttpURLConnection.HTTP_FORBIDDEN:
                    Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                            getResources().getString(R.string.something_went_wrong_error),
                            Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    break;
                /*case HttpURLConnection.HTTP_UNAUTHORIZED:
                    Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                            getResources().getString(R.string.invalid_username_password_error),
                            Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
                    break;*/
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
        switch (view.getId()) {
            case R.id.btnSendOtp:
                /*if (fragmentListener != null) {
                    fragmentListener.addFragmentWithType(AppConstant.FRAGMENT_TYPE_VERIFY_OTP);
                }*/
                if (validateGenerateOtpForm()) {
                    callGenerateOtpApi();
//                    if (fragmentListener != null) {
//                        fragmentListener.addFragmentWithType(AppConstant.FRAGMENT_TYPE_VERIFY_OTP, editTextMobileNumber.getText().toString().trim());
//                    }
                }
                break;
        }
    }
}
