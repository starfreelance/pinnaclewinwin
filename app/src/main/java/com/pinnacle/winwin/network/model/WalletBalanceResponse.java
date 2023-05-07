package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletBalanceResponse implements Parcelable {

    @SerializedName("statusCode")
    @Expose
    private int statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("data")
    @Expose
    private WalletBalanceData walletBalanceData;

    public WalletBalanceResponse() {

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

    public WalletBalanceData getWalletBalanceData() {
        return walletBalanceData;
    }

    public void setWalletBalanceData(WalletBalanceData walletBalanceData) {
        this.walletBalanceData = walletBalanceData;
    }

    protected WalletBalanceResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        walletBalanceData = (WalletBalanceData) in.readValue(WalletBalanceData.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(statusCode);
        dest.writeString(statusMessage);
        dest.writeValue(walletBalanceData);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WalletBalanceResponse> CREATOR = new Parcelable.Creator<WalletBalanceResponse>() {
        @Override
        public WalletBalanceResponse createFromParcel(Parcel in) {
            return new WalletBalanceResponse(in);
        }

        @Override
        public WalletBalanceResponse[] newArray(int size) {
            return new WalletBalanceResponse[size];
        }
    };
}
