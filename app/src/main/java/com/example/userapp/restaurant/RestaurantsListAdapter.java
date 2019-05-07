package com.example.userapp.restaurant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.userapp.R;
import com.example.userapp.restaurantMenu.RestaurantMenuActivity;

import java.util.ArrayList;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RestaurantsListAdapter.RestaurantsViewHolder>
    implements Filterable {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<RestaurantModel> dataSet;
    private ArrayList<RestaurantModel> dataSetFiltered;

    public RestaurantsListAdapter(Context context, ArrayList<RestaurantModel> restaurants){
        this.context = context;
        this.dataSet = restaurants;
        this.dataSetFiltered = restaurants;
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
                Intent intent = new Intent(context, RestaurantMenuActivity.class);
                int position = holder.getAdapterPosition();
                RestaurantModel rm = dataSetFiltered.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("rest", rm);
                intent.putExtras(bundle);
                context.startActivity(intent);
             }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder restaurantsViewHolder, int position) {

        ImageView imageViewLogo = restaurantsViewHolder.imageViewLogo;
        TextView textViewName = restaurantsViewHolder.textViewName;
        TextView textViewDistance = restaurantsViewHolder.textViewDistance;
        TextView textViewDescription = restaurantsViewHolder.textViewDeliveryFee;

        RestaurantModel restaurantModel = dataSetFiltered.get(position);

        Uri tmpUri = Uri.parse(restaurantModel.getRestaurantLogo());
        Glide.with(context).load(tmpUri).placeholder(R.drawable.img_rest_1).into(imageViewLogo);
        textViewName.setText(restaurantModel.getName());
        textViewDistance.setText(restaurantModel.getAddress());
        textViewDescription.setText(String.valueOf(restaurantModel.getDeliveryFee()));

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
                    dataSetFiltered = dataSet;
                } else {
                    ArrayList<RestaurantModel> filteredList = new ArrayList<>();
                    for (RestaurantModel row : dataSet) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
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
