package com.pinnacle.winwin.ui.recharge;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.pinnacle.winwin.BuildConfig;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.UpiPaymentStatusRequest;
import com.pinnacle.winwin.network.model.UpiPaymentStatusResponse;
import com.pinnacle.winwin.network.model.WalletBalanceData;
import com.pinnacle.winwin.network.model.WalletBalanceRequest;
import com.pinnacle.winwin.network.model.WalletBalanceResponse;
import com.pinnacle.winwin.utils.DateUtils;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.ResponseSplit;
import com.pinnacle.winwin.utils.Utils;

import java.util.Date;
import java.util.Map;

public class PayWithUPIActivity extends ASOnlineBaseActivity implements View.OnClickListener{

    private static final String TAG = "PayWithUPIActivity";

    private final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    private final String PAYTM_PACKAGE_NAME = "net.one97.paytm";
    private final String PHONEPE_PACKAGE_NAME = "com.phonepe.app";
    private final String BHIM_UPI_PACKAGE_NAME = "in.org.npci.upiapp";
    private final String AMAZON_PAY_PACKAGE_NAME = "in.amazon.mShop.android.shopping";

    int PAY_REQUEST_CODE = 123;

    private EditText et_enter_points;
    private TextView tv_payment_status,tv_add_points;

    private RadioGroup rg_payment_apps;

    private RadioButton rb_Gpay,rb_paytm, rb_phone_pe;

    private String selectedApp = "";

    private int paymentApp = 0;

    private int TRANSACTION_TYPE = 0;

    private View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_upiactivity);

        parentLayout = findViewById(R.id.parentLayout);
        et_enter_points = findViewById(R.id.et_enter_points);
        rg_payment_apps = findViewById(R.id.rg_payment_apps);
        rb_Gpay = findViewById(R.id.rb_Gpay);
        rb_paytm = findViewById(R.id.rb_paytm);
        rb_phone_pe = findViewById(R.id.rb_phone_pe);
        tv_payment_status = findViewById(R.id.tv_payment_status);
        tv_add_points = findViewById(R.id.tv_add_points);

        Intent intent = getIntent();
        String rechargeAmount = intent.getStringExtra("RechargeAmount");

        et_enter_points.setText(rechargeAmount);

        tv_add_points.setTypeface(Utils.getTypeFaceBodoni72(this));
        tv_add_points.getPaint().setShader(Utils.getTextGradient(new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        et_enter_points.setTypeface(Utils.getTypeFaceBodoni72(this));
        et_enter_points.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        rb_Gpay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    selectedApp = GOOGLE_PAY_PACKAGE_NAME;
                    paymentApp = 1;
                }
            }
        });

        rb_paytm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    selectedApp = PAYTM_PACKAGE_NAME;
                    paymentApp = 2;
                }
            }
        });

        rb_phone_pe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    selectedApp = PHONEPE_PACKAGE_NAME;
                    paymentApp = 3;
                }
            }
        });

        findViewById(R.id.btn_pay).setOnClickListener(view -> {
            if (validation())
                openUPIApp(selectedApp);
        });

        /*
        Response Testing

        //GPAY wrong Response
        String GpayResponse = "txnId=ICI0d9b26ae4e4840cd80ba1582b8d3342d&responseCode=0&Status=SUCCESS&txnRef";

        //PayTm Correct Response
        String PayTmResponse = "txnId=PTM280913c93f684e009c16614110ef36d4&responseCode=U16&ApprovalRefNo=231768315782&Status=FAILURE";

        //Phonepe Correct Response
        String PhonePeResponse = "status: txnId=YBLce4c24d81ef84a3fb8f3185c2d9adca5&txnRef=null&Status=Success&responseCode=00";

        Map<String, String> mapFromQuery = ResponseSplit.getMapFromQuery(PhonePeResponse);

        Log.e(TAG, "onCreate: "+ mapFromQuery);
        */
    }


    @Override
    protected void showInternetError() {

    }

    @Override
    protected void onFailure(Object response) {
        Log.e(TAG, "onFailure: "+ response);
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

        exit();
//        if (response instanceof WalletBalanceResponse) {
//            exit();
//        }
//
//        if (response instanceof UpiPaymentStatusResponse) {
//            if (TRANSACTION_TYPE != 0 && TRANSACTION_TYPE != 1){
//                callGetWalletBalanceApi();
//            }
//        }
    }

    @Override
    protected void onSuccess(Object response) {
        Log.e(TAG, "onSuccess: "+ response);
        if (response instanceof WalletBalanceResponse) {
            WalletBalanceResponse walletBalanceResponse = (WalletBalanceResponse) response;
            if (walletBalanceResponse.getWalletBalanceData() != null) {
                WalletBalanceData walletBalanceData = walletBalanceResponse.getWalletBalanceData();
                updateWalletBalance(walletBalanceData.getPoints());
            }
        }

        if (response instanceof UpiPaymentStatusResponse) {
            if (TRANSACTION_TYPE != 0 && TRANSACTION_TYPE != 1){
                callGetWalletBalanceApi();
            }
        }

    }

    private void exit(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },5000);
    }

    private boolean validation() {
        if (TextUtils.isEmpty(et_enter_points.getText().toString())){
            Toast.makeText(this, "Please enter points!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!TextUtils.isEmpty(et_enter_points.getText().toString())){
            String amount = et_enter_points.getText().toString();
            int netAmount = Integer.parseInt(amount);
            if ( netAmount > 0  && netAmount <= 2000) {

            }else {
                if (netAmount == 0){
                    Toast.makeText(this, "Point cannot be 0!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Only 2000 points are allowed in single transaction!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }

        if (rg_payment_apps.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Please select App to pay!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void openUPIApp(String packageName){
        tv_payment_status.setVisibility(View.GONE);
        //send payment initiate status to api
        callSendUPIPaymentStatusApi("null","1");

        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", "q208326330@ybl")
                        .appendQueryParameter("pn", "Vishal Rajesh")
                        .appendQueryParameter("mc", "")
                        .appendQueryParameter("tr", "")
                        .appendQueryParameter("tn", "Add Points")
                        .appendQueryParameter("am", et_enter_points.getText().toString()+".00")
                        .appendQueryParameter("cu", "INR")
                        .appendQueryParameter("url", "")
                        .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(packageName);
        startActivityForResult(intent, PAY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAY_REQUEST_CODE){
            tv_payment_status.setVisibility(View.VISIBLE);
            if (data != null){
                String response = data.getStringExtra("response");
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "payment status: " + response);
                }
                if (!TextUtils.isEmpty(response)){
                    Map<String, String> mapFromQuery = ResponseSplit.getMapFromQuery(response);
                    String status = mapFromQuery.get("Status");
                    if (!TextUtils.isEmpty(status)) {
                        if (null != status && status.equalsIgnoreCase("SUCCESS")) {
                            tv_payment_status.setText("Payment SUCCESS!");
                            tv_payment_status.setTextColor(getResources().getColor(R.color.success_color));
                            String txnId = mapFromQuery.get("txnId");
                            TRANSACTION_TYPE = 2;
                            callSendUPIPaymentStatusApi(txnId, "2");
                            Toast.makeText(this, "Payment Successful, Points added to your account!", Toast.LENGTH_SHORT).show();
                        }
                        if (null != status && status.equalsIgnoreCase("FAILURE")) {
                            tv_payment_status.setText("Payment FAILED!");
                            tv_payment_status.setTextColor(getResources().getColor(R.color.success_color));
                            String txnId = mapFromQuery.get("txnId");
                            TRANSACTION_TYPE = 3;
                            callSendUPIPaymentStatusApi(txnId, "3");
                            Toast.makeText(this, "Payment Failed, Please contact administrator!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    tv_payment_status.setText("Payment FAILED!");
                    tv_payment_status.setTextColor(getResources().getColor(R.color.failure_color));
                    TRANSACTION_TYPE = 3;
                    callSendUPIPaymentStatusApi("null","3");
                    Toast.makeText(this, "Something went wrong, Please contact administrator!", Toast.LENGTH_SHORT).show();
                }
            }else {
                tv_payment_status.setText("Payment FAILED!");
                tv_payment_status.setTextColor(getResources().getColor(R.color.failure_color));
                TRANSACTION_TYPE = 3;
                callSendUPIPaymentStatusApi("null","3");
                Toast.makeText(this, "Something went wrong, Please contact administrator!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void callSendUPIPaymentStatusApi(String txnId, String transactionType) {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_UPI_PAYMENT_INFO_STATUS, getUPIPaymentStatusRequest(txnId,transactionType));
        callAppServer(AppConstant.REQ_API_TYPE_SEND_UPI_PAYMENT_STATUS, intent, true);
    }

    private UpiPaymentStatusRequest getUPIPaymentStatusRequest(String txnId, String transactionType) {
        UpiPaymentStatusRequest upiPaymentStatusRequest = new UpiPaymentStatusRequest();
        upiPaymentStatusRequest.setCust_id(ASOnlinePreferenceManager.getInteger(this,
                AppConstant.KEY_USER_CUST_ID, -1));
        upiPaymentStatusRequest.setUpi_id("null");
        upiPaymentStatusRequest.setAmount_point(Integer.parseInt(et_enter_points.getText().toString()));
        //1 = gpay, 2 = paytm, 3 = Phonepe
        switch (paymentApp){
            case 1:
                upiPaymentStatusRequest.setUpi_provider("gpay");
                break;
            case 2:
                upiPaymentStatusRequest.setUpi_provider("paytm");
                break;
            case 3:
                upiPaymentStatusRequest.setUpi_provider("phonepe");
                break;
        }

        upiPaymentStatusRequest.setDate_time(DateUtils.getStringFormattedDate(new Date(), DateUtils.DATE_FORMAT_24_hrs));
        upiPaymentStatusRequest.setDevice_id(ASOnlinePreferenceManager.getString(this, AppConstant.KEY_IMEI_NUMBER, ""));
        upiPaymentStatusRequest.setMobile_no(ASOnlinePreferenceManager.getString(this, AppConstant.KEY_USER_MOBILE_NO, ""));
        //1 = Initiate, 2 = Success, 3 = Failure
        upiPaymentStatusRequest.setTransaction_type(transactionType);
        upiPaymentStatusRequest.setTransaction_no(txnId);


        return upiPaymentStatusRequest;
    }


    //Reload Points
    private void callGetWalletBalanceApi() {
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

        Utils.showCustomSnackBarMessageView(this, parentLayout,
                getResources().getString(R.string.wallet_updated_msg),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
        LogUtils.e(TAG,TAG +" Points added " + points);
        exit();

    }

    @Override
    public void onClick(View view) {

    }
}