package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WithdrawalHistoryData implements Parcelable {

    @SerializedName("cust_payment_id")
    @Expose
    private int custPaymentId;
    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("initiated_amount")
    @Expose
    private int initiatedAmount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public WithdrawalHistoryData() {

    }

    public int getCustPaymentId() {
        return custPaymentId;
    }

    public void setCustPaymentId(int custPaymentId) {
        this.custPaymentId = custPaymentId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected WithdrawalHistoryData(Parcel in) {
        custPaymentId = in.readInt();
        custId = in.readInt();
        initiatedAmount = in.readInt();
        status = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custPaymentId);
        dest.writeInt(custId);
        dest.writeInt(initiatedAmount);
        dest.writeString(status);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WithdrawalHistoryData> CREATOR = new Parcelable.Creator<WithdrawalHistoryData>() {
        @Override
        public WithdrawalHistoryData createFromParcel(Parcel in) {
            return new WithdrawalHistoryData(in);
        }

        @Override
        public WithdrawalHistoryData[] newArray(int size) {
            return new WithdrawalHistoryData[size];
        }
    };
}
