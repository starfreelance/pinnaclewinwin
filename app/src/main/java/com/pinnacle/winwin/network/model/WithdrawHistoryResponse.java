package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WithdrawHistoryResponse implements Parcelable {

    @SerializedName("statusCode")
    @Expose
    private int statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private WithdrawalHistoryDataResponse withdrawalHistoryDataResponse;

    public WithdrawHistoryResponse() {

    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WithdrawalHistoryDataResponse getWithdrawalHistoryDataResponse() {
        return withdrawalHistoryDataResponse;
    }

    public void setWithdrawalHistoryDataResponse(WithdrawalHistoryDataResponse withdrawalHistoryDataResponse) {
        this.withdrawalHistoryDataResponse = withdrawalHistoryDataResponse;
    }

    protected WithdrawHistoryResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        withdrawalHistoryDataResponse = (WithdrawalHistoryDataResponse) in.readValue(WithdrawalHistoryDataResponse.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(statusCode);
        dest.writeString(statusMessage);
        dest.writeString(message);
        dest.writeValue(withdrawalHistoryDataResponse);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WithdrawHistoryResponse> CREATOR = new Parcelable.Creator<WithdrawHistoryResponse>() {
        @Override
        public WithdrawHistoryResponse createFromParcel(Parcel in) {
            return new WithdrawHistoryResponse(in);
        }

        @Override
        public WithdrawHistoryResponse[] newArray(int size) {
            return new WithdrawHistoryResponse[size];
        }
    };
}
