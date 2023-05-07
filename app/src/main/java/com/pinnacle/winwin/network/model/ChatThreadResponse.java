package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatThreadResponse implements Parcelable {

    @SerializedName("statusCode")
    @Expose
    private int statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ChatDataResponse chatDataResponse;

    public ChatThreadResponse() {

    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatDataResponse getChatDataResponse() {
        return chatDataResponse;
    }

    public void setChatDataResponse(ChatDataResponse chatDataResponse) {
        this.chatDataResponse = chatDataResponse;
    }

    protected ChatThreadResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        chatDataResponse = (ChatDataResponse) in.readValue(ChatDataResponse.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(statusCode);
        dest.writeString(statusMessage);
        dest.writeString(message);
        dest.writeValue(chatDataResponse);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChatThreadResponse> CREATOR = new Parcelable.Creator<ChatThreadResponse>() {
        @Override
        public ChatThreadResponse createFromParcel(Parcel in) {
            return new ChatThreadResponse(in);
        }

        @Override
        public ChatThreadResponse[] newArray(int size) {
            return new ChatThreadResponse[size];
        }
    };
}
