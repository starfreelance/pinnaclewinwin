package com.pinnacle.winwin.network.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "cp_paana_details")
public class CPDetailsResponse implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "cp_paana_id")
    @SerializedName("cp_paana_id")
    @Expose
    private int paanaId;

    @ColumnInfo(name = "cp_paana_no")
    @SerializedName("cp_paana_no")
    @Expose
    private String paanaNo;

    @Ignore
    private boolean isSelected;

    @Ignore
    private int pointsValue;

    public CPDetailsResponse() {

    }

    public int getPaanaId() {
        return paanaId;
    }

    public void setPaanaId(int paanaId) {
        this.paanaId = paanaId;
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

    public int getPointsValue() {
        return pointsValue;
    }

    public void setPointsValue(int pointsValue) {
        this.pointsValue = pointsValue;
    }

    protected CPDetailsResponse(Parcel in) {
        paanaId = in.readInt();
        paanaNo = in.readString();
        isSelected = in.readByte() != 0x00;
        pointsValue = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(paanaId);
        dest.writeString(paanaNo);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
        dest.writeInt(pointsValue);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CPDetailsResponse> CREATOR = new Parcelable.Creator<CPDetailsResponse>() {
        @Override
        public CPDetailsResponse createFromParcel(Parcel in) {
            return new CPDetailsResponse(in);
        }

        @Override
        public CPDetailsResponse[] newArray(int size) {
            return new CPDetailsResponse[size];
        }
    };
}
