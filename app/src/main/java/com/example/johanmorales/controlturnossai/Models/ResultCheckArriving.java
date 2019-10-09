package com.example.johanmorales.controlturnossai.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResultCheckArriving {

    @SerializedName("socialNumber")
    public String socialNumber;
    @SerializedName("name")
    public String name;
    @SerializedName("agentPosition")
    public String agentPosition;
    @SerializedName("validation")
    public boolean validation;
    @SerializedName("detail")
    public String detail;
    @SerializedName("filterUbication")
    public String filterUbication;
    @SerializedName("turn")
    public String turn;
    @SerializedName("ubication")
    public String ubication;
    @SerializedName("notifications")
    public ArrayList<NotificationResultCheckArriving> notifications;

    public ResultCheckArriving(String socialNumber, String name, String agentPosition, boolean validation, String detail, String filterUbication, String turn, String ubication, ArrayList<NotificationResultCheckArriving> notifications) {
        this.socialNumber = socialNumber;
        this.name = name;
        this.agentPosition = agentPosition;
        this.validation = validation;
        this.detail = detail;
        this.filterUbication = filterUbication;
        this.turn = turn;
        this.ubication = ubication;
        this.notifications = notifications;
    }

    public String getSocialNumber() {
        return socialNumber;
    }

    public void setSocialNumber(String socialNumber) {
        this.socialNumber = socialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgentPosition() {
        return agentPosition;
    }

    public void setAgentPosition(String agentPosition) {
        this.agentPosition = agentPosition;
    }

    public boolean isValidation() {
        return validation;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFilterUbication() {
        return filterUbication;
    }

    public void setFilterUbication(String filterUbication) {
        this.filterUbication = filterUbication;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getUbication() {
        return ubication;
    }

    public void setUbication(String ubication) {
        this.ubication = ubication;
    }

    public ArrayList<NotificationResultCheckArriving> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<NotificationResultCheckArriving> notifications) {
        this.notifications = notifications;
    }
}
