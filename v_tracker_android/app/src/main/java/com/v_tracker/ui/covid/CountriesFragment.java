package com.v_tracker.ui.covid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.v_tracker.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CountriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CountriesFragment extends Fragment {

    public ArrayList<String> countryNames;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    public CountriesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CountriesFragment newInstance(ArrayList<String> cn) {
        CountriesFragment fragment = new CountriesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.countryNames = cn;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_countries, container, false);
       /* RecyclerView recyclerView = findViewById(R.id.recycler_countries);
        mAdapter = new CountryAdapter(getContext(), country_names);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
*/
        return inflate;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_countries);
        mAdapter = new CountryAdapter(this.getContext(), countryNames);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
