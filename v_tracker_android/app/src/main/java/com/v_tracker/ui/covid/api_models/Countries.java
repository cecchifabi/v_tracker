package com.v_tracker.ui.covid.api_models;

import java.util.ArrayList;

public class Countries {
        public ArrayList<Country> countries = new ArrayList<Country>();

        public void addCountry(Country c){
            countries.add( c);
        }
        public int size(){
            return countries.size();
        }





}
