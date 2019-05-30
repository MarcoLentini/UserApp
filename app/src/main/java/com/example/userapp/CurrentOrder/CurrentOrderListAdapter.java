package com.example.userapp.CurrentOrder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static  final  int  CURRENT_ORDER_DETAIL_INFO_CODE =1;

    private Context context;
    private ArrayList<CurrentOrderModel>  currentOrders;
    private LayoutInflater mInflater;

    HashMap<String,CurrentOrderModel> orders;

    public CurrentOrderListAdapter(Context context, ArrayList<CurrentOrderModel> currentOrders) {
              this.context = context;
              this.currentOrders = currentOrders;
              this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.current_order_info, viewGroup,false);
        CurrentOrderViewHolder holder = new CurrentOrderViewHolder(view);
        //when click on the view you can see the detail info of current reservation

        view.setOnClickListener(v -> {
            Intent myIntent = new Intent(view.getContext(), CurrentOrderDetailInfoActivity.class);
            int itemPosition = holder.getAdapterPosition();
            Bundle bn = new Bundle();
            System.out.println("List adapter"+itemPosition);
            bn.putInt("currentOrderData", itemPosition);
            myIntent.putExtras(bn);
            context.startActivity(myIntent);
//            ((Activity)context).startActivityForResult(myIntent,CURRENT_ORDER_DETAIL_INFO_CODE);
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
        CurrentOrderViewHolder currentOrderViewHolder = (CurrentOrderViewHolder)viewHolder;

        TextView textViewRestName = currentOrderViewHolder.textViewRestName;
        TextView textViewOrderStatus = currentOrderViewHolder.textViewOrderStatus;
        TextView textViewTotalCost =currentOrderViewHolder.textViewTotalCost;
        TextView textViewTotalItems = currentOrderViewHolder.textViewTotalItems;
        Button buttonOrderAccepted = currentOrderViewHolder.buttonOrderAccepted;
        TextView textViewCurrentOrderInfo = currentOrderViewHolder.textViewCurrentOrderInfo;
        TextView textViewConfirmationCode = currentOrderViewHolder.textViewConfirmationCode;


        CurrentOrderModel currentOrder = currentOrders.get(pos);

        textViewRestName.setText(""+currentOrder.getRest_name());
        textViewOrderStatus.setText(""+currentOrder.getRs_status());
        textViewTotalCost.setText(""+currentOrder.getTotal_cost());
        textViewConfirmationCode.setText(""+currentOrder.getConfirmation_code());
        String reservationOffer = "";
        int count = 0;
        for (int i = 0; i < currentOrder.getDishes().size(); i++) {
            String offerName = currentOrder.getDishes().get(i).getDish_name();
            reservationOffer += offerName + "(" + currentOrder.getDishes().get(i).getDish_qty() + ")  ";
            count += currentOrder.getDishes().get(i).getDish_qty();
        }
        textViewTotalItems.setText(""+count);
        textViewCurrentOrderInfo.setText(reservationOffer);

        //TODO click buttonOrderAccepted this order remove from current order
        buttonOrderAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Received Order",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {

            return currentOrders.size();
    }

    private class CurrentOrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRestName;
        TextView textViewOrderStatus;
        TextView textViewTotalCost;
        TextView textViewTotalItems;
        Button buttonOrderAccepted;
        TextView textViewCurrentOrderInfo;
        TextView textViewConfirmationCode;

        public CurrentOrderViewHolder(View view) {
            super(view);
            this.textViewConfirmationCode = itemView.findViewById(R.id.tvConfirmationCode);
            this.textViewRestName = itemView.findViewById(R.id.tvRestaurantNameCurrentOrder);
            this.textViewOrderStatus = itemView.findViewById(R.id.tvOrderStatusCurrentOrder);
            this.textViewTotalCost = itemView.findViewById(R.id.tvCurrentOrderTotalCost);
            this.textViewTotalItems = itemView.findViewById(R.id.tvCurrentOrderTotalCount);
            this.buttonOrderAccepted = itemView.findViewById(R.id.btnCurrentOrderAccepted);
            this.textViewCurrentOrderInfo = itemView.findViewById(R.id.CurrentOrderInfo);
        }

    }

}
