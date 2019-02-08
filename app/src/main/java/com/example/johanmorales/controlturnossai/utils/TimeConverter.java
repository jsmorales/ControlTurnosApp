package com.example.johanmorales.controlturnossai.utils;

public class TimeConverter {

    public static String getMinsConverted(Integer minutes){

        if(minutes < 0){
            return convertMinsToHrsMins(minutes);
        }else{
            return "-";
        }
    }

    public static String convertMinsToHrsMins(Integer minutes){

        minutes = Math.abs(minutes); //always positive number

        Double h = Math.floor(minutes / 60);

        Integer h2 = h.intValue();

        Integer m = minutes % 60;

        StringBuilder stringBuilder = new StringBuilder();

        if(h2 < 10){
            stringBuilder.append('0').append(h2);
        }else{
            stringBuilder.append(h2);
        }

        stringBuilder.append(':');

        if(m < 10){
            stringBuilder.append('0').append(m);
        }else{
            stringBuilder.append(m);
        }

        return stringBuilder.toString();
    }
}
