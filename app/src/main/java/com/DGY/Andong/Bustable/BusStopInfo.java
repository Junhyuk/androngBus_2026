package com.DGY.Andong.Bustable;

public class BusStopInfo {


    String routeNum;
    String RouteID;
    String StartEndName;
    String predictTm;
    String postPlateNo;
    String remainStation;

    String GpsX;
    String GpsY;


    public   BusStopInfo(String RouteID, String routeNum, String StartEndName, String predictTm, String postPlateNo, String remainStation) {

        this.RouteID = RouteID;
        this.routeNum = routeNum;
        this.StartEndName = StartEndName;
        this.predictTm = predictTm;
        this.postPlateNo = postPlateNo;
        this.remainStation = remainStation;

    }

    public   BusStopInfo(String RouteID, String routeNum, String StartEndName, String predictTm, String postPlateNo, String remainStation, String GpsX, String GpsY) {

        this.RouteID = RouteID;
        this.routeNum = routeNum;
        this.StartEndName = StartEndName;
        this.predictTm = predictTm;
        this.postPlateNo = postPlateNo;
        this.remainStation = remainStation;

        this.GpsX = GpsX;
        this.GpsY = GpsY;

    }



    public  BusStopInfo() {

    }

    public String  getrouteNum() { return routeNum;    }
    public void setrouteNum(String routeNum) {  this.routeNum = routeNum; }

    public String  getRouteID() {return RouteID;}
    public void setRouteID(String RouteID) { this.RouteID = RouteID; }

    public String  getStartEndName() { return StartEndName; }
    public void setStartEndName (String gpsX) { this.StartEndName = StartEndName;    }

    //=======================================================================//

    public String  getpredictTm() { return predictTm;    }
    public void setpredictTm(String predictTm) { this.predictTm = predictTm;}

    public String  getpostPlateNo() {        return postPlateNo;    }
    public void setpostPlateNo(String postPlateNo) { this.postPlateNo = postPlateNo;}

    public String  getremainStation() { return remainStation;    }
    public void setremainStation(String remainStation) { this.remainStation = remainStation; }


    public String  getGpsX() { return GpsX;    }
    public void setGpsX(String gpsX) { this.GpsX = gpsX;}


    public String  getGpsY() { return GpsX;    }
    public void setGpsY(String gpsY) { this.GpsY = gpsY;}



}
