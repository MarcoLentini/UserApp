package com.example.userapp.shoppingCart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.userapp.R;

import java.util.ArrayList;

public class OrderItemListAdapter extends  RecyclerView.Adapter<OrderItemListAdapter.OrderItemViewHolder> {
    private Context context;
    private  ArrayList<OrderItemModel> orderItems;
    private LayoutInflater mInflater;

    public OrderItemListAdapter(Context context,ArrayList<OrderItemModel> orderItems){
        this.context = context;
        this.orderItems = orderItems;
        this.mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.order_item_info,viewGroup,false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder orderItemViewHolder, int i) {
        OrderItemModel item = orderItems.get(i);

        TextView textViewItemName = orderItemViewHolder.textViewItemName;
        TextView textViewIemPrice = orderItemViewHolder.textViewIemPrice;
        TextView textViewItemCount = orderItemViewHolder.textViewItemCount;
        ImageView imageViewItemAdd = orderItemViewHolder.imageViewItemAdd;
        ImageView imageViewItemRemove = orderItemViewHolder.imageViewItemRemove;

        textViewIemPrice.setText(String.valueOf(item.getPrice()));
        textViewItemName.setText(item.getName());
        textViewItemCount.setText(item.getCount());
/*
        imageViewItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShoppingCartActivity)context).handlerShoppingCarNum(1,i,true);
            }
        });

        imageViewItemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 ((ShoppingCartActivity)context).handlerShoppingCarNum(0,i,true);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName;
        TextView textViewIemPrice;
        ImageView imageViewItemAdd;
        ImageView imageViewItemRemove;
        TextView textViewItemCount;

        public OrderItemViewHolder(View itemView) {
            super(itemView);
            this.textViewItemName = itemView.findViewById(R.id.tv_name);
            this.textViewIemPrice = itemView.findViewById(R.id.tv_price);
            this.textViewItemCount = itemView.findViewById(R.id.tv_count);
            this.imageViewItemAdd = itemView.findViewById(R.id.iv_add);
            this.imageViewItemRemove = itemView.findViewById(R.id.iv_remove);
        }
    }
}
