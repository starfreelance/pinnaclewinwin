package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelWithdrawPointsRequest implements Parcelable {

    @SerializedName("cust_payment_id")
    @Expose
    private int custPaymentId;

    public CancelWithdrawPointsRequest() {

    }

    public int getCustPaymentId() {
        return custPaymentId;
    }

    public void setCustPaymentId(int custPaymentId) {
        this.custPaymentId = custPaymentId;
    }

    protected CancelWithdrawPointsRequest(Parcel in) {
        custPaymentId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custPaymentId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CancelWithdrawPointsRequest> CREATOR = new Parcelable.Creator<CancelWithdrawPointsRequest>() {
        @Override
        public CancelWithdrawPointsRequest createFromParcel(Parcel in) {
            return new CancelWithdrawPointsRequest(in);
        }

        @Override
        public CancelWithdrawPointsRequest[] newArray(int size) {
            return new CancelWithdrawPointsRequest[size];
        }
    };
}
