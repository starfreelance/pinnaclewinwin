package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HTCancelBetRequest implements Parcelable {

    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("slot_id")
    @Expose
    private String slotId;

    public HTCancelBetRequest() {

    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    protected HTCancelBetRequest(Parcel in) {
        custId = in.readInt();
        slotId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custId);
        dest.writeString(slotId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTCancelBetRequest> CREATOR = new Parcelable.Creator<HTCancelBetRequest>() {
        @Override
        public HTCancelBetRequest createFromParcel(Parcel in) {
            return new HTCancelBetRequest(in);
        }

        @Override
        public HTCancelBetRequest[] newArray(int size) {
            return new HTCancelBetRequest[size];
        }
    };
}
