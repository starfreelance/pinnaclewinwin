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

public class VerifyOtpFragment extends ASOnlineBaseFragment implements
        View.OnClickListener {

    private AppCompatTextView textViewOtp;

    private AppCompatEditText editTextOtp;

    private Button btnVerifyOtp;

    private ProgressBar progressBar;

    private Activity mActivity;
    private AddFragmentListener fragmentListener;
    private OtpData otpData;

    public void setFragmentListener(AddFragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    public static VerifyOtpFragment newInstance(OtpData otpData) {

        Bundle args = new Bundle();
        args.putParcelable(AppConstant.KEY_OTP_DATA, otpData);

        VerifyOtpFragment fragment = new VerifyOtpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public VerifyOtpFragment() {

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
        return inflater.inflate(R.layout.fragment_verify_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        if (otpData != null) {
            textViewOtp.setVisibility(View.VISIBLE);
            textViewOtp.setText("Your OTP is : " + otpData.getOtpValue());
        }
    }

    //Local Methods
    private void processBundleArgs(Bundle args) {
        if (args != null) {
            if (args.containsKey(AppConstant.KEY_OTP_DATA) &&
                    args.getParcelable(AppConstant.KEY_OTP_DATA) != null) {
                otpData = args.getParcelable(AppConstant.KEY_OTP_DATA);
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

        btnVerifyOtp = view.findViewById(R.id.btnVerifyOtp);
        btnVerifyOtp.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        btnVerifyOtp.setOnClickListener(this);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

    }

    private boolean validateVerifyOtpForm() {
        if (TextUtils.isEmpty(editTextOtp.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                    getResources().getString(R.string.enter_otp_error), Snackbar.LENGTH_LONG,
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

    private void callValidateOtpApi() {
        /*toggleProgressBar(true);*/
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_VALIDATE_OTP_REQUEST, getValidateOtpRequest());
        callAppServer(AppConstant.REQ_API_TYPE_VALIDATE_OTP, intent, true);
    }

    private OtpRequest getValidateOtpRequest() {
        OtpRequest validateOtpRequest = new OtpRequest();
        validateOtpRequest.setMobileNo(otpData.getUserData().getMobileNo());
        validateOtpRequest.setOtpValue(Integer.parseInt(editTextOtp.getText().toString().trim()));

        return validateOtpRequest;
    }

    //Callbacks
    //Api Callback
    @Override
    protected void onSuccess(Object response) {
        /*toggleProgressBar(false);*/
        if (response instanceof OtpResponse) {
            OtpResponse otpResponse = (OtpResponse) response;
            if (/*otpResponse.getStatusCode() == HttpURLConnection.HTTP_OK*/
                    otpResponse.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                if (fragmentListener != null) {
                    fragmentListener.addFragmentWithType(AppConstant.FRAGMENT_TYPE_NEW_PASSWORD, null);
                }
            }
        }
    }

    @Override
    protected void onFailure(Object response) {
        /*toggleProgressBar(false);*/
        if (response instanceof Response) {
            Response mResponse = (Response) response;
            switch (mResponse.code()) {
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                            getResources().getString(R.string.invalid_otp_error),
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
        switch (view.getId()) {
            case R.id.btnVerifyOtp:
                /*if (fragmentListener != null) {
                    fragmentListener.addFragmentWithType(AppConstant.FRAGMENT_TYPE_NEW_PASSWORD);
                }*/
                if (validateVerifyOtpForm()) {
                    /*callValidateOtpApi();*/
                    if (fragmentListener != null) {
                        fragmentListener.addFragmentWithType(AppConstant.FRAGMENT_TYPE_NEW_PASSWORD, editTextOtp.getText().toString().trim());
                    }
                }
                break;
        }
    }
}
