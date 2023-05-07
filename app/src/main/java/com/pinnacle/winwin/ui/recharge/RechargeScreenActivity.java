package com.pinnacle.winwin.ui.recharge;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.pinnacle.winwin.BuildConfig;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.custom.CustomSingleButtonDialogFragment;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.WalletRechargeRequest;
import com.pinnacle.winwin.network.model.WalletRechargeResponse;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;

public class RechargeScreenActivity extends ASOnlineBaseActivity implements View.OnClickListener {

    private static final String TAG = RechargeScreenActivity.class.getSimpleName();

    private AppCompatTextView textViewTitle;

    private AppCompatEditText editTextAmount;

    private Button btnSubmit;

    private View parentLayout;

    private CustomSingleButtonDialogFragment customSingleButtonDialogFragment;

    private int minRecharge;
    private int maxRecharge;
    private boolean isWebViewOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_screen);

        initViews();
//        minRecharge = ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_MIN_RECHARGE, -1);
        minRecharge = 1; //todo: remove
        maxRecharge = ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_MAX_RECHARGE, -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isWebViewOpened) {
            isWebViewOpened = false;
            Log.e("DIALOG", "IS WEB VIEW");
            showRechargeInfoDialog();
        }
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

    private boolean validateRechargeForm() {
        if (TextUtils.isEmpty(editTextAmount.getText().toString().trim())) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.enter_amount_error), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (getRechargeAmount() < minRecharge) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    String.format(getResources().getString(R.string.minimum_amount_error), minRecharge), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else if (getRechargeAmount() > maxRecharge) {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    String.format(getResources().getString(R.string.maximum_amount_error), maxRecharge), Snackbar.LENGTH_LONG,
                    getResources().getString(R.string.btn_okay)).show();
            return false;
        } else {
            return true;
        }
    }

    private int getRechargeAmount() {
        int amount = 0;
        try {
            amount = Integer.parseInt(editTextAmount.getText().toString().trim());
        } catch (NumberFormatException e) {
            LogUtils.e("NumberException", String.valueOf(e));
        }
        return amount;
    }

    private void navigateToRazorPayWebViewScreen(String billId) {
//        Intent intent = new Intent(this, RazorPayWebViewScreenActivity.class);
//        intent.putExtra(AppConstant.KEY_BILL_ID, billId);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(BuildConfig.BASE_URL_RAZOR_PAY + billId));
        startActivity(intent);
        isWebViewOpened = true;
    }

    private void navigateToPreviousScreen() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_RECHARGE_INFO_STATUS, true);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showRechargeInfoDialog() {
        dismissCustomSingleButtonDialog();
        customSingleButtonDialogFragment = CustomSingleButtonDialogFragment.newInstance(
                getResources().getString(R.string.recharge_info_msg));
        customSingleButtonDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_RECHARGE_INFO);
    }

    private void dismissCustomSingleButtonDialog() {
        if (!isFinishing() && customSingleButtonDialogFragment != null &&
                customSingleButtonDialogFragment.isVisible()) {
            customSingleButtonDialogFragment.dismissAllowingStateLoss();
            customSingleButtonDialogFragment = null;
        }
    }

    private void callWalletRechargeApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_WALLET_RECHARGE_REQUEST, getWalletRechargeRequest());
        callAppServer(AppConstant.REQ_API_TYPE_WALLET_RECHARGE, intent, true);
    }

    private WalletRechargeRequest getWalletRechargeRequest() {
        WalletRechargeRequest walletRechargeRequest = new WalletRechargeRequest();
        walletRechargeRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        walletRechargeRequest.setAmount(Integer.parseInt(editTextAmount.getText().toString()));

        return walletRechargeRequest;
    }

    @Override
    protected void onSuccess(Object response) {
        if (response instanceof WalletRechargeResponse) {
            WalletRechargeResponse walletRechargeResponse = (WalletRechargeResponse) response;
            if (walletRechargeResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                if (walletRechargeResponse.getData() != null) {
                    LogUtils.d(TAG, walletRechargeResponse.getData());
                    navigateToRazorPayWebViewScreen(walletRechargeResponse.getData());
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

    @Override
    public void onClickSingleButtonListener() {
        if (customSingleButtonDialogFragment != null) {
            if (customSingleButtonDialogFragment.getTag() != null) {
                if (customSingleButtonDialogFragment.getTag().equalsIgnoreCase(AppConstant.DIALOG_TAG_RECHARGE_INFO)) {
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
            if (validateRechargeForm()) {
//                callWalletRechargeApi();
                Intent intent = new Intent(RechargeScreenActivity.this,PayWithUPIActivity.class);
                intent.putExtra("RechargeAmount",editTextAmount.getText().toString());
                startActivity(intent);
                finish();
//                editTextAmount.getText().toString().trim()
            }
        }
    }
}