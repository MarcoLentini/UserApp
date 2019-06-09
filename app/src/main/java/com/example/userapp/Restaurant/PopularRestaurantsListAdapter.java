package com.example.userapp.Restaurant;

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
import com.example.userapp.Favorites.FavoritesModel;
import com.example.userapp.R;
import com.example.userapp.RestaurantMenu.RestaurantMenuActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PopularRestaurantsListAdapter extends RecyclerView.Adapter<PopularRestaurantsListAdapter.PopularRestaurantsViewHolder>{
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<RestaurantModel> dataSet;
    private ArrayList<FavoritesModel> favorites;

    public PopularRestaurantsListAdapter(Context context, ArrayList<RestaurantModel> restaurants, ArrayList<FavoritesModel> favorites) {
        this.favorites = favorites;
        this.context = context;
        this.dataSet = restaurants;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PopularRestaurantsListAdapter.PopularRestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.cardview_restaurant_popular, viewGroup,false);
        PopularRestaurantsViewHolder holder = new PopularRestaurantsViewHolder(view);
        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, RestaurantMenuActivity.class);
            int position = holder.getAdapterPosition();
            RestaurantModel rm = dataSet.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("rest", rm);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PopularRestaurantsListAdapter.PopularRestaurantsViewHolder popularRestaurantsViewHolder, int position) {
        ImageView imageViewLogo = popularRestaurantsViewHolder.imageViewLogo;
        TextView textViewName = popularRestaurantsViewHolder.textViewName;
        TextView textViewDistance = popularRestaurantsViewHolder.textViewDistance;
        TextView textViewDeliveryFee = popularRestaurantsViewHolder.textViewDeliveryFee;
        TextView textViewAverageVote = popularRestaurantsViewHolder.textViewAverageVote;

        RestaurantModel restaurantModel = dataSet.get(position);

        if (favorites.size() > 0) {
            for (FavoritesModel favoritesModel : favorites) {
                if (favoritesModel.getRestaurantID().equals(restaurantModel.getId())) {
                    restaurantModel.setLiked(true);
                }
            }
        }

        Uri tmpUri = Uri.parse(restaurantModel.getRestaurantLogo());
        Glide.with(context).load(tmpUri).placeholder(R.drawable.img_rest_1).into(imageViewLogo);
        textViewName.setText(restaurantModel.getName());

        textViewDistance.setText(restaurantModel.getAddress()); // TODO - fix me

        DecimalFormat format = new DecimalFormat("0.00");
        String formattedPrice = format.format(restaurantModel.getDeliveryFee());
        // String formattedDistance = format.format(restaurantModel.getDistance());
        textViewDeliveryFee.setText("€ " +formattedPrice);
        // textViewDistance.setText(formattedDistance + "KM");

        textViewAverageVote.setText(String.valueOf(restaurantModel.getRating()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class PopularRestaurantsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewLogo;
        TextView textViewName;
        TextView textViewDistance;
        TextView textViewDeliveryFee;
        TextView textViewAverageVote;
        public PopularRestaurantsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewLogo = itemView.findViewById(R.id.tvRestaurantLogoPopular);
            this.textViewName = itemView.findViewById(R.id.tvRestaurantNamePopular);
            this.textViewDistance = itemView.findViewById(R.id.tvDistanceRestaurantPopular);
            this.textViewDeliveryFee = itemView.findViewById(R.id.tvDeliveryFeeRestaurantPopular);
            this.textViewAverageVote = itemView.findViewById(R.id.tvAverageVoteRestaurantPopular);
        }
    }
}
