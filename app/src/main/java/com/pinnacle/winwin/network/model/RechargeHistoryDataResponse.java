package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RechargeHistoryDataResponse implements Parcelable {

    @SerializedName("total_data")
    @Expose
    private int totalData;
    @SerializedName("table_data")
    @Expose
    private List<RechargeHistoryData> rechargeHistoryDataList = null;

    public RechargeHistoryDataResponse() {

    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public List<RechargeHistoryData> getRechargeHistoryDataList() {
        return rechargeHistoryDataList;
    }

    public void setRechargeHistoryDataList(List<RechargeHistoryData> rechargeHistoryDataList) {
        this.rechargeHistoryDataList = rechargeHistoryDataList;
    }

    protected RechargeHistoryDataResponse(Parcel in) {
        totalData = in.readInt();
        if (in.readByte() == 0x01) {
            rechargeHistoryDataList = new ArrayList<RechargeHistoryData>();
            in.readList(rechargeHistoryDataList, RechargeHistoryData.class.getClassLoader());
        } else {
            rechargeHistoryDataList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalData);
        if (rechargeHistoryDataList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(rechargeHistoryDataList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RechargeHistoryDataResponse> CREATOR = new Parcelable.Creator<RechargeHistoryDataResponse>() {
        @Override
        public RechargeHistoryDataResponse createFromParcel(Parcel in) {
            return new RechargeHistoryDataResponse(in);
        }

        @Override
        public RechargeHistoryDataResponse[] newArray(int size) {
            return new RechargeHistoryDataResponse[size];
        }
    };
}
