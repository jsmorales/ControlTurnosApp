package com.example.johanmorales.controlturnossai.Models;

//"extraInfo":{"questions":[],"zones":[]}

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoginExtraInfo {

    @SerializedName("questions")
    public ArrayList<String> questions;
    @SerializedName("zones")
    public ArrayList<String> zones;

    public LoginExtraInfo(ArrayList<String> questions, ArrayList<String> zones) {
        this.questions = questions;
        this.zones = zones;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

    public ArrayList<String> getZones() {
        return zones;
    }

    public void setZones(ArrayList<String> zones) {
        this.zones = zones;
    }
}
