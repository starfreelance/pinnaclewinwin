package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletRechargeRequest implements Parcelable {

    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("amount")
    @Expose
    private int amount;

    public WalletRechargeRequest() {

    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    protected WalletRechargeRequest(Parcel in) {
        custId = in.readInt();
        amount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custId);
        dest.writeInt(amount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WalletRechargeRequest> CREATOR = new Parcelable.Creator<WalletRechargeRequest>() {
        @Override
        public WalletRechargeRequest createFromParcel(Parcel in) {
            return new WalletRechargeRequest(in);
        }

        @Override
        public WalletRechargeRequest[] newArray(int size) {
            return new WalletRechargeRequest[size];
        }
    };
}
