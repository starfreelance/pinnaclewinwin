package com.pinnacle.winwin.network.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "game_details")
public class GameDetailsResponse implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "game_id")
    @SerializedName("game_id")
    @Expose
    private int gameId;

    @ColumnInfo(name = "game_name")
    @SerializedName("game_name")
    @Expose
    private String gameName;

    @ColumnInfo(name = "status")
    @SerializedName("status")
    @Expose
    private boolean status;

    /*@TypeConverters(DataConverter.class)*/
    @Ignore
    @SerializedName("amount_detail")
    @Expose
    private List<AmountDetailsResponse> amountDetail = null;

    private int imgResource;

    @ColumnInfo(name = "imageResourceName")
    private String imageResourceName;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<AmountDetailsResponse> getAmountDetail() {
        return amountDetail;
    }

    public void setAmountDetail(List<AmountDetailsResponse> amountDetail) {
        this.amountDetail = amountDetail;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public String getImageResourceName() {
        return imageResourceName;
    }

    public void setImageResourceName(String imageResourceName) {
        this.imageResourceName = imageResourceName;
    }

    public GameDetailsResponse() {

    }

    protected GameDetailsResponse(Parcel in) {
        gameId = in.readInt();
        gameName = in.readString();
        status = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            amountDetail = new ArrayList<AmountDetailsResponse>();
            in.readList(amountDetail, AmountDetailsResponse.class.getClassLoader());
        } else {
            amountDetail = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(gameId);
        dest.writeString(gameName);
        dest.writeByte((byte) (status ? 0x01 : 0x00));
        if (amountDetail == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(amountDetail);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GameDetailsResponse> CREATOR = new Parcelable.Creator<GameDetailsResponse>() {
        @Override
        public GameDetailsResponse createFromParcel(Parcel in) {
            return new GameDetailsResponse(in);
        }

        @Override
        public GameDetailsResponse[] newArray(int size) {
            return new GameDetailsResponse[size];
        }
    };
}
