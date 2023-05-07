package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ChatDataResponse implements Parcelable {

    @SerializedName("total_data")
    @Expose
    private int totalData;
    @SerializedName("table_data")
    @Expose
    private List<ChatData> chats = null;

    public ChatDataResponse() {

    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public List<ChatData> getChats() {
        return chats;
    }

    public void setChats(List<ChatData> chats) {
        this.chats = chats;
    }

    protected ChatDataResponse(Parcel in) {
        totalData = in.readInt();
        if (in.readByte() == 0x01) {
            chats = new ArrayList<ChatData>();
            in.readList(chats, ChatData.class.getClassLoader());
        } else {
            chats = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalData);
        if (chats == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(chats);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChatDataResponse> CREATOR = new Parcelable.Creator<ChatDataResponse>() {
        @Override
        public ChatDataResponse createFromParcel(Parcel in) {
            return new ChatDataResponse(in);
        }

        @Override
        public ChatDataResponse[] newArray(int size) {
            return new ChatDataResponse[size];
        }
    };
}
