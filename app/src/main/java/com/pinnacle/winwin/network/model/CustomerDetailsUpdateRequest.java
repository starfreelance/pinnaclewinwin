package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerDetailsUpdateRequest implements Parcelable {

    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;

    public CustomerDetailsUpdateRequest() {

    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    protected CustomerDetailsUpdateRequest(Parcel in) {
        custId = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custId);
        dest.writeString(firstName);
        dest.writeString(lastName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CustomerDetailsUpdateRequest> CREATOR = new Parcelable.Creator<CustomerDetailsUpdateRequest>() {
        @Override
        public CustomerDetailsUpdateRequest createFromParcel(Parcel in) {
            return new CustomerDetailsUpdateRequest(in);
        }

        @Override
        public CustomerDetailsUpdateRequest[] newArray(int size) {
            return new CustomerDetailsUpdateRequest[size];
        }
    };
}
