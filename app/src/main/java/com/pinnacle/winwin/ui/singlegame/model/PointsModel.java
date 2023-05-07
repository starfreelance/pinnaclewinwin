package com.pinnacle.winwin.ui.singlegame.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PointsModel implements Parcelable {

    private String pointValue;

    public PointsModel() {

    }

    public String getPointValue() {
        return pointValue;
    }

    public void setPointValue(String pointValue) {
        this.pointValue = pointValue;
    }

    protected PointsModel(Parcel in) {
        pointValue = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pointValue);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PointsModel> CREATOR = new Parcelable.Creator<PointsModel>() {
        @Override
        public PointsModel createFromParcel(Parcel in) {
            return new PointsModel(in);
        }

        @Override
        public PointsModel[] newArray(int size) {
            return new PointsModel[size];
        }
    };
}
