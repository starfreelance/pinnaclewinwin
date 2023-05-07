package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletBalanceRequest implements Parcelable {

    @SerializedName("id")
    @Expose
    private int customerId;

    public WalletBalanceRequest() {

    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    protected WalletBalanceRequest(Parcel in) {
        customerId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(customerId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WalletBalanceRequest> CREATOR = new Parcelable.Creator<WalletBalanceRequest>() {
        @Override
        public WalletBalanceRequest createFromParcel(Parcel in) {
            return new WalletBalanceRequest(in);
        }

        @Override
        public WalletBalanceRequest[] newArray(int size) {
            return new WalletBalanceRequest[size];
        }
    };
}
