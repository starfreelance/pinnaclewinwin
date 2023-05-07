package com.pinnacle.winwin.ui.paanagame.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PaanaGroupModel implements Parcelable {

    private String paanaType;
    private boolean isSelected;
    private int pointsValue;
    private int totalPaanaPoints;

    public PaanaGroupModel() {

    }

    public String getPaanaType() {
        return paanaType;
    }

    public void setPaanaType(String paanaType) {
        this.paanaType = paanaType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPointsValue() {
        return pointsValue;
    }

    public void setPointsValue(int pointsValue) {
        this.pointsValue = pointsValue;
    }

    public int getTotalPaanaPoints() {
        return totalPaanaPoints;
    }

    public void setTotalPaanaPoints(int totalPaanaPoints) {
        this.totalPaanaPoints = totalPaanaPoints;
    }

    protected PaanaGroupModel(Parcel in) {
        paanaType = in.readString();
        isSelected = in.readByte() != 0x00;
        pointsValue = in.readInt();
        totalPaanaPoints = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paanaType);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
        dest.writeInt(pointsValue);
        dest.writeInt(totalPaanaPoints);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PaanaGroupModel> CREATOR = new Parcelable.Creator<PaanaGroupModel>() {
        @Override
        public PaanaGroupModel createFromParcel(Parcel in) {
            return new PaanaGroupModel(in);
        }

        @Override
        public PaanaGroupModel[] newArray(int size) {
            return new PaanaGroupModel[size];
        }
    };
}
