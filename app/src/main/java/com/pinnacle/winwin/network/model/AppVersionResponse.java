package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppVersionResponse implements Parcelable {

    @SerializedName("app_version_id")
    @Expose
    private int appVersionId;

    public AppVersionResponse() {

    }

    public int getAppVersionId() {
        return appVersionId;
    }

    public void setAppVersionId(int appVersionId) {
        this.appVersionId = appVersionId;
    }

    protected AppVersionResponse(Parcel in) {
        appVersionId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(appVersionId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AppVersionResponse> CREATOR = new Parcelable.Creator<AppVersionResponse>() {
        @Override
        public AppVersionResponse createFromParcel(Parcel in) {
            return new AppVersionResponse(in);
        }

        @Override
        public AppVersionResponse[] newArray(int size) {
            return new AppVersionResponse[size];
        }
    };
}
