package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CustomerTransactionDataResponse implements Parcelable {

    @SerializedName("total_data")
    @Expose
    private Integer totalData;
    @SerializedName("table_data")
    @Expose
    private List<CustomerTransactionData> customerTransactions = null;

    public CustomerTransactionDataResponse() {

    }

    public Integer getTotalData() {
        return totalData;
    }

    public void setTotalData(Integer totalData) {
        this.totalData = totalData;
    }

    public List<CustomerTransactionData> getCustomerTransactions() {
        return customerTransactions;
    }

    public void setCustomerTransactions(List<CustomerTransactionData> customerTransactions) {
        this.customerTransactions = customerTransactions;
    }

    protected CustomerTransactionDataResponse(Parcel in) {
        totalData = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            customerTransactions = new ArrayList<CustomerTransactionData>();
            in.readList(customerTransactions, CustomerTransactionData.class.getClassLoader());
        } else {
            customerTransactions = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (totalData == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(totalData);
        }
        if (customerTransactions == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(customerTransactions);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CustomerTransactionDataResponse> CREATOR = new Parcelable.Creator<CustomerTransactionDataResponse>() {
        @Override
        public CustomerTransactionDataResponse createFromParcel(Parcel in) {
            return new CustomerTransactionDataResponse(in);
        }

        @Override
        public CustomerTransactionDataResponse[] newArray(int size) {
            return new CustomerTransactionDataResponse[size];
        }
    };
}
