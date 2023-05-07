package com.pinnacle.winwin.data.local.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "motor_comb_details")
public class MotorCombDetails {

    @PrimaryKey
    private int id;

    private int count;

    @ColumnInfo(name = "sp_comb")
    private int spComb;

    @ColumnInfo(name = "dp_comb")
    private int dpComb;

    @ColumnInfo(name = "tp_comb")
    private int tpComb;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
}
