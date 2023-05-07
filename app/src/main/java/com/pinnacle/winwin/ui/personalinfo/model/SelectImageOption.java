package com.pinnacle.winwin.ui.personalinfo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SelectImageOption implements Parcelable {

    private int selectImageOption;
    private String selectImageTitle;
    private boolean isSelected;

    public SelectImageOption() {

    }

    public int getSelectImageOption() {
        return selectImageOption;
    }

    public void setSelectImageOption(int selectImageOption) {
        this.selectImageOption = selectImageOption;
    }

    public String getSelectImageTitle() {
        return selectImageTitle;
    }

    public void setSelectImageTitle(String selectImageTitle) {
        this.selectImageTitle = selectImageTitle;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    protected SelectImageOption(Parcel in) {
        selectImageOption = in.readInt();
        selectImageTitle = in.readString();
        isSelected = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(selectImageOption);
        dest.writeString(selectImageTitle);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SelectImageOption> CREATOR = new Parcelable.Creator<SelectImageOption>() {
        @Override
        public SelectImageOption createFromParcel(Parcel in) {
            return new SelectImageOption(in);
        }

        @Override
        public SelectImageOption[] newArray(int size) {
            return new SelectImageOption[size];
        }
    };
}
