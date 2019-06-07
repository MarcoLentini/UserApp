package com.example.userapp.HistoryOrder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.userapp.CurrentOrder.CurrentOrderModel;
import com.example.userapp.R;

import java.util.ArrayList;

public class HistoryOrderListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "HistoryOrderListAdapter";
    private Context context;
    private ArrayList<CurrentOrderModel> historyOrders;
    private LayoutInflater mInflater;

    public HistoryOrderListAdapter(Context context,ArrayList<CurrentOrderModel> historyOrders){
        this.context = context;
        this.historyOrders = historyOrders;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.history_order_info, viewGroup,false);
        HistoryOrderViewHolder holder = new HistoryOrderViewHolder(view);
        view.setOnClickListener(v -> {
            Intent myIntent = new Intent(view.getContext(), HistoryOrderDetailInfoActivity.class);
            int itemPosition = holder.getAdapterPosition();
            Bundle bn = new Bundle();
            bn.putInt("historyOrderData", itemPosition);
            myIntent.putExtras(bn);
            context.startActivity(myIntent);
         });

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
        Button  btnCommentForThisOrderFinish = historyOrderViewHolder.btnCommentForThisOrderFinish;

        CurrentOrderModel historyOrder = historyOrders.get(pos);
        textViewRestName.setText(historyOrder.getRest_name());
        textViewOrderStatus.setText(historyOrder.getRs_status());
        textViewTotalCost.setText(""+historyOrder.getTotal_income());
         String reservationOffer = "";
        int count = 0;
        for (int i = 0; i < historyOrder.getDishes().size(); i++) {
            String offerName = historyOrder.getDishes().get(i).getDish_name();
            reservationOffer += offerName + "(" + historyOrder.getDishes().get(i).getDish_qty() + ")  ";
            count += historyOrder.getDishes().get(i).getDish_qty();
        }
        textViewTotalItems.setText(""+count);
        textViewHistoryOrderInfo.setText(reservationOffer);

        if (!historyOrder.getIs_commented()){
            btnCommentForThisOrderFinish.setVisibility(View.VISIBLE);
        }else {
            btnCommentForThisOrderFinish.setVisibility(View.GONE);
        }

        btnCommentForThisOrderFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "btnCommentForThisOrderFinish is clicked,AddCommentsActivity is called.");
                Intent intent = new Intent(view.getContext(), com.example.userapp.Comments.AddCommentsActivity.class);
                Bundle bn = new Bundle();
                bn.putInt("historyOrderData", pos);
                intent.putExtras(bn);
                context.startActivity(intent);;
            }
        });
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
        Button  btnCommentForThisOrderFinish;

        public HistoryOrderViewHolder(View itemView) {
            super(itemView);
            this.textViewRestName = itemView.findViewById(R.id.tvRestaurantNameHistoryOrderFinished);
            this.textViewOrderStatus = itemView.findViewById(R.id.tvOrderStatusHistoryOrderFinished);
            this.textViewTotalCost = itemView.findViewById(R.id.tvOrderTotalCostFinished);
            this.textViewTotalItems = itemView.findViewById(R.id.tvOrderTotalCountFinished);
            this.textViewHistoryOrderInfo = itemView.findViewById(R.id.OrderInfoFinished);
            this.btnCommentForThisOrderFinish = itemView.findViewById(R.id.btnCommentForThisOrderFinish);
        }
    }
}
