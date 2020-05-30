package com.v_tracker.ui.covid;

import java.util.ArrayList;

public class Post {

    private Global Global;
    private ArrayList<Country> Countries;
    private String Date;

    public  com.v_tracker.ui.covid.Global getGlobal() {
        return Global;
    }

    public void setGlobal(com.v_tracker.ui.covid.Global global) {
        Global = global;
    }

    public ArrayList<Country> getCountries() {
        return Countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        Countries = countries;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Post(com.v_tracker.ui.covid.Global global, ArrayList<Country> countries, String date) {
        Global = global;
        Countries = countries;
        Date = date;
    }
}
