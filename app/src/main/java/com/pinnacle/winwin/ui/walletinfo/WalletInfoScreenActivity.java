package com.pinnacle.winwin.ui.walletinfo;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.WalletBalanceData;
import com.pinnacle.winwin.network.model.WalletBalanceRequest;
import com.pinnacle.winwin.network.model.WalletBalanceResponse;
import com.pinnacle.winwin.ui.wallettransactionhistory.WalletTransactionHistoryScreenActivity;
import com.pinnacle.winwin.utils.Utils;

public class WalletInfoScreenActivity extends ASOnlineBaseActivity implements View.OnClickListener {

    private static final String TAG = WalletInfoScreenActivity.class.getSimpleName();

    private AppCompatTextView textViewTitle;
    private AppCompatTextView lblName;
    private AppCompatTextView textViewName;
    private AppCompatTextView lblNumber;
    private AppCompatTextView textViewNumber;
    private AppCompatTextView lblWalletBalance;
    private AppCompatTextView textViewWalletBalance;
    private AppCompatTextView lblWalletHistory;
    private AppCompatTextView textViewWalletHistory;

    private ProgressBar progressBarWallet;

    private ConstraintLayout layoutWalletHistory;

    private Button btnReload;

    private View parentLayout;

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_info_screen_new);

        name = ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_FIRST_NAME, "") +
                " " + ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_LAST_NAME, "");
        initViews();
    }

    //Local Methods
    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewTitle.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        lblName = findViewById(R.id.lblName);
        lblName.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblName.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewName = findViewById(R.id.textViewName);
        textViewName.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewName.setText(name);
        /*textViewName.setText("");
        textViewName.append(ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_FIRST_NAME, ""));
        textViewName.append(" " + ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_LAST_NAME, ""));*/

        lblNumber = findViewById(R.id.lblNumber);
        lblNumber.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblNumber.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewNumber = findViewById(R.id.textViewNumber);
        textViewNumber.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewNumber.setText(ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_MOBILE_NO, ""));

        lblWalletBalance = findViewById(R.id.lblWalletBalance);
        lblWalletBalance.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblWalletBalance.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewWalletBalance = findViewById(R.id.textViewWalletBalance);
        textViewWalletBalance.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewWalletBalance.setText(String.valueOf(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_POINTS, 0)));

        lblWalletHistory = findViewById(R.id.lblWalletHistory);
        lblWalletHistory.setTypeface(Utils.getTypeFaceBodoni72(this));
        lblWalletHistory.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        textViewWalletHistory = findViewById(R.id.textViewWalletHistory);
        textViewWalletHistory.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewWalletHistory.setPaintFlags(textViewWalletHistory.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        progressBarWallet = findViewById(R.id.progressBarWallet);
        progressBarWallet.getIndeterminateDrawable().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        layoutWalletHistory = findViewById(R.id.layoutWalletHistory);
        layoutWalletHistory.setOnClickListener(this);

        btnReload = findViewById(R.id.btnReload);
        btnReload.setTypeface(Utils.getTypeFaceBodoni72(this));
        btnReload.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));
        btnReload.setOnClickListener(this);
    }

    private void toggleProgressBar(boolean isVisible) {
        if (isVisible) {
            textViewWalletBalance.setVisibility(View.GONE);
            progressBarWallet.setVisibility(View.VISIBLE);
        } else {
            progressBarWallet.setVisibility(View.GONE);
            textViewWalletBalance.setVisibility(View.VISIBLE);
        }
    }

    private void navigateToWalletTransactionHistoryScreen() {
        Intent intent = new Intent(this, WalletTransactionHistoryScreenActivity.class);
        startActivity(intent);
    }

    private void callGetWalletBalanceApi() {
        toggleProgressBar(true);
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_GET_WALLET_BALANCE_REQUEST, getWalletBalanceRequest());
        callAppServer(AppConstant.REQ_API_TYPE_GET_WALLET_BALANCE, intent, false);
    }

    private WalletBalanceRequest getWalletBalanceRequest() {
        WalletBalanceRequest walletBalanceRequest = new WalletBalanceRequest();
        walletBalanceRequest.setCustomerId(ASOnlinePreferenceManager.
                getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        /*Gson gson = new Gson();
        String jsonString = gson.toJson(walletBalanceRequest);
        return RSAUtility.encrypt(jsonString, RSAUtility.publicKey);*/
        return walletBalanceRequest;
    }

    private void updateWalletBalance(int points) {
        ASOnlinePreferenceManager.saveInteger(this, AppConstant.KEY_USER_POINTS, points);
        textViewWalletBalance.setText(String.valueOf(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_POINTS, 0)));
        Utils.showCustomSnackBarMessageView(this, parentLayout,
                getResources().getString(R.string.wallet_updated_msg),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
    }

    //Callbacks
    //Api Callback
    @Override
    protected void onSuccess(Object response) {
        if (response instanceof WalletBalanceResponse) {
            toggleProgressBar(false);
            WalletBalanceResponse walletBalanceResponse = (WalletBalanceResponse) response;
            if (walletBalanceResponse.getWalletBalanceData() != null) {
                WalletBalanceData walletBalanceData = walletBalanceResponse.getWalletBalanceData();
                updateWalletBalance(walletBalanceData.getPoints());
            }
        }

    }

    @Override
    protected void onFailure(Object response) {
        toggleProgressBar(false);
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
        /*if (response instanceof Response) {
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
        toggleProgressBar(false);
        Utils.showCustomSnackBarMessageView(this, parentLayout,
                getResources().getString(R.string.internet_unavailable_error),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
    }

    //Event Handlers
    //View.OnClickListener
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnReload:
                callGetWalletBalanceApi();
                break;
            case R.id.layoutWalletHistory:
                navigateToWalletTransactionHistoryScreen();
                break;
        }
    }
}
