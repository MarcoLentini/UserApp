package com.example.userapp.Favorites;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.userapp.R;
import com.example.userapp.Restaurant.RestaurantModel;
import com.example.userapp.RestaurantMenu.RestaurantMenuActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FavoriteRestaurantListAdapter extends RecyclerView.Adapter<FavoriteRestaurantListAdapter.FavoriteRestaurantsViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<FavoritesModel> favorites;


     public FavoriteRestaurantListAdapter(Context context,  ArrayList<FavoritesModel> favorites){
        this.favorites = favorites;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public FavoriteRestaurantListAdapter.FavoriteRestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_restaurant,viewGroup,false);
        FavoriteRestaurantsViewHolder holder = new  FavoriteRestaurantListAdapter.FavoriteRestaurantsViewHolder(view);
        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, RestaurantMenuActivity.class);
            int position = holder.getAdapterPosition();
            RestaurantModel rm = favorites.get(position).getRestaurantModel();
            Bundle bundle = new Bundle();
            bundle.putSerializable("rest", rm);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRestaurantsViewHolder restaurantsViewHolder, int position) {

        ImageView imageViewLogo = restaurantsViewHolder.imageViewLogo;
        TextView textViewName = restaurantsViewHolder.textViewName;
        TextView textViewDistance = restaurantsViewHolder.textViewDistance;
        TextView textViewDeliveryFee = restaurantsViewHolder.textViewDeliveryFee;

        RestaurantModel restaurantModel = favorites.get(position).getRestaurantModel();

        Uri tmpUri = Uri.parse(restaurantModel.getRestaurantLogo());
        Glide.with(context).load(tmpUri).placeholder(R.drawable.img_rest_1).into(imageViewLogo);
        textViewName.setText(restaurantModel.getName());
        DecimalFormat format = new DecimalFormat("0.00");
        String formattedPriceDelivery = format.format(restaurantModel.getDeliveryFee());
        textViewDeliveryFee.setText(formattedPriceDelivery+"€");

        String formattedDistance =format.format(restaurantModel.getDistance());
        textViewDistance.setText(formattedDistance+"Km");

    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }


    static class FavoriteRestaurantsViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewLogo;
        TextView textViewName;
        TextView textViewDistance;
        TextView textViewDeliveryFee;

        FavoriteRestaurantsViewHolder(View itemView) {
            super(itemView);
            this.imageViewLogo = itemView.findViewById(R.id.tvRestaurantLogo);
            this.textViewName = itemView.findViewById(R.id.tvRestaurantName);
            this.textViewDistance = itemView.findViewById(R.id.tvDistanceRestaurant);
            this.textViewDeliveryFee = itemView.findViewById(R.id.tvDeliveryFeeRestaurant);
        }
    }
}
