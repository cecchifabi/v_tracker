package com.v_tracker.ui.covid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.v_tracker.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CountryInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CountryInfo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public Country country;
    TextView country_name, totalCases, totalRecovered, totalDeaths, newCases, newRecovered, newDeaths;

    public static CountryInfo newInstance(Country c) {

        Bundle args = new Bundle();

        CountryInfo fragment = new CountryInfo();
        fragment.setArguments(args);

        fragment.country = c;
        return fragment;
    }
    public CountryInfo() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CountryInfo newInstance(String param1, String param2) {
        CountryInfo fragment = new CountryInfo();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_country_info, container, false);
        return inflater.inflate(R.layout.fragment_country_info, container, false);




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       /* totalCases= root.findViewById(R.id.totalCases_info);
        totalDeaths=root.findViewById(R.id.deaths_info);
        totalRecovered=root.findViewById(R.id.recovered_info);
        newCases=root.findViewById(R.id.newCases_info);
        newDeaths=root.findViewById(R.id.newDeaths_info);
        newRecovered=root.findViewById(R.id.newRecovered_info);
        country_name = root.findViewById(R.id.country_name_info);

        newCases.setText(""+country.getNewConfirmed());
        newDeaths.setText(""+country.getNewDeaths());
        newRecovered.setText(""+country.getNewRecovered());

        totalCases.setText(""+country.getTotalConfirmed());
        totalDeaths.setText(""+country.getTotalDeaths());
        totalRecovered.setText(""+country.getTotalRecovered());
        country_name.setText(country.getCountry());*/
        totalCases= view.findViewById(R.id.totalCases_info);
        totalDeaths=view.findViewById(R.id.deaths_info);
        totalRecovered=view.findViewById(R.id.recovered_info);
        newCases=view.findViewById(R.id.newCases_info);
        newDeaths=view.findViewById(R.id.newDeaths_info);
        newRecovered=view.findViewById(R.id.newRecovered_info);
        country_name = view.findViewById(R.id.country_name_info);

        newCases.setText(""+country.getNewConfirmed());
        newDeaths.setText(""+country.getNewDeaths());
        newRecovered.setText(""+country.getNewRecovered());

        totalCases.setText(""+country.getTotalConfirmed());
        totalDeaths.setText(""+country.getTotalDeaths());
        totalRecovered.setText(""+country.getTotalRecovered());
        country_name.setText(country.getCountry());




    }
}
