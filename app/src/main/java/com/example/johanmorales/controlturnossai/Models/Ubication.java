package com.example.johanmorales.controlturnossai.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ubication implements Parcelable {

    public String id;
    public String position;
    public String airport;

    protected Ubication(Parcel in) {
        id = in.readString();
        position = in.readString();
        airport = in.readString();
    }

    public Ubication(){

    }

    public static final Creator<Ubication> CREATOR = new Creator<Ubication>() {
        @Override
        public Ubication createFromParcel(Parcel in) {
            return new Ubication(in);
        }

        @Override
        public Ubication[] newArray(int size) {
            return new Ubication[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(position);
        dest.writeString(airport);
    }
}
