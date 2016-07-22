package com.appanalyzer;

import android.graphics.drawable.Drawable;


public final class App {

    private String name;
    private Drawable icon;
    private String apkPath;
    private long apkSize;
    private String packName;
    private String md5Info;


    public App(String name, Drawable icon, String apkPath, long apkSize, String packName, String md5Info) {
        this.name = name;
        this.icon = icon;
        this.apkPath = apkPath;
        this.apkSize = apkSize;
        this.packName = packName;
        this.md5Info = md5Info;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getApkPath() {
        return apkPath;
    }

    public long getApkSize() {
        return apkSize;
    }

    public String getPackName(){

        return packName;
    }
    public String getMd5Info(){

        return md5Info;
    }
}
