package com.v_tracker.ui.models;

import java.util.List;

public class User {
    boolean isInfected;
    List<Position> listOfPositions;

    public User() {}
    public User(boolean isInf, List<Position> list){
        isInfected = isInf;
        listOfPositions = list;
    }

    public void addNewPosition(Position position){
        listOfPositions.add(position);
    }

    public List<Position> getListOfPositions() {
        return listOfPositions;
    }

    public boolean isInfected() {
        return isInfected;
    }
}

