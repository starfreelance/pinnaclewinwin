package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "chart_details")
public class ChartDetailsResponse implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "chart_id")
    @SerializedName("chart_id")
    @Expose
    private int chartId;

    @ColumnInfo(name = "game_no")
    @SerializedName("game_no")
    @Expose
    private String gameNo;

    @ColumnInfo(name = "single_value")
    @SerializedName("single_value")
    @Expose
    private String singleValue;

    @ColumnInfo(name = "chart_name")
    @SerializedName("chart_name")
    @Expose
    private String chartName;

    @ColumnInfo(name = "paana_no")
    @SerializedName("paana_no")
    @Expose
    private String paanaNo;

    @Ignore
    private boolean isSelected;

    public ChartDetailsResponse() {

    }

    public int getChartId() {
        return chartId;
    }

    public void setChartId(int chartId) {
        this.chartId = chartId;
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

    public String getPaanaNo() {
        return paanaNo;
    }

    public void setPaanaNo(String paanaNo) {
        this.paanaNo = paanaNo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    protected ChartDetailsResponse(Parcel in) {
        chartId = in.readInt();
        gameNo = in.readString();
        singleValue = in.readString();
        chartName = in.readString();
        paanaNo = in.readString();
        isSelected = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(chartId);
        dest.writeString(gameNo);
        dest.writeString(singleValue);
        dest.writeString(chartName);
        dest.writeString(paanaNo);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChartDetailsResponse> CREATOR = new Parcelable.Creator<ChartDetailsResponse>() {
        @Override
        public ChartDetailsResponse createFromParcel(Parcel in) {
            return new ChartDetailsResponse(in);
        }

        @Override
        public ChartDetailsResponse[] newArray(int size) {
            return new ChartDetailsResponse[size];
        }
    };
}
