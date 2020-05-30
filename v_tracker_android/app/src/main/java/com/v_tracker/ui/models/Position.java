package com.v_tracker.ui.models;

public class Position{
    String timestamp;
    double latitude;
    double longitude;

    public Position(){}
    public Position(String t, double lat, double lon){
        timestamp = t;
        latitude = lat;
        longitude = lon;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }
}