package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HTNewBet implements Parcelable {

    @SerializedName("total_amount")
    @Expose
    private int totalAmount;
    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("bet_type")
    @Expose
    private String betType;

    public HTNewBet() {

    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getBetType() {
        return betType;
    }

    public void setBetType(String betType) {
        this.betType = betType;
    }

    protected HTNewBet(Parcel in) {
        totalAmount = in.readInt();
        custId = in.readInt();
        betType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalAmount);
        dest.writeInt(custId);
        dest.writeString(betType);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTNewBet> CREATOR = new Parcelable.Creator<HTNewBet>() {
        @Override
        public HTNewBet createFromParcel(Parcel in) {
            return new HTNewBet(in);
        }

        @Override
        public HTNewBet[] newArray(int size) {
            return new HTNewBet[size];
        }
    };
}
