package com.example.userapp.restaurant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.userapp.R;

import java.util.ArrayList;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RestaurantsListAdapter.RestaurantsViewHolder>  {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<RestaurantModel> dataSet;

    public RestaurantsListAdapter(Context context, ArrayList<RestaurantModel> restaurants){
        this.context = context;
        this.dataSet = restaurants;
        this.mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_restaurant,viewGroup,false);
        RestaurantsViewHolder holder = new RestaurantsViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to do show new activity related to this page get Restaurant name get detail infomation
                Intent intent = new Intent(context, RestaurantMenuActivity.class);
                context.startActivity(intent);
             }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder restaurantsViewHolder, int position) {

        ImageView imageViewLogo = restaurantsViewHolder.imageViewLogo;
        TextView textViewName = restaurantsViewHolder.textViewName;
        TextView textViewAddress = restaurantsViewHolder.textViewAddress;
        TextView textViewDescription = restaurantsViewHolder.textViewDescription;

        RestaurantModel restaurantModel = dataSet.get(position);

        Uri tmpUri = Uri.parse(restaurantModel.getRestaurantLogo());
        Glide.with(context).load(tmpUri).placeholder(R.drawable.img_rest_1).into(imageViewLogo);
        textViewName.setText(restaurantModel.getName());
        textViewAddress.setText(restaurantModel.getAddress());
        textViewDescription.setText(restaurantModel.getDescription());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class RestaurantsViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewAddress;
        TextView textViewDescription;
        ImageView imageViewLogo;

        RestaurantsViewHolder(View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.tvRestaurantName);
            this.textViewAddress = itemView.findViewById(R.id.tvAddress);
            this.textViewDescription = itemView.findViewById(R.id.tvDescription);
            this.imageViewLogo = itemView.findViewById(R.id.ivRestaurantLogo);
        }
    }
}
