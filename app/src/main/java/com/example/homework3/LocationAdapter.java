package com.example.homework3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private List<Location> locations;

    // pass this list into the constructor of the adapter
    public LocationAdapter(List<Location> locations) {
        this.locations = locations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View locationView = inflater.inflate(R.layout.item_location, parent, false);
        ViewHolder viewHolder = new ViewHolder(locationView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // populate data into the item through holder
        Location location = locations.get(position);

        // set the view based on the data and the view names
        holder.textView_name.setText(location.getName());
        holder.textView_type.setText(location.getType());
        holder.textView_dimension.setText(location.getDimension());
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_name;
        TextView textView_type;
        TextView textView_dimension;

        // create constructor
        public ViewHolder(View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.textView_loc_name);
            textView_type = itemView.findViewById(R.id.textView_loc_type);
            textView_dimension = itemView.findViewById(R.id.textView_loc_dimension);
        }
    }
}
