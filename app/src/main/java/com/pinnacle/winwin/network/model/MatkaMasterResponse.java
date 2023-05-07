package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatkaMasterResponse implements Parcelable {

    @SerializedName("statusCode")
    @Expose
    private int statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("data")
    @Expose
    private MasterDataResponse masterDataResponse;

    public MatkaMasterResponse() {

    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public MasterDataResponse getMasterDataResponse() {
        return masterDataResponse;
    }

    public void setMasterDataResponse(MasterDataResponse masterDataResponse) {
        this.masterDataResponse = masterDataResponse;
    }

    protected MatkaMasterResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        masterDataResponse = (MasterDataResponse) in.readValue(MasterDataResponse.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(statusCode);
        dest.writeString(statusMessage);
        dest.writeValue(masterDataResponse);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MatkaMasterResponse> CREATOR = new Parcelable.Creator<MatkaMasterResponse>() {
        @Override
        public MatkaMasterResponse createFromParcel(Parcel in) {
            return new MatkaMasterResponse(in);
        }

        @Override
        public MatkaMasterResponse[] newArray(int size) {
            return new MatkaMasterResponse[size];
        }
    };
}
