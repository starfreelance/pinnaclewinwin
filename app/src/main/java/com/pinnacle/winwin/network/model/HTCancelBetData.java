package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HTCancelBetData implements Parcelable {

    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("points")
    @Expose
    private int points;

    public HTCancelBetData() {

    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    protected HTCancelBetData(Parcel in) {
        custId = in.readInt();
        points = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custId);
        dest.writeInt(points);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTCancelBetData> CREATOR = new Parcelable.Creator<HTCancelBetData>() {
        @Override
        public HTCancelBetData createFromParcel(Parcel in) {
            return new HTCancelBetData(in);
        }

        @Override
        public HTCancelBetData[] newArray(int size) {
            return new HTCancelBetData[size];
        }
    };
}
