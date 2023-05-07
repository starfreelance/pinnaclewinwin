package com.pinnacle.winwin.ui.withdrawal;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.snackbar.Snackbar;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.custom.CustomSingleButtonDialogFragment;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.WithdrawPointsRequest;
import com.pinnacle.winwin.network.model.WithdrawPointsResponse;
import com.pinnacle.winwin.network.model.WithdrawalPointsData;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;

public class WithdrawalScreenActivity extends ASOnlineBaseActivity implements View.OnClickListener {

    private static final String TAG = WithdrawalScreenActivity.class.getSimpleName();

    private AppCompatTextView textViewTitle;

    private AppCompatEditText editTextAmount;

    private Button btnSubmit;

    private View parentLayout;

    private CustomSingleButtonDialogFragment customSingleButtonDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal_screen);

        initViews();
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewTitle.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextAmount.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextAmount.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(Utils.getTypeFaceBodoni72(this));
        btnSubmit.setOnClickListener(this);

    }

    private boolean validateWithdrawalForm() {
        if (TextUtils.isEmpty(editTextAmount.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_amount_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else {
            return true;
        }
    }

    private int getWithdrawalAmount() {
        int amount = 0;
        try {
            amount = Integer.parseInt(editTextAmount.getText().toString().trim());
        } catch (NumberFormatException e) {
            LogUtils.e("NumberException", String.valueOf(e));
        }
        return amount;
    }

    private void clearData() {
        editTextAmount.setText("");
    }

    private void navigateToPreviousScreen() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_WITHDRAW_POINTS_STATUS, true);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showWithdrawRequestSuccessDialog() {
        dismissCustomSingleButtonDialog();
        customSingleButtonDialogFragment = CustomSingleButtonDialogFragment.newInstance(
                getResources().getString(R.string.withdraw_request_success_msg));
        customSingleButtonDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_WITHDRAW_REQUEST);
    }

    private void dismissCustomSingleButtonDialog() {
        if (!isFinishing() && customSingleButtonDialogFragment != null &&
                customSingleButtonDialogFragment.isVisible()) {
            customSingleButtonDialogFragment.dismissAllowingStateLoss();
            customSingleButtonDialogFragment = null;
        }
    }

    private void callWithdrawPointsApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_WITHDRAW_POINTS_REQUEST, getWithdrawPointsRequest());
        callAppServer(AppConstant.REQ_API_TYPE_WITHDRAW_POINTS, intent, true);
    }

    private WithdrawPointsRequest getWithdrawPointsRequest() {
        WithdrawPointsRequest withdrawPointsRequest = new WithdrawPointsRequest();
        withdrawPointsRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        withdrawPointsRequest.setInitiatedAmount(Integer.parseInt(editTextAmount.getText().toString()));

        return withdrawPointsRequest;
    }

    @Override
    protected void onSuccess(Object response) {
        if (response instanceof WithdrawPointsResponse) {
            WithdrawPointsResponse withdrawPointsResponse = (WithdrawPointsResponse) response;
            if (withdrawPointsResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                WithdrawalPointsData withdrawalPointsData = withdrawPointsResponse.getWithdrawalPointsData();
                ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_POINTS, withdrawalPointsData.getPoints());
                clearData();
                showWithdrawRequestSuccessDialog();
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

    @Override
    public void onClickSingleButtonListener() {
        if (customSingleButtonDialogFragment != null) {
            if (customSingleButtonDialogFragment.getTag() != null) {
                if (customSingleButtonDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_WITHDRAW_REQUEST)) {
                    dismissCustomSingleButtonDialog();
                    navigateToPreviousScreen();
                }
            }
        } else {
            super.onClickSingleButtonListener();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSubmit) {
            if (validateWithdrawalForm()) {
                callWithdrawPointsApi();
            }
        }
    }
}