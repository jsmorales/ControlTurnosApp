package com.example.johanmorales.controlturnossai.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Respuesta implements Parcelable {

    @SerializedName("success")
    public Boolean succes;
    @SerializedName("message")
    public String message;
    @SerializedName("result")
    public Resultado result;


    public Respuesta(Parcel in) {
        byte tmpSucces = in.readByte();
        succes = tmpSucces == 0 ? null : tmpSucces == 1;
        message = in.readString();
    }

    public static final Creator<Respuesta> CREATOR = new Creator<Respuesta>() {
        @Override
        public Respuesta createFromParcel(Parcel in) {
            return new Respuesta(in);
        }

        @Override
        public Respuesta[] newArray(int size) {
            return new Respuesta[size];
        }
    };

    public Respuesta() {

    }

    public Boolean getSucces() {
        return succes;
    }

    public void setSucces(Boolean succes) {
        this.succes = succes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Resultado getResult() {
        return result;
    }

    public void setResult(Resultado result) {
        this.result = result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (succes == null ? 0 : succes ? 1 : 2));
        dest.writeString(message);
        //dest.writeValue(result);
    }
}
