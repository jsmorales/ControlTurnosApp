package com.example.johanmorales.controlturnossai.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Resultado implements Parcelable {

    private String token;
    private Employee employee;

    public Resultado(Parcel in) {
        token = in.readString();
    }

    public static final Creator<Resultado> CREATOR = new Creator<Resultado>() {
        @Override
        public Resultado createFromParcel(Parcel in) {
            return new Resultado(in);
        }

        @Override
        public Resultado[] newArray(int size) {
            return new Resultado[size];
        }
    };

    public Resultado() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        //dest.writeValue(employee);
    }
}
