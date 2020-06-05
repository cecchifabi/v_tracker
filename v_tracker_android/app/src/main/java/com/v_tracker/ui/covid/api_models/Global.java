package com.v_tracker.ui.covid.api_models;

public class Global {
    private int NewConfirmed;
    private int TotalConfirmed;
    private int NewDeaths;
    private int TotalDeaths;
    private int NewRecovered;
    private int TotalRecovered;

    public Global() {
        super();
    }

    public Global(int newConfirmed, int totalConfirmed, int newDeaths, int totalDeaths, int newRecovered, int totalRecovered) {
        NewConfirmed = newConfirmed;
        TotalConfirmed = totalConfirmed;
        NewDeaths = newDeaths;
        TotalDeaths = totalDeaths;
        NewRecovered = newRecovered;
        TotalRecovered = totalRecovered;
    }

    public int getNewConfirmed() {
        return NewConfirmed;
    }

    public void setNewConfirmed(int newConfirmed) {
        NewConfirmed = newConfirmed;
    }

    public int getTotalConfirmed() {
        return TotalConfirmed;
    }

    public void setTotalConfirmed(int totalConfirmed) {
        TotalConfirmed = totalConfirmed;
    }

    public int getNewDeaths() {
        return NewDeaths;
    }

    public void setNewDeaths(int newDeaths) {
        NewDeaths = newDeaths;
    }

    public int getTotalDeaths() {
        return TotalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        TotalDeaths = totalDeaths;
    }

    public int getNewRecovered() {
        return NewRecovered;
    }

    public void setNewRecovered(int newRecovered) {
        NewRecovered = newRecovered;
    }

    public int getTotalRecovered() {
        return TotalRecovered;
    }

    public void setTotalRecovered(int totalRecovered) {
        TotalRecovered = totalRecovered;
    }
}
