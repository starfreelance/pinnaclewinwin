package com.pinnacle.winwin.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.pinnacle.winwin.BuildConfig;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.custom.CustomProgressDialog;
import com.pinnacle.winwin.custom.CustomSingleButtonDialogFragment;
import com.pinnacle.winwin.listener.CustomSingleButtonDialogListener;
import com.pinnacle.winwin.network.ApiCalls;
import com.pinnacle.winwin.network.RetrofitUtils;
import com.pinnacle.winwin.network.model.AddNewBetOtcRequest;
import com.pinnacle.winwin.network.model.AddNewBetResponse;
import com.pinnacle.winwin.network.model.AddHTNewBetRequest;
import com.pinnacle.winwin.network.model.AddHTNewBetResponse;
import com.pinnacle.winwin.network.model.BaazaarHistoryRequest;
import com.pinnacle.winwin.network.model.BaazaarHistoryResponse;
import com.pinnacle.winwin.network.model.BaazaarRemainingTimeRequest;
import com.pinnacle.winwin.network.model.BaazaarRemainingTimeResponse;
import com.pinnacle.winwin.network.model.BetClaimRequest;
import com.pinnacle.winwin.network.model.BetClaimResponse;
import com.pinnacle.winwin.network.model.BetHistoryRequest;
import com.pinnacle.winwin.network.model.BetHistoryResponse;
import com.pinnacle.winwin.network.model.CancelWithdrawPointsRequest;
import com.pinnacle.winwin.network.model.CancelWithdrawPointsResponse;
import com.pinnacle.winwin.network.model.ChangePasswordRequest;
import com.pinnacle.winwin.network.model.ChatRequest;
import com.pinnacle.winwin.network.model.ChatResponse;
import com.pinnacle.winwin.network.model.ChatThreadRequest;
import com.pinnacle.winwin.network.model.ChatThreadResponse;
import com.pinnacle.winwin.network.model.CustomerDetailsUpdateRequest;
import com.pinnacle.winwin.network.model.CustomerTransactionRequest;
import com.pinnacle.winwin.network.model.CustomerTransactionResponse;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.network.model.HTBetHistoryRequest;
import com.pinnacle.winwin.network.model.HTBetHistoryResponse;
import com.pinnacle.winwin.network.model.HTCancelBetRequest;
import com.pinnacle.winwin.network.model.HTCancelBetResponse;
import com.pinnacle.winwin.network.model.HTInitialRequest;
import com.pinnacle.winwin.network.model.HTInitialResponse;
import com.pinnacle.winwin.network.model.HTResultRequest;
import com.pinnacle.winwin.network.model.HTResultResponse;
import com.pinnacle.winwin.network.model.IfscResponse;
import com.pinnacle.winwin.network.model.KycRequest;
import com.pinnacle.winwin.network.model.KycResponse;
import com.pinnacle.winwin.network.model.LoginRequest;
import com.pinnacle.winwin.network.model.LoginResponse;
import com.pinnacle.winwin.network.model.MatkaInitialRequest;
import com.pinnacle.winwin.network.model.MatkaInitialResponse;
import com.pinnacle.winwin.network.model.MatkaMasterResponse;
import com.pinnacle.winwin.network.model.OtpRequest;
import com.pinnacle.winwin.network.model.OtpResponse;
import com.pinnacle.winwin.network.model.RechargeHistoryRequest;
import com.pinnacle.winwin.network.model.RechargeHistoryResponse;
import com.pinnacle.winwin.network.model.SignUpRequest;
import com.pinnacle.winwin.network.model.SignUpResponse;
import com.pinnacle.winwin.network.model.UpdateProfileImageRequest;
import com.pinnacle.winwin.network.model.UpdateProfileImageResponse;
import com.pinnacle.winwin.network.model.UpiPaymentStatusRequest;
import com.pinnacle.winwin.network.model.UpiPaymentStatusResponse;
import com.pinnacle.winwin.network.model.ValidateOtpResponse;
import com.pinnacle.winwin.network.model.WalletBalanceRequest;
import com.pinnacle.winwin.network.model.WalletBalanceResponse;
import com.pinnacle.winwin.network.model.WalletRechargeRequest;
import com.pinnacle.winwin.network.model.WalletRechargeResponse;
import com.pinnacle.winwin.network.model.WithdrawHistoryRequest;
import com.pinnacle.winwin.network.model.WithdrawHistoryResponse;
import com.pinnacle.winwin.network.model.WithdrawPointsRequest;
import com.pinnacle.winwin.network.model.WithdrawPointsResponse;
import com.pinnacle.winwin.security.CryptLib;
import com.pinnacle.winwin.security.RSAUtility;
import com.pinnacle.winwin.ui.LoginScreenActivity;
import com.pinnacle.winwin.ui.baazaarhistory.BaazaarHistoryScreenActivity;
import com.pinnacle.winwin.ui.singlegame.fragment.CustomDualButtonDialogFragment;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.NetworkUtils;
import com.pinnacle.winwin.utils.Utils;

import java.net.HttpURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_ADD_HT_NEW_BET;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_CANCEL_HT_BET;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_CANCEL_WITHDRAW_POINTS;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_CHANGE_PASSWORD;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_GENERATE_OTP;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_GET_BANK_DETAILS;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_GET_CHAT_THREAD;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_GET_HT_BET_HISTORY;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_GET_HT_INITIAL_DATA;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_GET_HT_RESULT;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_GET_RECHARGE_HISTORY;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_GET_WITHDRAW_HISTORY;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_SEND_MESSAGE;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_SEND_UPI_PAYMENT_STATUS;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_UPDATE_CUSTOMER_NAME;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_UPDATE_KYC;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_USER_SIGN_UP;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_VALIDATE_OTP;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_WALLET_RECHARGE;
import static com.pinnacle.winwin.app.AppConstant.REQ_API_TYPE_WITHDRAW_POINTS;

public abstract class ASOnlineBaseActivity<T> extends AppCompatActivity implements Callback<T>,
        CustomSingleButtonDialogListener {

    private CustomProgressDialog customProgressDialog;
    protected CustomDualButtonDialogFragment customDualButtonDialogFragment;
    private CustomSingleButtonDialogFragment customSingleButtonDialogFragment;
    private int requestType = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void showProgressDialog(boolean isVisible, String message) {
        if (!isFinishing()) {
            if (isVisible) {
                customProgressDialog = new CustomProgressDialog(this);
                customProgressDialog.setCanceledOnTouchOutside(false);
                customProgressDialog.setCancelable(false);
                customProgressDialog.show();
            } else {
                if (customProgressDialog != null &&
                        customProgressDialog.isShowing()) {
                    customProgressDialog.dismiss();
                }
            }
        }

    }

    protected void dismissProgressDialog() {
        if (!isFinishing() && customProgressDialog != null &&
                customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
        }
    }

    protected void callAppServer(int requestType, Intent intent, boolean isProgressVisible) {

        if (!NetworkUtils.isNetworkConnected(this)) {
            showInternetError();
            return;
        }

        if (isProgressVisible) {
            showProgressDialog(true, "Please Wait...");
        }

        RetrofitUtils retrofitUtils = RetrofitUtils.getRetrofitInstance(BuildConfig.BASE_URL);
        LogUtils.d("RETROFIT INSTANCE", String.valueOf(System.identityHashCode(retrofitUtils)));
        ApiCalls apiCalls = retrofitUtils.getApiCalls();

        Gson gson;
        String jsonString;
        String requestString;

        this.requestType = requestType;

        switch (requestType) {
            case AppConstant.REQ_API_TYPE_GET_MASTER_DATA:
                apiCalls.getMaster(Utils.getHeaderToken(this)).enqueue((Callback<MatkaMasterResponse>) this);
                break;
            case AppConstant.REQ_API_TYPE_USER_LOGIN:
                LoginRequest loginRequest = intent.getParcelableExtra(AppConstant.KEY_LOGIN_REQUEST);
                /*gson = new Gson();
                jsonString = gson.toJson(loginRequest);
                requestString = RSAUtility.encrypt(jsonString, RSAUtility.publicKey);*/
                gson = new Gson();
                jsonString = gson.toJson(loginRequest);
                requestString = getEncryptedString(jsonString);
                /*apiCalls.userLogin(Utils.getAsDataRequest(requestString)).enqueue((Callback<LoginResponse>) this);*/
                /*apiCalls.userLogin(Utils.getAsDataRequest(requestString)).enqueue((Callback<LoginResponse>) this);*/
                apiCalls.userLogin(loginRequest).enqueue((Callback<LoginResponse>) this);
                break;
            case AppConstant.REQ_API_TYPE_ADD_NEW_BET:
                /*AddNewBetRequest addNewBetRequest = intent.getParcelableExtra(AppConstant.KEY_ADD_NEW_BET_REQUEST);*/
                AddNewBetOtcRequest addNewBetRequest = intent.getParcelableExtra(AppConstant.KEY_ADD_NEW_BET_REQUEST);
                gson = new Gson();
                jsonString = gson.toJson(addNewBetRequest);
                /*requestString = RSAUtility.encrypt(jsonString, RSAUtility.publicKey);*/
                requestString = getEncryptedString(jsonString);
                /*apiCalls.addNewBet(Utils.getHeaderToken(this), Utils.getAsDataRequest(requestString)).enqueue((Callback<AddNewBetResponse>) this);*/
                apiCalls.addNewBet(Utils.getHeaderToken(this), addNewBetRequest).enqueue((Callback<AddNewBetResponse>) this);
                break;
            case AppConstant.REQ_API_TYPE_GET_BET_HISTORY:
                BetHistoryRequest betHistoryRequest = intent.getParcelableExtra(AppConstant.KEY_GET_BET_HISTORY_REQUEST);
                gson = new Gson();
                jsonString = gson.toJson(betHistoryRequest);
                /*requestString = RSAUtility.encrypt(jsonString, RSAUtility.publicKey);*/
                requestString = getEncryptedString(jsonString);
                /*apiCalls.getBetHistory(Utils.getHeaderToken(this), Utils.getAsDataRequest(requestString)).enqueue((Callback<BetHistoryResponse>) this);*/
                apiCalls.getBetHistory(Utils.getHeaderToken(this), betHistoryRequest).enqueue((Callback<BetHistoryResponse>) this);
                break;
            case AppConstant.REQ_API_TYPE_GET_INITIAL_DATA:
                MatkaInitialRequest matkaInitialRequest = intent.getParcelableExtra(AppConstant.KEY_MATKA_INITIAL_REQUEST);
                gson = new Gson();
                jsonString = gson.toJson(matkaInitialRequest);
                requestString = getEncryptedString(jsonString);
                /*apiCalls.getInitialData(Utils.getHeaderToken(this), Utils.getAsDataRequest(requestString)).
                        enqueue((Callback<MatkaInitialResponse>) this);*/
                apiCalls.getInitialData(Utils.getHeaderToken(this), matkaInitialRequest).
                        enqueue((Callback<MatkaInitialResponse>) this);
                break;
            case AppConstant.REQ_API_TYPE_GET_BAAZAAR_REMAINING_TIME:
                BaazaarRemainingTimeRequest baazaarRemainingTimeRequest = intent.getParcelableExtra(AppConstant.KEY_BAAZAAR_REMAINING_TIME_REQUEST);
                gson = new Gson();
                jsonString = gson.toJson(baazaarRemainingTimeRequest);
                requestString = getEncryptedString(jsonString);
                /*apiCalls.getBaazaarRemainingTime(Utils.getHeaderToken(this),
                        Utils.getAsDataRequest(requestString)).
                        enqueue((Callback<BaazaarRemainingTimeResponse>) this);*/
                apiCalls.getBaazaarRemainingTime(Utils.getHeaderToken(this),
                        baazaarRemainingTimeRequest).
                        enqueue((Callback<BaazaarRemainingTimeResponse>) this);
                break;
            case AppConstant.REQ_API_TYPE_GET_WALLET_BALANCE:
                WalletBalanceRequest walletBalanceRequest = intent.getParcelableExtra(AppConstant.KEY_GET_WALLET_BALANCE_REQUEST);
                gson = new Gson();
                jsonString = gson.toJson(walletBalanceRequest);
                requestString = getEncryptedString(jsonString);
                /*apiCalls.getWalletBalance(Utils.getHeaderToken(this),
                        Utils.getAsDataRequest(requestString)).
                        enqueue((Callback<WalletBalanceResponse>) this);*/
                apiCalls.getWalletBalance(Utils.getHeaderToken(this),
                        walletBalanceRequest).
                        enqueue((Callback<WalletBalanceResponse>) this);
                break;
            case AppConstant.REQ_API_TYPE_CLAIM_BET:
                BetClaimRequest betClaimRequest = intent.getParcelableExtra(AppConstant.KEY_CLAIM_BET_REQUEST);
                gson = new Gson();
                jsonString = gson.toJson(betClaimRequest);
                requestString = RSAUtility.encrypt(jsonString, RSAUtility.publicKey);
                /*apiCalls.claimBet(Utils.getHeaderToken(this), Utils.getAsDataRequest(requestString)).enqueue((Callback<BetClaimResponse>) this);*/
                apiCalls.claimBet(Utils.getHeaderToken(this), betClaimRequest).enqueue((Callback<BetClaimResponse>) this);
                break;
            case AppConstant.REQ_API_TYPE_GET_CUSTOMER_TRANSACTIONS:
                CustomerTransactionRequest customerTransactionRequest = intent.
                        getParcelableExtra(AppConstant.KEY_GET_CUSTOMER_TRANSACTIONS_REQUEST);
                gson = new Gson();
                jsonString = gson.toJson(customerTransactionRequest);
                requestString = RSAUtility.encrypt(jsonString, RSAUtility.publicKey);
                /*apiCalls.getCustomerTransactions(Utils.getHeaderToken(this), Utils.getAsDataRequest(requestString)).enqueue((Callback<CustomerTransactionResponse>) this);*/
                apiCalls.getCustomerTransactions(Utils.getHeaderToken(this), customerTransactionRequest).enqueue((Callback<CustomerTransactionResponse>) this);
                break;
            case AppConstant.REQ_API_TYPE_GET_BAAZAAR_HISTORY:
                BaazaarHistoryRequest baazaarHistoryRequest = intent.getParcelableExtra(AppConstant.KEY_GET_BAAZAAR_HISTORY_REQUEST);
                apiCalls.getBaazaarHistory(Utils.getHeaderToken(this), baazaarHistoryRequest).enqueue((Callback<BaazaarHistoryResponse>) this);
                break;
            case AppConstant.REQ_API_TYPE_UPDATE_PROFILE_IMAGE:
                UpdateProfileImageRequest updateProfileImageRequest = intent.getParcelableExtra(
                        AppConstant.KEY_UPDATE_PROFILE_IMAGE_REQUEST);
                gson = new Gson();
                jsonString = gson.toJson(updateProfileImageRequest);
                requestString = RSAUtility.encrypt(jsonString, RSAUtility.publicKey);
                /*apiCalls.updateProfileImage(Utils.getHeaderToken(this), Utils.getAsDataRequest(requestString)).enqueue((Callback<UpdateProfileImageResponse>) this);*/
                apiCalls.updateProfileImage(Utils.getHeaderToken(this), updateProfileImageRequest).enqueue((Callback<UpdateProfileImageResponse>) this);
                break;
            case REQ_API_TYPE_UPDATE_CUSTOMER_NAME:
                CustomerDetailsUpdateRequest customerDetailsUpdateRequest = intent.getParcelableExtra(AppConstant.KEY_UPDATE_CUSTOMER_NAME_REQUEST);
                gson = new Gson();
                jsonString = gson.toJson(customerDetailsUpdateRequest);
                requestString = RSAUtility.encrypt(jsonString, RSAUtility.publicKey);
                /*apiCalls.updateCustomerDetails(Utils.getHeaderToken(this),
                        Utils.getAsDataRequest(requestString)).enqueue((Callback<GenericResponse>) this);*/
                apiCalls.updateCustomerDetails(Utils.getHeaderToken(this),
                        customerDetailsUpdateRequest).enqueue((Callback<GenericResponse>) this);
                break;
            case REQ_API_TYPE_CHANGE_PASSWORD:
                ChangePasswordRequest changePasswordRequest = intent.getParcelableExtra(AppConstant.KEY_CHANGE_PASSWORD_REQUEST);
//                gson = new Gson();
//                jsonString = gson.toJson(changePasswordRequest);
//                /*requestString = RSAUtility.encrypt(jsonString, RSAUtility.publicKey);*/
//                requestString = ASOnlineBaseActivity.getEncryptedString(jsonString);
                /*apiCalls.changePassword(Utils.getHeaderToken(mActivity), Utils.getAsDataRequest(requestString)).enqueue((Callback<GenericResponse>) this);*/
                apiCalls.changePassword(Utils.getHeaderToken(this), changePasswordRequest).enqueue((Callback<GenericResponse>) this);
                break;
            case REQ_API_TYPE_GET_HT_INITIAL_DATA:
                HTInitialRequest htInitialRequest = intent.getParcelableExtra(AppConstant.KEY_HT_INITIAL_REQUEST);
                apiCalls.getHTInitialData(Utils.getHeaderToken(this), htInitialRequest).enqueue((Callback<HTInitialResponse>) this);
                break;
            case REQ_API_TYPE_ADD_HT_NEW_BET:
                AddHTNewBetRequest addHTNewBetRequest = intent.getParcelableExtra(AppConstant.KEY_ADD_HT_NEW_BET_REQUEST);
                apiCalls.addHTNewBet(Utils.getHeaderToken(this), addHTNewBetRequest).enqueue((Callback<AddHTNewBetResponse>) this);
                break;
            case REQ_API_TYPE_GET_HT_RESULT:
                HTResultRequest htResultRequest = intent.getParcelableExtra(AppConstant.KEY_HT_RESULT_REQUEST);
                apiCalls.getHTResult(Utils.getHeaderToken(this), htResultRequest).enqueue((Callback<HTResultResponse>) this);
                break;
            case REQ_API_TYPE_CANCEL_HT_BET:
                HTCancelBetRequest htCancelBetRequest = intent.getParcelableExtra(AppConstant.KEY_CANCEL_HT_BET_REQUEST);
                apiCalls.cancelHTBet(Utils.getHeaderToken(this), htCancelBetRequest).enqueue((Callback<HTCancelBetResponse>) this);
                break;
            case REQ_API_TYPE_GET_HT_BET_HISTORY:
                HTBetHistoryRequest htBetHistoryRequest = intent.getParcelableExtra(AppConstant.KEY_GET_HT_BET_HISTORY_REQUEST);
                apiCalls.getHTBetHistory(Utils.getHeaderToken(this), htBetHistoryRequest).enqueue((Callback<HTBetHistoryResponse>) this);
                break;
            case REQ_API_TYPE_USER_SIGN_UP:
                SignUpRequest signUpRequest = intent.getParcelableExtra(AppConstant.KEY_SIGN_UP_REQUEST);
                apiCalls.userSignUp(signUpRequest).enqueue((Callback<SignUpResponse>) this);
                break;
            case REQ_API_TYPE_GENERATE_OTP:
                OtpRequest otpRequest = intent.getParcelableExtra(AppConstant.KEY_GENERATE_OTP_REQUEST);
                apiCalls.generateOtp(otpRequest).enqueue((Callback<OtpResponse>) this);
                break;
            case REQ_API_TYPE_VALIDATE_OTP:
                OtpRequest validateOtpRequest = intent.getParcelableExtra(AppConstant.KEY_VALIDATE_OTP_REQUEST);
                apiCalls.validateOtp(validateOtpRequest).enqueue((Callback<ValidateOtpResponse>) this);
                break;
            case REQ_API_TYPE_SEND_MESSAGE:
                ChatRequest chatRequest = intent.getParcelableExtra(AppConstant.KEY_SEND_MESSAGE_REQUEST);
                apiCalls.sendMessage(Utils.getHeaderToken(this), chatRequest).enqueue((Callback<ChatResponse>) this);
                break;
            case REQ_API_TYPE_GET_CHAT_THREAD:
                ChatThreadRequest chatThreadRequest = intent.getParcelableExtra(AppConstant.KEY_GET_CHAT_THREAD_REQUEST);
                apiCalls.getChatThread(Utils.getHeaderToken(this), chatThreadRequest).enqueue((Callback<ChatThreadResponse>) this);
                break;
            case REQ_API_TYPE_WALLET_RECHARGE:
                WalletRechargeRequest walletRechargeRequest = intent.getParcelableExtra(AppConstant.KEY_WALLET_RECHARGE_REQUEST);
                apiCalls.walletRecharge(Utils.getHeaderToken(this), walletRechargeRequest).enqueue((Callback<WalletRechargeResponse>) this);
                break;
            case REQ_API_TYPE_UPDATE_KYC:
                KycRequest kycRequest = intent.getParcelableExtra(AppConstant.KEY_UPDATE_KYC_REQUEST);
                apiCalls.updateKyc(Utils.getHeaderToken(this), kycRequest).enqueue((Callback<KycResponse>) this);
                break;
            case REQ_API_TYPE_GET_RECHARGE_HISTORY:
                RechargeHistoryRequest rechargeHistoryRequest = intent.getParcelableExtra(AppConstant.KEY_GET_RECHARGE_HISTORY_REQUEST);
                apiCalls.getRechargeHistory(Utils.getHeaderToken(this), rechargeHistoryRequest).enqueue((Callback<RechargeHistoryResponse>) this);
                break;
            case REQ_API_TYPE_GET_BANK_DETAILS:
                String url = intent.getStringExtra(AppConstant.KEY_GET_BANK_DETAILS_URL);
                apiCalls.getBankDetails(url).enqueue((Callback<IfscResponse>) this);
                break;
            case REQ_API_TYPE_WITHDRAW_POINTS:
                WithdrawPointsRequest withdrawPointsRequest = intent.getParcelableExtra(AppConstant.KEY_WITHDRAW_POINTS_REQUEST);
                apiCalls.withdrawPoints(Utils.getHeaderToken(this), withdrawPointsRequest).enqueue((Callback<WithdrawPointsResponse>) this);
                break;
            case REQ_API_TYPE_CANCEL_WITHDRAW_POINTS:
                CancelWithdrawPointsRequest cancelWithdrawPointsRequest = intent.getParcelableExtra(AppConstant.KEY_CANCEL_WITHDRAW_POINTS_REQUEST);
                apiCalls.cancelWithdrawPoints(Utils.getHeaderToken(this), cancelWithdrawPointsRequest).enqueue((Callback<CancelWithdrawPointsResponse>) this);
                break;
            case REQ_API_TYPE_GET_WITHDRAW_HISTORY:
                WithdrawHistoryRequest withdrawHistoryRequest = intent.getParcelableExtra(AppConstant.KEY_GET_WITHDRAW_HISTORY_REQUEST);
                apiCalls.getWithdrawHistory(Utils.getHeaderToken(this), withdrawHistoryRequest).enqueue((Callback<WithdrawHistoryResponse>) this);
                break;
            case REQ_API_TYPE_SEND_UPI_PAYMENT_STATUS:
                UpiPaymentStatusRequest upiPaymentStatusRequest = intent.getParcelableExtra(AppConstant.KEY_UPI_PAYMENT_INFO_STATUS);
                apiCalls.sendUPIPaymentStatus(Utils.getHeaderToken(this), upiPaymentStatusRequest).enqueue((Callback<UpiPaymentStatusResponse>) this);
                break;
//            case REQ_API_TYPE_SEND_UPI_PAYMENT_STATUS:
//                UpiPaymentStatusRequest upiPaymentStatusRequest = intent.getParcelableExtra(AppConstant.KEY_UPI_PAYMENT_INFO_STATUS);
//                apiCalls.sendUPIPaymentStatus(Utils.getHeaderToken(this), upiPaymentStatusRequest).enqueue((Callback<UpiPaymentStatusResponse>) this);
//                break;
        }
    }

    /**
     * @param response Successful response body type
     * @param <T>
     */
    protected abstract <T> void onSuccess(T response);

    /**
     * @param response
     * @param <T>
     */
    protected abstract <T> void onFailure(T response);

    /**
     * Invoked if there is no internet connectivity
     */
    protected abstract void showInternetError();

    protected void showBetConfirmationDialog() {
        if (!isFinishing()) {
            dismissCustomDualButtonDialog();
            customDualButtonDialogFragment = CustomDualButtonDialogFragment.newInstance(
                    getResources().getString(R.string.bet_confirmation_msg));
            customDualButtonDialogFragment.show(getSupportFragmentManager(), "BetConfirm");
        }
    }

    protected void dismissCustomDualButtonDialog() {
        if (!isFinishing() && customDualButtonDialogFragment != null &&
                customDualButtonDialogFragment.isVisible()) {
            customDualButtonDialogFragment.dismissAllowingStateLoss();
            customDualButtonDialogFragment = null;
        }
    }

    private void showUnauthorizedDialog() {
        dismissCustomSingleButtonDialog();
        customSingleButtonDialogFragment = CustomSingleButtonDialogFragment.newInstance(
                getResources().getString(R.string.app_unauthorized_msg));
        customSingleButtonDialogFragment.show(getSupportFragmentManager(), AppConstant.DIALOG_TAG_UNAUTHORIZED);
    }

    private void dismissCustomSingleButtonDialog() {
        if (!isFinishing() && customSingleButtonDialogFragment != null &&
                customSingleButtonDialogFragment.isVisible()) {
            customSingleButtonDialogFragment.dismissAllowingStateLoss();
            customSingleButtonDialogFragment = null;
        }
    }

    protected void navigateToLoginScreen() {
        Intent intent = new Intent(this, LoginScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected void clearAppData() {
        ASOnlinePreferenceManager.clearASOnlinePreferences(this);
    }

    protected void navigateToAppSettingsScreen() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, AppConstant.REQ_CODE_APP_SETTINGS);
    }

    protected void navigateToBaazaarHistoryScreen() {
        Intent intent = new Intent(this, BaazaarHistoryScreenActivity.class);
        startActivity(intent);
    }

    public static String getEncryptedString(String requestString) {
        String encryptedString = "";
        /*try {
            CryptLib _crypt = new CryptLib();
            Gson gson = new Gson();
            String jsonString = gson.toJson(requestString);
            *//*String key = CryptLib.SHA256(BuildConfig.KEY, 32);*//*
            encryptedString = _crypt.encrypt(jsonString, BuildConfig.KEY, BuildConfig.IV); //encrypt
            LogUtils.e("TAG", encryptedString);
            LogUtils.e("TAG", _crypt.decrypt(encryptedString, BuildConfig.KEY, BuildConfig.IV));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            CryptLib _crypt = new CryptLib();
            Gson gson = new Gson();
            String jsonString = gson.toJson(requestString);
            encryptedString = _crypt.encryption(jsonString, BuildConfig.KEY, BuildConfig.IV); //encrypt
            LogUtils.e("TAG", encryptedString);
            LogUtils.e("TAG", _crypt.decryption(encryptedString, BuildConfig.KEY, BuildConfig.IV));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return encryptedString;
    }

    //API Callback
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        showProgressDialog(false, "");
        if (response.isSuccessful() && response.body() != null) {
            onSuccess(response.body());
        } else {
            switch (response.code()) {
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    /*LogUtils.e("TAG", "UNAUTHORIZED");*/
                    showUnauthorizedDialog();
                    break;
                default:
                    if (requestType == REQ_API_TYPE_GET_BANK_DETAILS) {
                        onFailure(response);
                    } else {
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
                    break;
            }

        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        showProgressDialog(false, "");
        onFailure(t);
    }

    @Override
    public void onClickSingleButtonListener() {
        dismissCustomSingleButtonDialog();
        clearAppData();
        navigateToLoginScreen();
    }
}
