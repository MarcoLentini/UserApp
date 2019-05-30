package com.example.userapp.AddComments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userapp.CurrentOrder.CurrentOrderItemModel;
import com.example.userapp.R;

import java.util.ArrayList;

public class CommentsDishesListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<CurrentOrderItemModel> orderedDishes;
    private Context context;
    private LayoutInflater mInflater;

    public CommentsDishesListAdapter(Context context,ArrayList<CurrentOrderItemModel> orderedDishes) {
        this.context = context;
        this.orderedDishes = orderedDishes;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        View view = mInflater.inflate(R.layout.comments_item, viewGroup,false);
        CommentsDishesViewHolder holder = new CommentsDishesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
        CommentsDishesViewHolder commentsDishesViewHolder= (CommentsDishesViewHolder)viewHolder;
        TextView tvOrderedDishesName = commentsDishesViewHolder.tvOrderedDishesName;
        View vThumbDown = commentsDishesViewHolder.vTumbDown;
        View vThumbUp = commentsDishesViewHolder.vThumbUp;

        CurrentOrderItemModel currentOrderItem= orderedDishes.get(pos);
        tvOrderedDishesName.setText(currentOrderItem.getDish_name());
        vThumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO send back the position and counter +1
                Toast.makeText(v.getContext(),"Excellent",Toast.LENGTH_SHORT).show();

            }
        });
        vThumbDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO send back the position and counter -1
                Toast.makeText(v.getContext(),"Excellent",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return orderedDishes.size();
    }

    private class CommentsDishesViewHolder extends RecyclerView.ViewHolder {
         TextView tvOrderedDishesName;
         View vThumbUp;
         View vTumbDown;

        public CommentsDishesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvOrderedDishesName = itemView.findViewById(R.id.tvDishNameCommentsItem);
            this.vTumbDown = itemView.findViewById(R.id.ivThumbDown);
            this.vThumbUp = itemView.findViewById(R.id.ivThumbUp);
        }
    }
}
