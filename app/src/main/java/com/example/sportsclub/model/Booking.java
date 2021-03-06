package com.example.sportsclub.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Booking implements Parcelable {
    private String status;
    private String date;
    private String field;
    private String time;
    private String code;
    private String idUser;

    public String getIdUser() {
        return idUser;
    }
    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    private String totalPrice;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static final Parcelable.Creator<Booking> CREATOR = new Parcelable.Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    public Booking(Parcel in) {
        status = in.readString();
        date= in.readString();
        field= in.readString();
        time= in.readString();
        code= in.readString();
        idUser= in.readString();
    }
    public Booking() {

    }

    public static Parcelable.Creator<Booking> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        parcel.writeString(date);
        parcel.writeString(field);
        parcel.writeString(time);
        parcel.writeString(code);
    }

}
