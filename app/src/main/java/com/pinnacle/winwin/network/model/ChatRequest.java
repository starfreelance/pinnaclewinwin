package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatRequest implements Parcelable {

    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("message")
    @Expose
    private String message;

    public ChatRequest() {

    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected ChatRequest(Parcel in) {
        custId = in.readInt();
        message = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custId);
        dest.writeString(message);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChatRequest> CREATOR = new Parcelable.Creator<ChatRequest>() {
        @Override
        public ChatRequest createFromParcel(Parcel in) {
            return new ChatRequest(in);
        }

        @Override
        public ChatRequest[] newArray(int size) {
            return new ChatRequest[size];
        }
    };
}
