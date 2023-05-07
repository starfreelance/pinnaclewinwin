package com.pinnacle.winwin.data.local.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "paana_details")
public class PaanaDetails {

    @PrimaryKey
    @ColumnInfo(name = "paana_id")
    private int paanaId;

    @ColumnInfo(name = "single_value")
    private int singleValue;

    @ColumnInfo(name = "paana_no")
    private int paanaNo;

    @ColumnInfo(name = "paana_type")
    private String paanaType;

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

    public int getPaanaNo() {
        return paanaNo;
    }

    public void setPaanaNo(int paanaNo) {
        this.paanaNo = paanaNo;
    }

    public String getPaanaType() {
        return paanaType;
    }

    public void setPaanaType(String paanaType) {
        this.paanaType = paanaType;
    }
}
