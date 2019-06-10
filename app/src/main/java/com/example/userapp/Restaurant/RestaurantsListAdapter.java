package com.example.userapp.Restaurant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.userapp.Favorites.FavoritesModel;
import com.example.userapp.R;
import com.example.userapp.RestaurantMenu.RestaurantMenuActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RestaurantsListAdapter.RestaurantsViewHolder>
    implements Filterable {
    private static final String TAG = "RestaurantsListAdapter";
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<RestaurantModel> dataSet;
    private ArrayList<RestaurantModel> dataSetActiveFilters;
    private ArrayList<RestaurantModel> dataSetFiltered;
    private boolean activeFilters;

    private ArrayList<FavoritesModel> favorites;
    public RestaurantsListAdapter(Context context, ArrayList<RestaurantModel> restaurants, ArrayList<FavoritesModel> favorites){
        this.favorites = favorites;
        this.context = context;
        this.dataSet = restaurants;
        this.dataSetFiltered = restaurants;
        this.mInflater = LayoutInflater.from(context);
        this.activeFilters = false;
    }
    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_restaurant,viewGroup,false);
        RestaurantsViewHolder holder = new RestaurantsViewHolder(view);
        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, RestaurantMenuActivity.class);
            int position = holder.getAdapterPosition();
            RestaurantModel rm = dataSetFiltered.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("rest", rm);
            intent.putExtras(bundle);
            context.startActivity(intent);
         });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder restaurantsViewHolder, int position) {

        ImageView imageViewLogo = restaurantsViewHolder.imageViewLogo;
        TextView textViewName = restaurantsViewHolder.textViewName;
        TextView textViewDistance = restaurantsViewHolder.textViewDistance;
        TextView textViewDeliveryFee = restaurantsViewHolder.textViewDeliveryFee;

        RestaurantModel restaurantModel = dataSetFiltered.get(position);

        if (favorites.size() > 0) {
            for (FavoritesModel favoritesModel : favorites) {
                if (favoritesModel.getRestaurantID().equals(restaurantModel.getId())) {
                    System.out.println("**********find this restaurant"+restaurantModel.getId());
                    restaurantModel.setLiked(true);
                }
            }
        }

        Log.e(TAG,"restaurantModel.getRestaurantLogo()"+restaurantModel.getRestaurantLogo());
        Uri tmpUri = Uri.parse(restaurantModel.getRestaurantLogo());
        Glide.with(context).load(tmpUri).placeholder(R.drawable.img_rest_1).into(imageViewLogo);
        textViewName.setText(restaurantModel.getName());

        DecimalFormat format = new DecimalFormat("0.00");
        String formattedPrice = format.format(restaurantModel.getDeliveryFee());
        String formattedDistance = format.format(restaurantModel.getDistance());
        textViewDeliveryFee.setText("€ " +formattedPrice);
        textViewDistance.setText(formattedDistance + "KM");

    }

    @Override
    public int getItemCount() {
        return dataSetFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    if(!activeFilters)
                        dataSetFiltered = dataSet;
                    else
                        dataSetFiltered = dataSetActiveFilters;
                } else {
                    ArrayList<RestaurantModel> filteredList = new ArrayList<>();
                    if(!activeFilters) {
                        for (RestaurantModel row : dataSet) {
                            if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                    } else {
                        for (RestaurantModel row : dataSetActiveFilters) {
                            if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                    }
                    dataSetFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataSetFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataSetFiltered = (ArrayList<RestaurantModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setFilters(ArrayList<String> filters) {
        dataSetActiveFilters = new ArrayList<>();
        for (RestaurantModel row : dataSet) {
            ArrayList<String> rowTags = row.getTags();
            if(rowTags != null) {
                for (String filterString : filters)
                    if (rowTags.contains(filterString)) {
                        dataSetActiveFilters.add(row);
                        break;
                    }
            }
        }
        dataSetFiltered = dataSetActiveFilters;
        activeFilters = true;
    }

    public void removeFilters() {
        dataSetFiltered = dataSet;
        activeFilters = false;
    }

    static class RestaurantsViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewLogo;
        TextView textViewName;
        TextView textViewDistance;
        TextView textViewDeliveryFee;

        RestaurantsViewHolder(View itemView) {
            super(itemView);
            this.imageViewLogo = itemView.findViewById(R.id.tvRestaurantLogo);
            this.textViewName = itemView.findViewById(R.id.tvRestaurantName);
            this.textViewDistance = itemView.findViewById(R.id.tvDistanceRestaurant);
            this.textViewDeliveryFee = itemView.findViewById(R.id.tvDeliveryFeeRestaurant);
        }
    }
}
