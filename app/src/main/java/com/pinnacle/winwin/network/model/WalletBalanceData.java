package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletBalanceData implements Parcelable {

    @SerializedName("points")
    @Expose
    private int points;

    public WalletBalanceData() {

    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    protected WalletBalanceData(Parcel in) {
        points = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(points);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WalletBalanceData> CREATOR = new Parcelable.Creator<WalletBalanceData>() {
        @Override
        public WalletBalanceData createFromParcel(Parcel in) {
            return new WalletBalanceData(in);
        }

        @Override
        public WalletBalanceData[] newArray(int size) {
            return new WalletBalanceData[size];
        }
    };
}
