package com.example.userapp.restaurant;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

        ImageView imageViewRestaurantLogo = restaurantsViewHolder.imageViewRestaurantLogo;
        TextView textViewRestaurantName = restaurantsViewHolder.textViewRestaurantName;
        TextView textViewMonthlySales = restaurantsViewHolder.textViewMonthlySales;
        TextView textViewDeliveryInfo = restaurantsViewHolder.textViewDeliveryInfo;

        RestaurantModel tmpRM = dataSet.get(position);

        imageViewRestaurantLogo.setImageResource(tmpRM.getRestaurantLogo());
        textViewRestaurantName.setText(tmpRM.getName());
        textViewMonthlySales.setText("Monthly sales"+tmpRM.getMonthlySales());
        textViewDeliveryInfo.setText("Delivery Only Min Spend :"+tmpRM.getMinSpend()+"Delivery fee:"+tmpRM.getDeliveryFee());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class RestaurantsViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewRestaurantLogo;
        TextView textViewRestaurantName;
        TextView textViewMonthlySales;
        TextView textViewDeliveryInfo;

        RestaurantsViewHolder(View itemView) {
            super(itemView);
            this.imageViewRestaurantLogo = itemView.findViewById(R.id.ivRestaurantLogo);
            this.textViewRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            this.textViewMonthlySales = itemView.findViewById(R.id.tvMonthlySales);
            this.textViewDeliveryInfo = itemView.findViewById(R.id.tvDeliveryInfo);
        }
    }
}
