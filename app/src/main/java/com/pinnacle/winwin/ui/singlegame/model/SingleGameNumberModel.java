package com.pinnacle.winwin.ui.singlegame.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SingleGameNumberModel implements Parcelable {

    private String number;
    private int pointsValue;
    private boolean isSelected;
    private int selectedIndexPosition = -1;
    private int combinationCount;
    private int combinationPointsValue;

    public SingleGameNumberModel() {

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getPointsValue() {
        return pointsValue;
    }

    public void setPointsValue(int pointsValue) {
        this.pointsValue = pointsValue;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getSelectedIndexPosition() {
        return selectedIndexPosition;
    }

    public void setSelectedIndexPosition(int selectedIndexPosition) {
        this.selectedIndexPosition = selectedIndexPosition;
    }

    public int getCombinationCount() {
        return combinationCount;
    }

    public void setCombinationCount(int combinationCount) {
        this.combinationCount = combinationCount;
    }

    public int getCombinationPointsValue() {
        return combinationPointsValue;
    }

    public void setCombinationPointsValue(int combinationPointsValue) {
        this.combinationPointsValue = combinationPointsValue;
    }

    protected SingleGameNumberModel(Parcel in) {
        number = in.readString();
        pointsValue = in.readInt();
        selectedIndexPosition = in.readInt();
        isSelected = in.readByte() != 0x00;
        combinationCount = in.readInt();
        combinationPointsValue = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeInt(pointsValue);
        dest.writeInt(selectedIndexPosition);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
        dest.writeInt(combinationCount);
        dest.writeInt(combinationPointsValue);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SingleGameNumberModel> CREATOR = new Parcelable.Creator<SingleGameNumberModel>() {
        @Override
        public SingleGameNumberModel createFromParcel(Parcel in) {
            return new SingleGameNumberModel(in);
        }

        @Override
        public SingleGameNumberModel[] newArray(int size) {
            return new SingleGameNumberModel[size];
        }
    };
}
