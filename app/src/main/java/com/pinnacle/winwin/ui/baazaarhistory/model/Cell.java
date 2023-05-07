package com.pinnacle.winwin.ui.baazaarhistory.model;

public class Cell {

    private String mId;
    private Object mData;

    public Cell(String mId) {
        this.mId = mId;
    }

    public Cell(String mId, Object mData) {
        this.mId = mId;
        this.mData = mData;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public Object getmData() {
        return mData;
    }

    public void setmData(Object mData) {
        this.mData = mData;
    }
}
