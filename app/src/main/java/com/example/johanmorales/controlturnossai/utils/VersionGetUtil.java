package com.example.johanmorales.controlturnossai.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class VersionGetUtil extends Application {

    private Context context;
    private String versionName;
    private Integer versionCode;
    private PackageInfo pInfo;

    public VersionGetUtil(Context context){
        this.context = context;
    }

    public PackageInfo getPackageInfo(){

        try {

            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return pInfo;
    }

    public String getVersionName(){

        PackageInfo pInfo = getPackageInfo();
        versionName = pInfo.versionName;

        return versionName;
    }

    public Integer getVersionCode(){

        PackageInfo pInfo = getPackageInfo();
        versionCode = pInfo.versionCode;

        return versionCode;
    }
}
