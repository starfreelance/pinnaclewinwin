package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelWithdrawPointsData implements Parcelable {

    @SerializedName("points")
    @Expose
    private int points;

    public CancelWithdrawPointsData() {

    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    protected CancelWithdrawPointsData(Parcel in) {
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
    public static final Parcelable.Creator<CancelWithdrawPointsData> CREATOR = new Parcelable.Creator<CancelWithdrawPointsData>() {
        @Override
        public CancelWithdrawPointsData createFromParcel(Parcel in) {
            return new CancelWithdrawPointsData(in);
        }

        @Override
        public CancelWithdrawPointsData[] newArray(int size) {
            return new CancelWithdrawPointsData[size];
        }
    };
}
