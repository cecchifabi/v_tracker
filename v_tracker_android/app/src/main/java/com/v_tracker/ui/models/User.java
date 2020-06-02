package com.v_tracker.ui.models;

import java.util.List;

public class User {
    String uid;
    boolean isInfected;
    List<Position> listOfPositions;

    public User() {}
    public User(boolean isInf, List<Position> list, String id){
        isInfected = isInf;
        listOfPositions = list;
        uid = id;
    }

    public void setInfected(boolean infected) {
        isInfected = infected;
    }

    public String getUid() {
        return uid;
    }

    public void addNewPosition(Position position){
        listOfPositions.add(position);
    }

    public List<Position> getListOfPositions() {
        return listOfPositions;
    }

    public boolean getIsInfected() {
        return isInfected;
    }

}

