package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WithdrawalPointsData implements Parcelable {

    @SerializedName("points")
    @Expose
    private int points;

    public WithdrawalPointsData() {

    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    protected WithdrawalPointsData(Parcel in) {
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
    public static final Parcelable.Creator<WithdrawalPointsData> CREATOR = new Parcelable.Creator<WithdrawalPointsData>() {
        @Override
        public WithdrawalPointsData createFromParcel(Parcel in) {
            return new WithdrawalPointsData(in);
        }

        @Override
        public WithdrawalPointsData[] newArray(int size) {
            return new WithdrawalPointsData[size];
        }
    };
}
