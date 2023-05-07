package com.pinnacle.winwin.network.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "paana_details")
public class PaanaDetailsResponse implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "paana_id")
    @SerializedName("paana_id")
    @Expose
    private int paanaId;

    @ColumnInfo(name = "single_value")
    @SerializedName("single_value")
    @Expose
    private int singleValue;

    @ColumnInfo(name = "paana_no")
    @SerializedName("paana_no")
    @Expose
    private String paanaNo;

    @ColumnInfo(name = "paana_type")
    @SerializedName("paana_type")
    @Expose
    private String paanaType;

    @Ignore
    private boolean isSelected;

    @Ignore
    private int pointsValue;

    public PaanaDetailsResponse() {

    }

    public int getPaanaId() {
        return paanaId;
    }

    public void setPaanaId(int paanaId) {
        this.paanaId = paanaId;
    }

    public int getSingleValue() {
        return singleValue;
    }

    public void setSingleValue(int singleValue) {
        this.singleValue = singleValue;
    }

    public String getPaanaNo() {
        return paanaNo;
    }

    public void setPaanaNo(String paanaNo) {
        this.paanaNo = paanaNo;
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

    protected PaanaDetailsResponse(Parcel in) {
        paanaId = in.readInt();
        singleValue = in.readInt();
        paanaNo = in.readString();
        paanaType = in.readString();
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
        dest.writeInt(singleValue);
        dest.writeString(paanaNo);
        dest.writeString(paanaType);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
        dest.writeInt(pointsValue);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PaanaDetailsResponse> CREATOR = new Parcelable.Creator<PaanaDetailsResponse>() {
        @Override
        public PaanaDetailsResponse createFromParcel(Parcel in) {
            return new PaanaDetailsResponse(in);
        }

        @Override
        public PaanaDetailsResponse[] newArray(int size) {
            return new PaanaDetailsResponse[size];
        }
    };
}
