package com.DGY.Andong.Bustable;

import android.os.Parcel;
import android.os.Parcelable;



class BusInfo implements Comparable<BusInfo> , Parcelable {

    String busNum;
    String startName;
    String endName;
    String startTime;
    String endTime;
    String busIdx;
    String routeID;

    int  numofworking;

    public BusInfo(String busNum, String startName, String endName, String startTime, String endTime, String busIdx, String routeID, int numofworking) {

        this.busNum = busNum;
        this.startName = startName;
        this.endName = endName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.busIdx = busIdx;
        this.routeID = routeID;

        this.numofworking = numofworking;
    }

    public String getBusNum() {
        return this.busNum;
    }

    public String getStartName() {
        return this.startName;
    }

    public String getEndName() {
        return this.endName;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public String getRouteID() {
        return this.routeID;
    }

    public int getNumofworking() { return this.numofworking; }

    @Override
    public int compareTo(BusInfo s) {

        if (this.busNum.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
            return -1;
        } else if (s.busNum.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
            return 1;
        } else {

        }

        if (this.busIdx.length() == s.busIdx.length()) {
            return this.busNum.compareTo(s.busNum);
        } else {
            return Integer.compare(this.busIdx.length(), s.busIdx.length());
        }
    }


    public BusInfo(Parcel src) {

        busNum = src.readString();
        startName = src.readString();
        endName = src.readString();
        startTime = src.readString();
        endTime = src.readString();
        busIdx = src.readString();
        routeID = src.readString();
    }

    public static final Parcelable.Creator <BusInfo> CREATOR = new Parcelable.Creator() {
        public BusInfo createFromParcel(Parcel src){
            return new BusInfo(src);
        }

        public BusInfo[] newArray(int size){
            return new BusInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(busNum);
        dest.writeString(startName);
        dest.writeString(endName);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(busIdx);
        dest.writeString(routeID);
    }

}

