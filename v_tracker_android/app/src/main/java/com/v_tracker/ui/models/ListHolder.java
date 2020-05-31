package com.v_tracker.ui.models;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.v_tracker.R;

public class ListHolder extends RecyclerView.ViewHolder {

    TextView Lat, Lon, TimeStamp, Street;

    public ListHolder(@NonNull View itemView) {
        super(itemView);

        this.Lat = itemView.findViewById(R.id.Latitude);
        this.Lon = itemView.findViewById(R.id.Longitude);
        this.TimeStamp = itemView.findViewById(R.id.Timestamp);
        this.Street = itemView.findViewById(R.id.Street);
    }
}
