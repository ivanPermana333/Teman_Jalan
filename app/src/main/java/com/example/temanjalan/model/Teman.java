package com.example.temanjalan.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Teman implements Parcelable {
    private String id;
    private String name;
    private String address;
    private String price;
    private String photo;
    private String location;
    private String open;
    private String close;
    private String username;
    private String umur;

    public Teman(){

    }

    protected Teman(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        price = in.readString();
        photo = in.readString();
        location = in.readString();
        open = in.readString();
        close = in.readString();
        username = in.readString();
        umur = in.readString();
    }

    public static final Creator<Teman> CREATOR = new Creator<Teman>() {
        @Override
        public Teman createFromParcel(Parcel in) {
            return new Teman(in);
        }

        @Override
        public Teman[] newArray(int size) {
            return new Teman[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(price);
        dest.writeString(photo);
        dest.writeString(location);
        dest.writeString(open);
        dest.writeString(close);
        dest.writeString(username);
        dest.writeString(umur);
    }

    @NonNull
    @Override
    public String toString() {
        return username;
    }
}
