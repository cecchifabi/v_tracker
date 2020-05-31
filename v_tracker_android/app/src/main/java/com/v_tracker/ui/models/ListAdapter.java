package com.v_tracker.ui.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.v_tracker.R;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListHolder> {

    Context c;
    ArrayList<UserList> list;

    public void setModels(ArrayList<UserList> t){
        list = t;
    }

    public ListAdapter(Context c, ArrayList<UserList> list) {
        this.c = c;
        this.list = list;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null);

        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {

        holder.Lat.setText(String.valueOf(list.get(position).getLat()));
        holder.Lon.setText(String.valueOf(list.get(position).getLon()));
        holder.TimeStamp.setText(list.get(position).getTimestamp());
        holder.Street.setText(list.get(position).getStreet());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
