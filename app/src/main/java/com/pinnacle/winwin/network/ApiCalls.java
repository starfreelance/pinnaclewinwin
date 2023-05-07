package com.pinnacle.winwin.network;

import com.pinnacle.winwin.app.AppConstant;
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
import com.pinnacle.winwin.network.model.CountryCodeResponse;
import com.pinnacle.winwin.network.model.CustomerDetailsUpdateRequest;
import com.pinnacle.winwin.network.model.CustomerTransactionRequest;
import com.pinnacle.winwin.network.model.CustomerTransactionResponse;
import com.pinnacle.winwin.network.model.ForgotPasswordRequest;
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

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiCalls {

    @POST("auth/login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);
    /*Call<LoginResponse> userLogin(@Body ASDataRequest asDataRequest);*/

    @Headers({
            "Accept: application/json",
            /*"login-authentication: vikas"*/
            "access-token: "+ AppConstant.ACCESS_TOKEN
    })
    @POST("otp/new/customer")
    Call<OtpResponse> generateOtp(@Body OtpRequest otpRequest);

    @Headers({
            "Accept: application/json",
            /*"login-authentication: vikas"*/
            "access-token: "+ AppConstant.ACCESS_TOKEN
    })
    @POST("otp/validate/customer")
    Call<ValidateOtpResponse> validateOtp(@Body OtpRequest otpRequest);

    @Headers({
            "Accept: application/json",
            /*"login-authentication: vikas"*/
            "access-token: "+ AppConstant.ACCESS_TOKEN
    })
    @POST("customer/forgot-password")
    Call<GenericResponse> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    @POST("customer/change-password")
    Call<GenericResponse> changePassword(@Header("Authorization") String authToken, @Body ChangePasswordRequest changePasswordRequest);
    /*Call<GenericResponse> changePassword(@Header("Authorization") String authToken, @Body ASDataRequest asDataRequest);*/

    @POST("customer/update-image")
    Call<UpdateProfileImageResponse> updateProfileImage(@Header("Authorization") String authToken, @Body UpdateProfileImageRequest updateProfileImageRequest);
    /*Call<UpdateProfileImageResponse> updateProfileImage(@Header("Authorization") String authToken, @Body ASDataRequest asDataRequest);*/

    @GET("master")
    Call<MatkaMasterResponse> getMaster(@Header("Authorization") String authToken);

    @POST("bet-history/new")
    /*Call<AddNewBetResponse> addNewBet(@Header("Authorization") String authToken, @Body AddNewBetRequest addNewBetRequest);*/
    Call<AddNewBetResponse> addNewBet(@Header("Authorization") String authToken, @Body AddNewBetOtcRequest addNewBetOtcRequest);
    /*Call<AddNewBetResponse> addNewBet(@Header("Authorization") String authToken, @Body ASDataRequest asDataRequest);*/

    @POST("bet-history/bet-list")
    Call<BetHistoryResponse> getBetHistory(@Header("Authorization") String authToken, @Body BetHistoryRequest betHistoryRequest);
    /*Call<BetHistoryResponse> getBetHistory(@Header("Authorization") String authToken, @Body ASDataRequest asDataRequest);*/

    /*@GET("customer/initial-data/{id}")
    Call<MatkaInitialResponse> getInitialData(@Header("Authorization") String authToken, @Path("id") int customerId);*/
    @POST("customer/initial-data")
    Call<MatkaInitialResponse> getInitialData(@Header("Authorization") String authToken, @Body MatkaInitialRequest matkaInitialRequest);
    /*Call<MatkaInitialResponse> getInitialData(@Header("Authorization") String authToken, @Body ASDataRequest asDataRequest);*/

    /*@GET("bazaar/timing/{id}")
    Call<BaazaarRemainingTimeResponse> getBaazaarRemainingTime(@Header("Authorization") String authToken, @Path("id") int baazaarId);*/
    @POST("bazaar/timing")
    /*Call<BaazaarRemainingTimeResponse> getBaazaarRemainingTime(@Header("Authorization") String authToken, @Body ASDataRequest asDataRequest);*/
    Call<BaazaarRemainingTimeResponse> getBaazaarRemainingTime(@Header("Authorization") String authToken, @Body BaazaarRemainingTimeRequest baazaarRemainingTimeRequest);

    /*@GET("customer/points/{id}")
    Call<WalletBalanceResponse> getWalletBalance(@Header("Authorization") String authToken, @Path("id") int customerId);*/
    @POST("customer/points")
    /*Call<WalletBalanceResponse> getWalletBalance(@Header("Authorization") String authToken, @Body ASDataRequest asDataRequest);*/
    Call<WalletBalanceResponse> getWalletBalance(@Header("Authorization") String authToken, @Body WalletBalanceRequest walletBalanceRequest);

    @POST("bet-history/claim")
    Call<BetClaimResponse> claimBet(@Header("Authorization") String authToken, @Body BetClaimRequest betClaimRequest);
    /*Call<BetClaimResponse> claimBet(@Header("Authorization") String authToken, @Body ASDataRequest asDataRequest);*/

    @POST("customer/update")
    Call<GenericResponse> updateCustomerDetails(@Header("Authorization") String authToken, @Body CustomerDetailsUpdateRequest customerDetailsUpdateRequest);
    /*Call<GenericResponse> updateCustomerDetails(@Header("Authorization") String authToken, @Body ASDataRequest asDataRequest);*/

    /*@POST("transactions/customer")
    Call<CustomerTransactionResponse> getCustomerTransactions(@Header("Authorization") String authToken, @Body CustomerTransactionRequest customerTransactionRequest);*/

    @POST("customer-transactions")
    Call<CustomerTransactionResponse> getCustomerTransactions(@Header("Authorization") String authToken, @Body CustomerTransactionRequest customerTransactionRequest);
    /*Call<CustomerTransactionResponse> getCustomerTransactions(@Header("Authorization") String authToken, @Body ASDataRequest asDataRequest);*/

    @POST("bazaar-history")
    Call<BaazaarHistoryResponse> getBaazaarHistory(@Header("Authorization") String authToken, @Body BaazaarHistoryRequest baazaarHistoryRequest);

    @POST("ht-slot/initial-data")
    Call<HTInitialResponse> getHTInitialData(@Header("Authorization") String authToken, @Body HTInitialRequest htInitialRequest);

    @POST("ht-bet-history/new")
    Call<AddHTNewBetResponse> addHTNewBet(@Header("Authorization") String authToken, @Body AddHTNewBetRequest addHTNewBetRequest);

    @POST("ht-results")
    Call<HTResultResponse> getHTResult(@Header("Authorization") String authToken, @Body HTResultRequest htResultRequest);

    @POST("ht-bet-history/cancel")
    Call<HTCancelBetResponse> cancelHTBet(@Header("Authorization") String authToken, @Body HTCancelBetRequest htCancelBetRequest);

    @POST("ht-bet-history/ht-bet-list")
    Call<HTBetHistoryResponse> getHTBetHistory(@Header("Authorization") String authToken, @Body HTBetHistoryRequest htBetHistoryRequest);

    @Headers({
            "Accept: application/json",
            "access-token: "+ AppConstant.ACCESS_TOKEN
    })
    @POST("auth/signup")
    Call<SignUpResponse> userSignUp(@Body SignUpRequest signUpRequest);

    @POST("chat/new/customer")
    Call<ChatResponse> sendMessage(@Header("Authorization") String authToken, @Body ChatRequest chatRequest);

    @POST("chat/thread")
    Call<ChatThreadResponse> getChatThread(@Header("Authorization") String authToken, @Body ChatThreadRequest chatThreadRequest);

    @POST("payment/customer")
    Call<WalletRechargeResponse> walletRecharge(@Header("Authorization") String authToken, @Body WalletRechargeRequest walletRechargeRequest);

    @POST("customer/update-kyc")
    Call<KycResponse> updateKyc(@Header("Authorization") String authToken, @Body KycRequest kycRequest);

//    @POST("payment/customers/all")
    @POST("/upi-payment/getLast10UpiTransaction")
//    Call<String> getRechargeHistory(@Header("Authorization") String authToken, @Body RechargeHistoryRequest rechargeHistoryRequest);
    Call<RechargeHistoryResponse> getRechargeHistory(@Header("Authorization") String authToken, @Body RechargeHistoryRequest rechargeHistoryRequest);

    @POST("customer-payment/inititate")
    Call<WithdrawPointsResponse> withdrawPoints(@Header("Authorization") String authToken, @Body WithdrawPointsRequest withdrawPointsRequest);

    @POST("customer-payment/cancel")
    Call<CancelWithdrawPointsResponse> cancelWithdrawPoints(@Header("Authorization") String authToken, @Body CancelWithdrawPointsRequest cancelWithdrawPointsRequest);

    @POST("customer-payment/all")
    Call<WithdrawHistoryResponse> getWithdrawHistory(@Header("Authorization") String authToken, @Body WithdrawHistoryRequest withdrawHistoryRequest);

    @Headers({
            "Accept: application/json",
            /*"login-authentication: vikas"*/
            "access-token: "+ AppConstant.ACCESS_TOKEN
    })
    @GET("country-code")
    Call<CountryCodeResponse> getCountryCodeList();

    @GET
    Call<IfscResponse> getBankDetails(@Url String url);

    @POST("/upi-payment/UpiPayment")
    Call<UpiPaymentStatusResponse> sendUPIPaymentStatus(@Header("Authorization") String authToken, @Body UpiPaymentStatusRequest upiPaymentStatusRequest);

    @POST("/upi-payment/getLast10UpiTransaction")
    Call<String> getLast10UPITransaction(@Header("Authorization") String authToken, @Body UpiPaymentStatusRequest upiPaymentStatusRequest);
}
