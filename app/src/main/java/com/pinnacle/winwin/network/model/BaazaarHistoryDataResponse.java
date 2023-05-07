package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BaazaarHistoryDataResponse implements Parcelable {

    @SerializedName("total_data")
    @Expose
    private Integer totalData;
    @SerializedName("table_data")
    @Expose
    private List<BaazaarHistoryData> baazaarHistoryList = null;

    public BaazaarHistoryDataResponse() {

    }

    public Integer getTotalData() {
        return totalData;
    }

    public void setTotalData(Integer totalData) {
        this.totalData = totalData;
    }

    public List<BaazaarHistoryData> getBaazaarHistoryList() {
        return baazaarHistoryList;
    }

    public void setBaazaarHistoryList(List<BaazaarHistoryData> baazaarHistoryList) {
        this.baazaarHistoryList = baazaarHistoryList;
    }

    protected BaazaarHistoryDataResponse(Parcel in) {
        totalData = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            baazaarHistoryList = new ArrayList<BaazaarHistoryData>();
            in.readList(baazaarHistoryList, BaazaarHistoryData.class.getClassLoader());
        } else {
            baazaarHistoryList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (totalData == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(totalData);
        }
        if (baazaarHistoryList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(baazaarHistoryList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BaazaarHistoryDataResponse> CREATOR = new Parcelable.Creator<BaazaarHistoryDataResponse>() {
        @Override
        public BaazaarHistoryDataResponse createFromParcel(Parcel in) {
            return new BaazaarHistoryDataResponse(in);
        }

        @Override
        public BaazaarHistoryDataResponse[] newArray(int size) {
            return new BaazaarHistoryDataResponse[size];
        }
    };
}
