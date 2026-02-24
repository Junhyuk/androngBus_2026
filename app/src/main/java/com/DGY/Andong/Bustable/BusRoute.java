package com.DGY.Andong.Bustable;

import java.util.ArrayList;
import java.util.List;

public class BusRoute {

    //List<String> BusLists = new ArrayList<String>();
    //List<String> BusStations = new ArrayList<String>();
    private String StationName;
    private String StationNum;

    private String CurStationName;
    private String BusplateNo;
    private Integer CurBusPos;


    public String  getStationName() {
        return StationName;
    }

    public void setStationName(String StationName) {
        this.StationName = StationName;
    }

    public String  getStationNum() {
        return StationNum;
    }

    public void setStationNum(String StationNum) {
        this.StationNum = StationNum;
    }

    //===========================================================//

    public String  getCurStationName() {
        return CurStationName;
    }

    public void setCurStationName(String CurStationName) {
        this.CurStationName = CurStationName;
    }


    public String  getPlateName() {
        return BusplateNo;
    }

    public void setPlateName(String BusplateNo) {
        this.BusplateNo = BusplateNo;
    }

    public Integer  getCurBusPos() {
        return CurBusPos;
    }

    public void setCurBusPos(Integer CurBusPos) {
        this.CurBusPos = CurBusPos;
    }



}