package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaazaarRemainingTimeData implements Parcelable {

    @SerializedName("remaining_time")
    @Expose
    private int remainingTime;

    public BaazaarRemainingTimeData() {

    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    protected BaazaarRemainingTimeData(Parcel in) {
        remainingTime = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(remainingTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BaazaarRemainingTimeData> CREATOR = new Parcelable.Creator<BaazaarRemainingTimeData>() {
        @Override
        public BaazaarRemainingTimeData createFromParcel(Parcel in) {
            return new BaazaarRemainingTimeData(in);
        }

        @Override
        public BaazaarRemainingTimeData[] newArray(int size) {
            return new BaazaarRemainingTimeData[size];
        }
    };
}
