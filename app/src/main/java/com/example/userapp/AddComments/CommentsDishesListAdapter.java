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
        TextView tvorderedDishesName = commentsDishesViewHolder.tvorderedDishesName;
        View vthumbDown = commentsDishesViewHolder.vthumbDown;
        View vthumbUp = commentsDishesViewHolder.vthumbUp;

        CurrentOrderItemModel currentOrderItem= orderedDishes.get(pos);
        tvorderedDishesName.setText(currentOrderItem.getDish_name());
        vthumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO send back the position and counter +1
                Toast.makeText(v.getContext(),"Excellent",Toast.LENGTH_SHORT).show();

            }
        });
        vthumbDown.setOnClickListener(new View.OnClickListener() {
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
         TextView tvorderedDishesName;
         View vthumbUp;
         View vthumbDown;

        public CommentsDishesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvorderedDishesName = itemView.findViewById(R.id.tvDishNameCommentsItem);
            this.vthumbDown = itemView.findViewById(R.id.ivThumbDown);
            this.vthumbUp = itemView.findViewById(R.id.ivThumbUp);
        }
    }
}
