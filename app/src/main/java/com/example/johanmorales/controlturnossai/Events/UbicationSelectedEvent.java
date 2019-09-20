package com.example.johanmorales.controlturnossai.Events;

import com.example.johanmorales.controlturnossai.Models.Ubication;

public class UbicationSelectedEvent {

    public final Ubication ubication;

    public UbicationSelectedEvent(Ubication ubication) {
        this.ubication = ubication;
    }

    public Ubication getUbication(){
        return this.ubication;
    }
}
