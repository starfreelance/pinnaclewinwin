package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HTResultRequest implements Parcelable {

    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("slot_no")
    @Expose
    private String slotNo;

    public HTResultRequest() {

    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(String slotNo) {
        this.slotNo = slotNo;
    }

    protected HTResultRequest(Parcel in) {
        custId = in.readInt();
        slotNo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custId);
        dest.writeString(slotNo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTResultRequest> CREATOR = new Parcelable.Creator<HTResultRequest>() {
        @Override
        public HTResultRequest createFromParcel(Parcel in) {
            return new HTResultRequest(in);
        }

        @Override
        public HTResultRequest[] newArray(int size) {
            return new HTResultRequest[size];
        }
    };
}
