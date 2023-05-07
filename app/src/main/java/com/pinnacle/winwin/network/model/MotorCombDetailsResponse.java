package com.pinnacle.winwin.network.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "motor_comb_details")
public class MotorCombDetailsResponse implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "motor_comb_id")
    @SerializedName("motor_comb_id")
    @Expose
    private int motorCombId;


    @SerializedName("paana_count")
    @Expose
    private int paanaCount;

    @ColumnInfo(name = "sp_comb")
    @SerializedName("sp_comb")
    @Expose
    private int spComb;

    @ColumnInfo(name = "dp_comb")
    @SerializedName("dp_comb")
    @Expose
    private int dpComb;

    @ColumnInfo(name = "tp_comb")
    @SerializedName("tp_comb")
    @Expose
    private int tpComb;

    public MotorCombDetailsResponse() {

    }

    public int getMotorCombId() {
        return motorCombId;
    }

    public void setMotorCombId(int motorCombId) {
        this.motorCombId = motorCombId;
    }

    public int getPaanaCount() {
        return paanaCount;
    }

    public void setPaanaCount(int paanaCount) {
        this.paanaCount = paanaCount;
    }

    public int getSpComb() {
        return spComb;
    }

    public void setSpComb(int spComb) {
        this.spComb = spComb;
    }

    public int getDpComb() {
        return dpComb;
    }

    public void setDpComb(int dpComb) {
        this.dpComb = dpComb;
    }

    public int getTpComb() {
        return tpComb;
    }

    public void setTpComb(int tpComb) {
        this.tpComb = tpComb;
    }

    protected MotorCombDetailsResponse(Parcel in) {
        motorCombId = in.readInt();
        paanaCount = in.readInt();
        spComb = in.readInt();
        dpComb = in.readInt();
        tpComb = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(motorCombId);
        dest.writeInt(paanaCount);
        dest.writeInt(spComb);
        dest.writeInt(dpComb);
        dest.writeInt(tpComb);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MotorCombDetailsResponse> CREATOR = new Parcelable.Creator<MotorCombDetailsResponse>() {
        @Override
        public MotorCombDetailsResponse createFromParcel(Parcel in) {
            return new MotorCombDetailsResponse(in);
        }

        @Override
        public MotorCombDetailsResponse[] newArray(int size) {
            return new MotorCombDetailsResponse[size];
        }
    };
}
