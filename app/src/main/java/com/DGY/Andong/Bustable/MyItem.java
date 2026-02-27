package com.DGY.Andong.Bustable;

import android.graphics.drawable.Drawable;

public class MyItem {


    private String BusNum;
    private String BusStation;
    private String RouteIdBusTime;
    private String RouteId;
    private Drawable icon;
    private String FirstBusTime;  // 첫차 시간
    private String LastBusTime;   // 막차 시간

    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getBusNum() {
        return BusNum;
    }

    public void setBusNum(String BusNum) {
        this.BusNum = BusNum;
    }

    public String getBusStation() {

        return BusStation;
    }

    public void setBusStation(String BusStation) {
        this.BusStation = BusStation;
    }

    public String getBusTime() {
        return RouteIdBusTime;
    }
    public void setBusTime(String RouteIdBusTime) {
        this.RouteIdBusTime = RouteIdBusTime;
    }

    public String getRouteId() {
        return RouteId;
    }
    public void setRouteId(String RouteId) {
        this.RouteId = RouteId;
    }

    public String getFirstBusTime() {
        return FirstBusTime;
    }

    public void setFirstBusTime(String FirstBusTime) {
        this.FirstBusTime = FirstBusTime;
    }

    public String getLastBusTime() {
        return LastBusTime;
    }

    public void setLastBusTime(String LastBusTime) {
        this.LastBusTime = LastBusTime;
    }
}
