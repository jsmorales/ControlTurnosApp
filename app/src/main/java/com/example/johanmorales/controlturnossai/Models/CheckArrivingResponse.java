package com.example.johanmorales.controlturnossai.Models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class CheckArrivingResponse {

    @SerializedName("success")
    public boolean success;
    @SerializedName("message")
    public String message;
    @SerializedName("result")
    public ResultCheckArriving result;

    public CheckArrivingResponse(boolean success, String message, ResultCheckArriving result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultCheckArriving getResult() {
        return result;
    }

    public void setResult(ResultCheckArriving result) {
        this.result = result;
    }
}
