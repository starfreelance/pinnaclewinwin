package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HTResultData implements Parcelable {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("updated_points")
    @Expose
    private int updatedPoints;
    @SerializedName("winning_amount")
    @Expose
    private int winningAmount;

    public HTResultData() {

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getUpdatedPoints() {
        return updatedPoints;
    }

    public void setUpdatedPoints(int updatedPoints) {
        this.updatedPoints = updatedPoints;
    }

    public int getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(int winningAmount) {
        this.winningAmount = winningAmount;
    }

    protected HTResultData(Parcel in) {
        result = in.readString();
        updatedPoints = in.readInt();
        winningAmount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result);
        dest.writeInt(updatedPoints);
        dest.writeInt(winningAmount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTResultData> CREATOR = new Parcelable.Creator<HTResultData>() {
        @Override
        public HTResultData createFromParcel(Parcel in) {
            return new HTResultData(in);
        }

        @Override
        public HTResultData[] newArray(int size) {
            return new HTResultData[size];
        }
    };
}
