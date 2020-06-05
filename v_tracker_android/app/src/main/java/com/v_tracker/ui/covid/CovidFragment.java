package com.v_tracker.ui.covid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.v_tracker.MainActivity;
import com.v_tracker.R;
import com.v_tracker.ui.covid.api_models.Country;
import com.v_tracker.ui.covid.api_models.JsonPlaceHolderAPI;
import com.v_tracker.ui.covid.api_models.Post;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CovidFragment extends Fragment {

    public static Post post;
    ArrayList<String> country_names = new ArrayList<String>();
    TextView covid, totalCases, totalRecovered, totalDeaths, newCases, newRecovered, newDeaths;
    Button viewCountries;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i(MainActivity.V_TRACKER_INFO, "I'm in COVID");
        View root = inflater.inflate(R.layout.fragment_covid, container, false);
        totalCases= root.findViewById(R.id.totalCases);
        totalDeaths=root.findViewById(R.id.deaths);
        totalRecovered=root.findViewById(R.id.recovered);

        newCases=root.findViewById(R.id.newCases);
        newDeaths=root.findViewById(R.id.newDeaths);
        newRecovered=root.findViewById(R.id.newRecovered);

        viewCountries = root.findViewById(R.id.buttonViewByCountry);
        viewCountries.setVisibility(View.VISIBLE);

        covid = root.findViewById(R.id.covid);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.covid19api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);

        Call<Post> call  = jsonPlaceHolderAPI.getPosts();

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    covid.setText(getResources().getString(R.string.API_not_respond));
                }
                else{
                    ArrayList<Country> countries= response.body().getCountries();
                    post = response.body();

                    for(Country c: countries){
                       country_names.add(c.getCountry());
                    }
                    newCases.setText(""+post.getGlobal().getNewConfirmed());
                    newDeaths.setText(""+post.getGlobal().getNewDeaths());
                    newRecovered.setText(""+post.getGlobal().getNewRecovered());

                    totalCases.setText(""+post.getGlobal().getTotalConfirmed());
                    totalDeaths.setText(""+post.getGlobal().getTotalDeaths());
                    totalRecovered.setText(""+post.getGlobal().getTotalRecovered());



                    viewCountries.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            CountriesFragment simpleFragment = CountriesFragment.newInstance(country_names);
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            viewCountries.setVisibility(View.INVISIBLE);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_covid, simpleFragment).addToBackStack(null).commit();


                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                covid.setText(getResources().getString(R.string.API_failure));
            }
        });

        /* View v = inflater.inflate(R.layout.fragment_countries, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recycler_countries);
        mAdapter = new CountryAdapter(this.getContext(), country_names);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));*/
        return root;
    }


}
