package com.example.userapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userapp.View.StarLinearLayout;

public class AddCommentsActivity extends AppCompatActivity {

    private StarLinearLayout mStarDeliveryService;
    private StarLinearLayout mStarFoodQuality;
    private TextView textViewDeliveryService;
    private TextView textViewFoodQuality;
    private TextView textViewMsg;
    private Button btnSubmitComments;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_add_comments);

        mStarDeliveryService = findViewById(R.id.commentFiveStarVoteDeliveryService);
        textViewDeliveryService = findViewById(R.id.commentTagDeliveryService);
        mStarFoodQuality = findViewById(R.id.commentFiveStarVoteFoodQuality);
        textViewFoodQuality = findViewById(R.id.commentTagFoodQuality);
        textViewMsg = findViewById(R.id.edit_text_input_comments);
        btnSubmitComments = findViewById(R.id.btnSubmitComments);

        mStarDeliveryService.setChangeListener(new StarLinearLayout.ChangeListener() {
            @Override
            public void Change(int level) {
                textViewDeliveryService.setText(level);
                Toast.makeText(AddCommentsActivity.this, " " + level + " ", Toast.LENGTH_LONG).show();
            }
        });

        mStarFoodQuality.setChangeListener(new StarLinearLayout.ChangeListener() {
            @Override
            public void Change(int level) {
                textViewFoodQuality.setText(level );
                Toast.makeText(AddCommentsActivity.this, " " + level + " ", Toast.LENGTH_LONG).show();
            }
        });


        btnSubmitComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double deliveryServiceLevel = 0.0;
                Double foodQualityService = 0.0;
                if (!textViewDeliveryService.getText().toString().isEmpty()){
                    deliveryServiceLevel = Double.parseDouble(textViewDeliveryService.getText().toString());
                }
               if (!textViewFoodQuality.getText().toString().isEmpty()){
                   foodQualityService = Double.parseDouble(textViewFoodQuality.getText().toString());
               }
              String myComments = textViewMsg.getText().toString();

               //TODO : LAB5 send comments to firebase
            }
        });
    }
}
