package com.pinnacle.winwin.network.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pinnacle.winwin.data.local.db.DataConverter;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "baazaar_details")
public class BazaarDetailsResponse implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "bazaar_id")
    @SerializedName("bazaar_id")
    @Expose
    private int bazaarId;

    @ColumnInfo(name = "bazaar_name")
    @SerializedName("bazaar_name")
    @Expose
    private String bazaarName;

    @ColumnInfo(name = "bazaar_timing")
    @SerializedName("timing")
    @Expose
    private String bazaarTiming;

    @ColumnInfo(name = "remaining_timing")
    @SerializedName("remaining_timing")
    @Expose
    private String remainingTiming;

    @ColumnInfo(name = "booking_date")
    @SerializedName("booking_date")
    @Expose
    private String bookingDate;

    @ColumnInfo(name = "final")
    @SerializedName("final")
    @Expose
    private String finalNumber;

    @ColumnInfo(name = "is_open_for_booking")
    @SerializedName("is_open_for_booking")
    @Expose
    private boolean isOpenForBooking;

    @ColumnInfo(name = "status")
    @SerializedName("status")
    @Expose
    private boolean status;

    @ColumnInfo(name = "close_before")
    @SerializedName("close_before")
    @Expose
    private int closeBefore;

    @ColumnInfo(name = "last_result")
    @SerializedName("last_result")
    @Expose
    private String lastResult;

    @ColumnInfo(name = "game_map")
    @TypeConverters(DataConverter.class)
    @SerializedName("game_map")
    @Expose
    private List<Integer> gameMap = null;

    private int imgResource;

    public BazaarDetailsResponse() {

    }


    public int getBazaarId() {
        return bazaarId;
    }

    public void setBazaarId(int bazaarId) {
        this.bazaarId = bazaarId;
    }

    public String getBazaarName() {
        return bazaarName;
    }

    public void setBazaarName(String bazaarName) {
        this.bazaarName = bazaarName;
    }

    public String getBazaarTiming() {
        return bazaarTiming;
    }

    public void setBazaarTiming(String bazaarTiming) {
        this.bazaarTiming = bazaarTiming;
    }

    public String getRemainingTiming() {
        return remainingTiming;
    }

    public void setRemainingTiming(String remainingTiming) {
        this.remainingTiming = remainingTiming;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getFinalNumber() {
        return finalNumber;
    }

    public void setFinalNumber(String finalNumber) {
        this.finalNumber = finalNumber;
    }

    public boolean getIsOpenForBooking() {
        return isOpenForBooking;
    }

    public void setIsOpenForBooking(boolean isOpenForBooking) {
        this.isOpenForBooking = isOpenForBooking;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCloseBefore() {
        return closeBefore;
    }

    public void setCloseBefore(int closeBefore) {
        this.closeBefore = closeBefore;
    }

    public String getLastResult() {
        return lastResult;
    }

    public void setLastResult(String lastResult) {
        this.lastResult = lastResult;
    }

    public List<Integer> getGameMap() {
        return gameMap;
    }

    public void setGameMap(List<Integer> gameMap) {
        this.gameMap = gameMap;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    protected BazaarDetailsResponse(Parcel in) {
        bazaarId = in.readInt();
        bazaarName = in.readString();
        bazaarTiming = in.readString();
        remainingTiming = in.readString();
        bookingDate = in.readString();
        finalNumber = in.readString();
        isOpenForBooking = in.readByte() != 0x00;
        status = in.readByte() != 0x00;
        closeBefore = in.readInt();
        lastResult = in.readString();
        if (in.readByte() == 0x01) {
            gameMap = new ArrayList<Integer>();
            in.readList(gameMap, Integer.class.getClassLoader());
        } else {
            gameMap = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bazaarId);
        dest.writeString(bazaarName);
        dest.writeString(bazaarTiming);
        dest.writeString(remainingTiming);
        dest.writeString(bookingDate);
        dest.writeString(finalNumber);
        dest.writeByte((byte) (isOpenForBooking ? 0x01 : 0x00));
        dest.writeByte((byte) (status ? 0x01 : 0x00));
        dest.writeInt(closeBefore);
        dest.writeString(lastResult);
        if (gameMap == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(gameMap);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BazaarDetailsResponse> CREATOR = new Parcelable.Creator<BazaarDetailsResponse>() {
        @Override
        public BazaarDetailsResponse createFromParcel(Parcel in) {
            return new BazaarDetailsResponse(in);
        }

        @Override
        public BazaarDetailsResponse[] newArray(int size) {
            return new BazaarDetailsResponse[size];
        }
    };
}
