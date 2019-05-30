package com.example.userapp.HistoryOrder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.userapp.R;

import java.util.ArrayList;

public class HistoryOrderListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<HistoryOrderModel> historyOrders;
    private LayoutInflater mInflater;

    public HistoryOrderListAdapter(Context context,ArrayList<HistoryOrderModel> historyOrders){
        this.context = context;
        this.historyOrders = historyOrders;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.history_order_info, viewGroup,false);
        HistoryOrderViewHolder holder = new HistoryOrderViewHolder(view);
/*
        view.setOnClickListener(v -> {
            Intent myIntent = new Intent(mainFragment.getContext(), InProgressDetailsActivity.class);
            int itemPosition = holder.getAdapterPosition();
            Bundle bn = new Bundle();
            bn.putInt("reservationCardData", itemPosition);
            myIntent.putExtras(bn);
            mainFragment.startActivityForResult(myIntent, tabFrag.INPROGRESS_REQ);
        });
*/
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
        HistoryOrderViewHolder historyOrderViewHolder = (HistoryOrderViewHolder)viewHolder;
        TextView textViewRestName = historyOrderViewHolder.textViewRestName;
        TextView textViewOrderStatus = historyOrderViewHolder.textViewOrderStatus;
        TextView textViewTotalCost =historyOrderViewHolder.textViewTotalCost;
        TextView textViewTotalItems = historyOrderViewHolder.textViewTotalItems;
        TextView textViewHistoryOrderInfo = historyOrderViewHolder.textViewHistoryOrderInfo;

        HistoryOrderModel historyOrder = historyOrders.get(pos);
        textViewRestName.setText(historyOrder.getRest_name());
        textViewOrderStatus.setText(historyOrder.getRs_status());
        textViewTotalCost.setText(""+historyOrder.getTotal_cost());
         String reservationOffer = "";
        int count = 0;
        for (int i = 0; i < historyOrder.getDishes().size(); i++) {
            String offerName = historyOrder.getDishes().get(i).getDish_name();
            reservationOffer += offerName + "(" + historyOrder.getDishes().get(i).getDish_qty() + ")  ";
            count += historyOrder.getDishes().get(i).getDish_qty();
        }
        textViewTotalItems.setText(""+count);
        textViewHistoryOrderInfo.setText(reservationOffer);

    }

    @Override
    public int getItemCount() {
        return historyOrders.size();
    }


    public class HistoryOrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRestName;
        TextView textViewOrderStatus;
        TextView textViewTotalCost;
        TextView textViewTotalItems;
        TextView textViewHistoryOrderInfo;
        public HistoryOrderViewHolder(View itemView) {
            super(itemView);
            this.textViewRestName = itemView.findViewById(R.id.tvRestaurantNameHistoryOrderFinished);
            this.textViewOrderStatus = itemView.findViewById(R.id.tvOrderStatusHistoryOrderFinished);
            this.textViewTotalCost = itemView.findViewById(R.id.tvOrderTotalCostFinished);
            this.textViewTotalItems = itemView.findViewById(R.id.tvOrderTotalCountFinished);
            this.textViewHistoryOrderInfo = itemView.findViewById(R.id.OrderInfoFinished);
        }
    }
}
