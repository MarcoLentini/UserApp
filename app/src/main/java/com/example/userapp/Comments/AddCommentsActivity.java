package com.example.userapp.Comments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userapp.CommentsActivity;
import com.example.userapp.CurrentOrder.CurrentOrderModel;
import com.example.userapp.HistoryOrderActivity;
import com.example.userapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCommentsActivity extends AppCompatActivity {
    private FirebaseFirestore db ;
    private final static String TAG = "AddCommentsActivity";
    private TextView textViewMsg;
    private Button btnSubmitComments;
    private RatingBar ratingBarFoodQuality;
    private RatingBar ratingBarDeliveryService;
    private CurrentOrderModel historyOrderModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_add_comments);

        String title = getString(R.string.title_activity_comments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
        //Get Firebase auth instance
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
        }
        //get Firestore instance
        db = FirebaseFirestore.getInstance();

        Intent receivedIntent = getIntent();
        Integer itemPosition = receivedIntent.getExtras().getInt("historyOrderData");
        historyOrderModel = HistoryOrderActivity.historyOrders.get(itemPosition);

        textViewMsg = findViewById(R.id.edit_text_input_comments);
        textViewMsg.setMaxLines(10);
        btnSubmitComments = findViewById(R.id.btnSubmitComments);
        ratingBarFoodQuality = findViewById(R.id.ratingBarFoodQuality);
        ratingBarFoodQuality.setStepSize((float) 0.5);
         ratingBarDeliveryService = findViewById(R.id.ratingBarDeliveryService);
        ratingBarDeliveryService.setStepSize((float) 0.5);


        btnSubmitComments.setOnClickListener(v -> {
            Double ratingFoodQuality = Double.valueOf(ratingBarDeliveryService.getRating());
            Double ratingDeliveryService = Double.valueOf(ratingBarDeliveryService.getRating());
            String myComments = textViewMsg.getText().toString();

            CommentsDataModel commentsDataModel = new CommentsDataModel(
                    historyOrderModel.getCust_name(),
                    "0",//initialized with 0
                    historyOrderModel.getRs_id(),
                    historyOrderModel.getRest_id(),
                    historyOrderModel.getRest_name(),
                    historyOrderModel.getBiker_id(),
                    historyOrderModel.getCust_id(),
                    ratingFoodQuality,
                    ratingDeliveryService,
                    myComments,
                    new Date()
            );

            Log.d(TAG, "This is my comments " + commentsDataModel.toString());

            DocumentReference restDRef = db.collection("restaurant").document(historyOrderModel.getRest_id());
            DocumentReference commentsRef = db.collection("comments").document();
            DocumentReference reservationsRef = db.collection("reservations").document(historyOrderModel.getOrderID());

            restDRef.get().addOnCompleteListener(t -> {
                if (t.isSuccessful()) {
                    Double sumRating = t.getResult().getDouble("sumOfRating") + ratingFoodQuality;
                    Long numbRating = t.getResult().getLong("numbOfRating") + 1;

                    Double avg = sumRating / numbRating;

                    WriteBatch batch = db.batch();
                    batch.update(restDRef, "sumOfRating", sumRating, "numbOfRating", numbRating, "rating", avg);
                    batch.set(commentsRef, commentsDataModel);
                    batch.update(reservationsRef, "is_commented", true);

                    batch.commit().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "Change is_commented of history to true");
                            historyOrderModel.setIs_commented(true);

                            Log.e(TAG, "Going to Comments Activity");
                            finish();
                        } else {
                            Log.d("Comments", "Failed batch write");
                        }
                    });
                }
            });
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
