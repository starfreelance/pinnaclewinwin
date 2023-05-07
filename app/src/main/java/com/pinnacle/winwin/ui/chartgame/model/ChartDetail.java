package com.pinnacle.winwin.ui.chartgame.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class ChartDetail implements Parcelable {

    private String gameNo;
    private String singleValue;
    private String chartName;
    private int pointsValue;
    private boolean isSelected;
    private int totalPoint;
    private int combinationCount;

    public ChartDetail() {

    }

    public String getGameNo() {
        return gameNo;
    }

    public void setGameNo(String gameNo) {
        this.gameNo = gameNo;
    }

    public String getSingleValue() {
        return singleValue;
    }

    public void setSingleValue(String singleValue) {
        this.singleValue = singleValue;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
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

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public int getCombinationCount() {
        return combinationCount;
    }

    public void setCombinationCount(int combinationCount) {
        this.combinationCount = combinationCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChartDetail that = (ChartDetail) o;
        return singleValue.equals(that.singleValue);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(singleValue);
    }

    protected ChartDetail(Parcel in) {
        gameNo = in.readString();
        singleValue = in.readString();
        chartName = in.readString();
        pointsValue = in.readInt();
        isSelected = in.readByte() != 0x00;
        totalPoint = in.readInt();
        combinationCount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gameNo);
        dest.writeString(singleValue);
        dest.writeString(chartName);
        dest.writeInt(pointsValue);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
        dest.writeInt(totalPoint);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChartDetail> CREATOR = new Parcelable.Creator<ChartDetail>() {
        @Override
        public ChartDetail createFromParcel(Parcel in) {
            return new ChartDetail(in);
        }

        @Override
        public ChartDetail[] newArray(int size) {
            return new ChartDetail[size];
        }
    };
}
