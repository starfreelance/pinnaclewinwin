package com.pinnacle.winwin.ui.baazaar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BaazaarModel implements Parcelable {

    private int baazaarType;
    private int imgResource;
    private String baazaarName;
    private String baazaarTime;
    private String baazaarPastResult;

    public BaazaarModel() {

    }

    public int getBaazaarType() {
        return baazaarType;
    }

    public void setBaazaarType(int baazaarType) {
        this.baazaarType = baazaarType;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public String getBaazaarName() {
        return baazaarName;
    }

    public void setBaazaarName(String baazaarName) {
        this.baazaarName = baazaarName;
    }

    public String getBaazaarTime() {
        return baazaarTime;
    }

    public void setBaazaarTime(String baazaarTime) {
        this.baazaarTime = baazaarTime;
    }

    public String getBaazaarPastResult() {
        return baazaarPastResult;
    }

    public void setBaazaarPastResult(String baazaarPastResult) {
        this.baazaarPastResult = baazaarPastResult;
    }

    protected BaazaarModel(Parcel in) {
        baazaarType = in.readInt();
        imgResource = in.readInt();
        baazaarName = in.readString();
        baazaarTime = in.readString();
        baazaarPastResult = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(baazaarType);
        dest.writeInt(imgResource);
        dest.writeString(baazaarName);
        dest.writeString(baazaarTime);
        dest.writeString(baazaarPastResult);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BaazaarModel> CREATOR = new Parcelable.Creator<BaazaarModel>() {
        @Override
        public BaazaarModel createFromParcel(Parcel in) {
            return new BaazaarModel(in);
        }

        @Override
        public BaazaarModel[] newArray(int size) {
            return new BaazaarModel[size];
        }
    };
}
