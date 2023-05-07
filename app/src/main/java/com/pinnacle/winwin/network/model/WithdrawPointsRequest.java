package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WithdrawPointsRequest implements Parcelable {

    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("initiated_amount")
    @Expose
    private int initiatedAmount;

    public WithdrawPointsRequest() {

    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getInitiatedAmount() {
        return initiatedAmount;
    }

    public void setInitiatedAmount(int initiatedAmount) {
        this.initiatedAmount = initiatedAmount;
    }

    protected WithdrawPointsRequest(Parcel in) {
        custId = in.readInt();
        initiatedAmount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custId);
        dest.writeInt(initiatedAmount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WithdrawPointsRequest> CREATOR = new Parcelable.Creator<WithdrawPointsRequest>() {
        @Override
        public WithdrawPointsRequest createFromParcel(Parcel in) {
            return new WithdrawPointsRequest(in);
        }

        @Override
        public WithdrawPointsRequest[] newArray(int size) {
            return new WithdrawPointsRequest[size];
        }
    };
}
