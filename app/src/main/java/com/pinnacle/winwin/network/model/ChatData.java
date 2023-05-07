package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatData implements Parcelable {

    @SerializedName("chat_id")
    @Expose
    private int chatId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("admin_id")
    @Expose
    private int adminId;
    @SerializedName("sent_by")
    @Expose
    private String sentBy;
    @SerializedName("sent_at")
    @Expose
    private String sentAt;
    @SerializedName("is_read")
    @Expose
    private boolean isRead;

    public ChatData() {

    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    protected ChatData(Parcel in) {
        chatId = in.readInt();
        message = in.readString();
        custId = in.readInt();
        adminId = in.readInt();
        sentBy = in.readString();
        sentAt = in.readString();
        isRead = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(chatId);
        dest.writeString(message);
        dest.writeInt(custId);
        dest.writeInt(adminId);
        dest.writeString(sentBy);
        dest.writeString(sentAt);
        dest.writeByte((byte) (isRead ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChatData> CREATOR = new Parcelable.Creator<ChatData>() {
        @Override
        public ChatData createFromParcel(Parcel in) {
            return new ChatData(in);
        }

        @Override
        public ChatData[] newArray(int size) {
            return new ChatData[size];
        }
    };
}
