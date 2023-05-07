package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HTCurrentSlotResponse implements Parcelable {

    @SerializedName("remaining_time")
    @Expose
    private int remainingTime;
    @SerializedName("slot_no")
    @Expose
    private String slotNo;

    public HTCurrentSlotResponse() {

    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(String slotNo) {
        this.slotNo = slotNo;
    }

    protected HTCurrentSlotResponse(Parcel in) {
        remainingTime = in.readInt();
        slotNo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(remainingTime);
        dest.writeString(slotNo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTCurrentSlotResponse> CREATOR = new Parcelable.Creator<HTCurrentSlotResponse>() {
        @Override
        public HTCurrentSlotResponse createFromParcel(Parcel in) {
            return new HTCurrentSlotResponse(in);
        }

        @Override
        public HTCurrentSlotResponse[] newArray(int size) {
            return new HTCurrentSlotResponse[size];
        }
    };
}
