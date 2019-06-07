package com.example.userapp.Comments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.userapp.Comments.CommentsDataModel;
import com.example.userapp.R;

import java.util.ArrayList;

public class CommentsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "CommentsListAdapter";
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<CommentsDataModel> commentsData;

    public CommentsListAdapter(Context context, ArrayList<CommentsDataModel> commentsData) {
        this.commentsData = commentsData;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.cardview_my_comments,viewGroup,false);
        CommentsViewHolder  holder = new  CommentsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        CommentsViewHolder commentsViewHolder = (CommentsViewHolder)viewHolder;

        TextView textViewCommentsRestaurantName = commentsViewHolder.textViewCommentsRestaruantName;
        TextView textViewCommentsCustID = commentsViewHolder.textViewCommentsCustID;
        RatingBar showRatingDeliveryService = commentsViewHolder.showRatingDeliveryService;
        RatingBar showRatingFoodQuantity = commentsViewHolder.showRatingFoodQuantity;
        TextView textViewCommentsCustNotes = commentsViewHolder.textViewCommentsCustNotes;
        // View viewBtnDeleteComment = commentsViewHolder.viewBtnDeleteComment;

        CommentsDataModel myComments = commentsData.get(position);
        Log.d(TAG, commentsData.get(position).getRestName());
        textViewCommentsCustID.setText(myComments.getCustName());

        textViewCommentsRestaurantName.setText(""+myComments.getRestName());
        //textViewCommentsCustID.setText(myComments.getUserId());
        showRatingDeliveryService.isIndicator();
        showRatingDeliveryService.setRating(myComments.getVoteForBiker().floatValue());
        showRatingFoodQuantity.isIndicator();
        showRatingFoodQuantity.setRating(myComments.getVoteForRestaurant().floatValue());
        textViewCommentsCustNotes.setMaxLines(10);
        textViewCommentsCustNotes.setText(myComments.getNotes());

    }

    @Override
    public int getItemCount() {
        return commentsData.size();
    }

    private class CommentsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCommentsRestaruantName;
        TextView textViewCommentsCustID;
        RatingBar showRatingDeliveryService;
        RatingBar showRatingFoodQuantity;
        TextView textViewCommentsCustNotes;
        //View viewBtnDeleteComment;

        public CommentsViewHolder(View view) {
            super(view);
            this.textViewCommentsRestaruantName = view.findViewById(R.id.myCommentsRestaurantName);
            this.textViewCommentsCustID =  view.findViewById(R.id.myCommentsCustID);
            this.showRatingDeliveryService =  view.findViewById(R.id.showRatingDeliveryService);
            this.showRatingFoodQuantity =  view.findViewById(R.id.showRatingFoodQuality);
            this.textViewCommentsCustNotes =  view.findViewById(R.id.myCommentsCustNotes);
           // this.viewBtnDeleteComment = view.findViewById(R.id.btnDeleteComment);
        }
    }
}
