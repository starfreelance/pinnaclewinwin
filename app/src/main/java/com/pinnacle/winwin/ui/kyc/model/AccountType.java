package com.pinnacle.winwin.ui.kyc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AccountType implements Parcelable {

    private int type;
    private String title;
    private boolean isSelected;

    public AccountType() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    protected AccountType(Parcel in) {
        type = in.readInt();
        title = in.readString();
        isSelected = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(title);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AccountType> CREATOR = new Parcelable.Creator<AccountType>() {
        @Override
        public AccountType createFromParcel(Parcel in) {
            return new AccountType(in);
        }

        @Override
        public AccountType[] newArray(int size) {
            return new AccountType[size];
        }
    };
}
