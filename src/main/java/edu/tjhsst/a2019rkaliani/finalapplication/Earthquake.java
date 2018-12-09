package edu.tjhsst.a2019rkaliani.finalapplication;

/**
 * Created by ratan on 1/18/2018.
 */

public class Earthquake {
    String myTitle, myPlace;
    Double myMag,myLat,myLon;
    int myTsu;
    String myTime;

    public Earthquake(String title, String place, double mag, int tsu, double lat, double lon, String time){
        myTitle=title;
        myPlace = place;
        myMag = mag;
        myLat = lat;
        myLon = lon;
        myTsu = tsu;
        myTime=time;


    }

    public Double getMyLat() {
        return myLat;
    }

    public String getMyTitle() {
        return myTitle;
    }

    public Double getMyLon() {
        return myLon;
    }

    public Double getMyMag() {
        return myMag;
    }

    public int getMyTsu() {
        return myTsu;
    }

    public String getMyPlace() {
        return myPlace;
    }

    public void setMyLat(Double myLat) {
        this.myLat = myLat;
    }

    public void setMyLon(Double myLon) {
        this.myLon = myLon;
    }

    public void setMyMag(Double myMag) {
        this.myMag = myMag;
    }

    public void setMyPlace(String myPlace) {
        this.myPlace = myPlace;
    }

    public void setMyTitle(String myTitle) {
        this.myTitle = myTitle;
    }

    public void setMyTsu(int myTsu) {
        this.myTsu = myTsu;
    }

    public String getMyTime() {
        return myTime;
    }

    public void setMyTime(String myTime) {
        this.myTime = myTime;
    }
}
