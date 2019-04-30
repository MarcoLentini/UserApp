package com.example.userapp.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.userapp.R;

import java.util.ArrayList;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RestaurantsListAdapter.RestaurantsViewHolder>  {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Restaurant> dataSet;

    public RestaurantsListAdapter(Context context, ArrayList<Restaurant> restaurants){
        this.context = context;
        this.dataSet = restaurants;
        this.mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_restaurant,viewGroup,false);
        return new RestaurantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder restaurantsViewHolder, int position) {

        TextView textViewRestaurantName = restaurantsViewHolder.textViewRestaurantName;

        Restaurant tmpRM = dataSet.get(position);
        textViewRestaurantName.setText("" + tmpRM.getRestaurant());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class RestaurantsViewHolder extends RecyclerView.ViewHolder {

        TextView textViewRestaurantName;

        RestaurantsViewHolder(View itemView) {
            super(itemView);
            this.textViewRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
        }
    }
}
