package com.pinnacle.winwin.data.local.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "baazaar_details")
public class BaazaarDetails {

    @PrimaryKey
    @ColumnInfo(name = "bazaar_id")
    private int bazaarId;

    @ColumnInfo(name = "bazaar_name")
    private String bazaarName;

    @ColumnInfo(name = "bazaar_timing")
    private String bazaarTiming;

    @ColumnInfo(name = "remaining_timing")
    private String remainingTiming;

    @ColumnInfo(name = "booking_date")
    private String bookingDate;

    @ColumnInfo(name = "final")
    private int finalNumber;

    @ColumnInfo(name = "is_open_for_booking")
    private boolean isOpenForBooking;

    @ColumnInfo(name = "close_before")
    private int closeBefore;

    @ColumnInfo(name = "status")
    private boolean status;

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

    public int getFinalNumber() {
        return finalNumber;
    }

    public void setFinalNumber(int finalNumber) {
        this.finalNumber = finalNumber;
    }

    public boolean isOpenForBooking() {
        return isOpenForBooking;
    }

    public void setOpenForBooking(boolean openForBooking) {
        isOpenForBooking = openForBooking;
    }

    public boolean isStatus() {
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
}
