package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WithdrawalHistoryDataResponse implements Parcelable {

    @SerializedName("total_data")
    @Expose
    private int totalData;
    @SerializedName("table_data")
    @Expose
    private List<WithdrawalHistoryData> withdrawalHistoryDataList = null;

    public WithdrawalHistoryDataResponse() {

    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public List<WithdrawalHistoryData> getWithdrawalHistoryDataList() {
        return withdrawalHistoryDataList;
    }

    public void setWithdrawalHistoryDataList(List<WithdrawalHistoryData> withdrawalHistoryDataList) {
        this.withdrawalHistoryDataList = withdrawalHistoryDataList;
    }

    protected WithdrawalHistoryDataResponse(Parcel in) {
        totalData = in.readInt();
        if (in.readByte() == 0x01) {
            withdrawalHistoryDataList = new ArrayList<WithdrawalHistoryData>();
            in.readList(withdrawalHistoryDataList, WithdrawalHistoryData.class.getClassLoader());
        } else {
            withdrawalHistoryDataList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalData);
        if (withdrawalHistoryDataList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(withdrawalHistoryDataList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WithdrawalHistoryDataResponse> CREATOR = new Parcelable.Creator<WithdrawalHistoryDataResponse>() {
        @Override
        public WithdrawalHistoryDataResponse createFromParcel(Parcel in) {
            return new WithdrawalHistoryDataResponse(in);
        }

        @Override
        public WithdrawalHistoryDataResponse[] newArray(int size) {
            return new WithdrawalHistoryDataResponse[size];
        }
    };
}
