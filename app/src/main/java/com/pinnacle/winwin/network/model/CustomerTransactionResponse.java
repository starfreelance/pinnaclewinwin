package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerTransactionResponse implements Parcelable {

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
    private CustomerTransactionDataResponse customerTransactionDataResponse;

    public CustomerTransactionResponse() {

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

    public CustomerTransactionDataResponse getCustomerTransactionDataResponse() {
        return customerTransactionDataResponse;
    }

    public void setCustomerTransactionDataResponse(CustomerTransactionDataResponse customerTransactionDataResponse) {
        this.customerTransactionDataResponse = customerTransactionDataResponse;
    }

    protected CustomerTransactionResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        customerTransactionDataResponse = (CustomerTransactionDataResponse) in.readValue(CustomerTransactionDataResponse.class.getClassLoader());
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
        dest.writeValue(customerTransactionDataResponse);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CustomerTransactionResponse> CREATOR = new Parcelable.Creator<CustomerTransactionResponse>() {
        @Override
        public CustomerTransactionResponse createFromParcel(Parcel in) {
            return new CustomerTransactionResponse(in);
        }

        @Override
        public CustomerTransactionResponse[] newArray(int size) {
            return new CustomerTransactionResponse[size];
        }
    };
}
