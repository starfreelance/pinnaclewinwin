package com.pinnacle.winwin.data.local.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cp_paana_details")
public class CPDetails {

    @PrimaryKey
    @ColumnInfo(name = "paana_id")
    private int paanaId;

    @ColumnInfo(name = "paana_no")
    private int paana_no;

    public int getPaanaId() {
        return paanaId;
    }

    public void setPaanaId(int paanaId) {
        this.paanaId = paanaId;
    }

    public int getPaana_no() {
        return paana_no;
    }

    public void setPaana_no(int paana_no) {
        this.paana_no = paana_no;
    }
}
