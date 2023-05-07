package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HTBetHistoryDataResponse implements Parcelable {

    @SerializedName("total_data")
    @Expose
    private int totalData;
    @SerializedName("table_data")
    @Expose
    private List<HTBetHistoryData> htBetHistoryList = null;

    public HTBetHistoryDataResponse() {

    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public List<HTBetHistoryData> getHtBetHistoryList() {
        return htBetHistoryList;
    }

    public void setHtBetHistoryList(List<HTBetHistoryData> htBetHistoryList) {
        this.htBetHistoryList = htBetHistoryList;
    }

    protected HTBetHistoryDataResponse(Parcel in) {
        totalData = in.readInt();
        if (in.readByte() == 0x01) {
            htBetHistoryList = new ArrayList<HTBetHistoryData>();
            in.readList(htBetHistoryList, HTBetHistoryData.class.getClassLoader());
        } else {
            htBetHistoryList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalData);
        if (htBetHistoryList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(htBetHistoryList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTBetHistoryDataResponse> CREATOR = new Parcelable.Creator<HTBetHistoryDataResponse>() {
        @Override
        public HTBetHistoryDataResponse createFromParcel(Parcel in) {
            return new HTBetHistoryDataResponse(in);
        }

        @Override
        public HTBetHistoryDataResponse[] newArray(int size) {
            return new HTBetHistoryDataResponse[size];
        }
    };
}
