package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BetHistoryDataResponse implements Parcelable {

    @SerializedName("total_data")
    @Expose
    private int totalData;
    @SerializedName("table_data")
    @Expose
    private List<BetHistoryData> betHistoryList = null;

    public BetHistoryDataResponse() {

    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public List<BetHistoryData> getBetHistoryList() {
        return betHistoryList;
    }

    public void setBetHistoryList(List<BetHistoryData> betHistoryList) {
        this.betHistoryList = betHistoryList;
    }

    protected BetHistoryDataResponse(Parcel in) {
        totalData = in.readInt();
        if (in.readByte() == 0x01) {
            betHistoryList = new ArrayList<BetHistoryData>();
            in.readList(betHistoryList, BetHistoryData.class.getClassLoader());
        } else {
            betHistoryList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalData);
        if (betHistoryList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(betHistoryList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BetHistoryDataResponse> CREATOR = new Parcelable.Creator<BetHistoryDataResponse>() {
        @Override
        public BetHistoryDataResponse createFromParcel(Parcel in) {
            return new BetHistoryDataResponse(in);
        }

        @Override
        public BetHistoryDataResponse[] newArray(int size) {
            return new BetHistoryDataResponse[size];
        }
    };
}
