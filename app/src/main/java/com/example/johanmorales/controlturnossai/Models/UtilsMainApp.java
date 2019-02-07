package com.example.johanmorales.controlturnossai.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class UtilsMainApp implements Parcelable {

    public String hostProduction = "https://control-llegada-backend.azurewebsites.net/";
    public String hostTest = "https://control-llegada-backend-test.azurewebsites.net/";
    public String hostAuth = "https://sai-auth.azurewebsites.net/api/authentication";
    public Integer numTouch = 6;
    public Integer touched = 0;

    public UtilsMainApp(Parcel in) {
        hostProduction = in.readString();
        hostTest = in.readString();
        hostAuth = in.readString();
        if (in.readByte() == 0) {
            numTouch = null;
        } else {
            numTouch = in.readInt();
        }
        if (in.readByte() == 0) {
            touched = null;
        } else {
            touched = in.readInt();
        }
    }

    public static final Creator<UtilsMainApp> CREATOR = new Creator<UtilsMainApp>() {
        @Override
        public UtilsMainApp createFromParcel(Parcel in) {
            return new UtilsMainApp(in);
        }

        @Override
        public UtilsMainApp[] newArray(int size) {
            return new UtilsMainApp[size];
        }
    };

    public UtilsMainApp() {

    }

    public String getHostProduction() {
        return hostProduction;
    }

    public String getHostTest() {
        return hostTest;
    }

    public String getHostAuth() {
        return hostAuth;
    }

    public Integer getTouched() {
        return touched;
    }

    public void setTouched(Integer touched) {
        this.touched = touched;
    }

    public Integer getNumTouch() {
        return numTouch;
    }

    public String getHost(){

        if(this.getTouched() >= this.getNumTouch()){
            return this.getHostTest();
        }else{
            return this.getHostProduction();
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hostProduction);
        dest.writeString(hostTest);
        dest.writeString(hostAuth);
        if (numTouch == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(numTouch);
        }
        if (touched == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(touched);
        }
    }
}
