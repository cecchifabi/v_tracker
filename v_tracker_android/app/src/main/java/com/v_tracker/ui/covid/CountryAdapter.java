package com.v_tracker.ui.covid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.v_tracker.R;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
    private static final String TAG = "CountryAdapter";
    private ArrayList<String> mCountryNames = new ArrayList<String>();
    private Context mContext;

    public CountryAdapter(Context mContext, ArrayList<String> mCountryNames) {
        super();
        this.mCountryNames = mCountryNames;
        this.mContext = mContext;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "on>BindViewHolder: called.");
        holder.countryName.setText(mCountryNames.get(position));

        holder.countryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick : clicked on: " + mCountryNames.get(position));
                // Toast.makeText(mContext, mCityNames.get(position), Toast.LENGTH_SHORT).show()
                //FragmentForecast.callWeatherForecastAndShowFragment( v, mCityNames.get(position) );

                Log.d(TAG, mCountryNames.get(position)+" : "+ CovidFragment.post.getCountries().get(position).getNewConfirmed());

                CountryInfo simpleFragment = CountryInfo.newInstance( CovidFragment.post.getCountries().get(position) );

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_covid, simpleFragment).addToBackStack(null).commit();


            }
        });
    }

    @Override
    public int getItemCount() {
        return mCountryNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView countryName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.country_name);
            parentLayout = itemView.findViewById(R.id.country_layout);
        }
    }
}
