package com.example.johanmorales.controlturnossai.Models;

import com.google.gson.annotations.SerializedName;

public class NotificationResultCheckArriving {

    @SerializedName("message")
    public String message;

    public NotificationResultCheckArriving(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
