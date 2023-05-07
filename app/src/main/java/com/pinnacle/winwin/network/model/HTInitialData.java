package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HTInitialData implements Parcelable {

    @SerializedName("current_slot")
    @Expose
    private HTCurrentSlotResponse htCurrentSlotResponse;
    @SerializedName("last_results")
    @Expose
    private List<HTLastResultResponse> htLastResults = null;
    @SerializedName("existing_bet")
    @Expose
    private List<HTExistingBetResponse> htExistingBets = null;
    @SerializedName("interval_time")
    @Expose
    private int intervalTime;
    @SerializedName("close_before")
    @Expose
    private int closeBefore;
    @SerializedName("maximum_amount")
    @Expose
    private int maximumAmount;

    public HTInitialData() {

    }

    public HTCurrentSlotResponse getHtCurrentSlotResponse() {
        return htCurrentSlotResponse;
    }

    public void setHtCurrentSlotResponse(HTCurrentSlotResponse htCurrentSlotResponse) {
        this.htCurrentSlotResponse = htCurrentSlotResponse;
    }

    public List<HTLastResultResponse> getHtLastResults() {
        return htLastResults;
    }

    public void setHtLastResults(List<HTLastResultResponse> htLastResults) {
        this.htLastResults = htLastResults;
    }

    public List<HTExistingBetResponse> getHtExistingBets() {
        return htExistingBets;
    }

    public void setHtExistingBets(List<HTExistingBetResponse> htExistingBets) {
        this.htExistingBets = htExistingBets;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public int getCloseBefore() {
        return closeBefore;
    }

    public void setCloseBefore(int closeBefore) {
        this.closeBefore = closeBefore;
    }

    public int getMaximumAmount() {
        return maximumAmount;
    }

    public void setMaximumAmount(int maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    protected HTInitialData(Parcel in) {
        htCurrentSlotResponse = (HTCurrentSlotResponse) in.readValue(HTCurrentSlotResponse.class.getClassLoader());
        if (in.readByte() == 0x01) {
            htLastResults = new ArrayList<HTLastResultResponse>();
            in.readList(htLastResults, HTLastResultResponse.class.getClassLoader());
        } else {
            htLastResults = null;
        }
        if (in.readByte() == 0x01) {
            htExistingBets = new ArrayList<HTExistingBetResponse>();
            in.readList(htExistingBets, HTExistingBetResponse.class.getClassLoader());
        } else {
            htExistingBets = null;
        }
        intervalTime = in.readInt();
        closeBefore = in.readInt();
        maximumAmount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(htCurrentSlotResponse);
        if (htLastResults == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(htLastResults);
        }
        if (htExistingBets == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(htExistingBets);
        }
        dest.writeInt(intervalTime);
        dest.writeInt(closeBefore);
        dest.writeInt(maximumAmount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTInitialData> CREATOR = new Parcelable.Creator<HTInitialData>() {
        @Override
        public HTInitialData createFromParcel(Parcel in) {
            return new HTInitialData(in);
        }

        @Override
        public HTInitialData[] newArray(int size) {
            return new HTInitialData[size];
        }
    };
}
