package com.DGY.Andong.Bustable;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class MyItem {


    private String BusNum;
    private String BusStation;
    private String RouteIdBusTime;
    private String RouteId;;
    private Drawable icon;

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
}
