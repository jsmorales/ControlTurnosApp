package com.example.johanmorales.controlturnossai.utils;

public class ContainsString {

    public static boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }
}
