package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpiPaymentStatusData implements Parcelable {

    @SerializedName("transaction_id")
    @Expose
    private int transactionId;

    @SerializedName("cust_id")
    @Expose
    private int custId;

    @SerializedName("transaction_no")
    @Expose
    private String transactionNo;

    @SerializedName("transaction_type")
    @Expose
    private String transactionType;

    @SerializedName("amount_point")
    @Expose
    private int Points;

    public UpiPaymentStatusData() {

    }

    protected UpiPaymentStatusData(Parcel in) {
        transactionId = in.readInt();
        custId = in.readInt();
        transactionNo = in.readString();
        transactionType = in.readString();
        Points = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(transactionId);
        dest.writeInt(custId);
        dest.writeString(transactionNo);
        dest.writeString(transactionType);
        dest.writeInt(Points);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UpiPaymentStatusData> CREATOR = new Creator<UpiPaymentStatusData>() {
        @Override
        public UpiPaymentStatusData createFromParcel(Parcel in) {
            return new UpiPaymentStatusData(in);
        }

        @Override
        public UpiPaymentStatusData[] newArray(int size) {
            return new UpiPaymentStatusData[size];
        }
    };

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }
}
