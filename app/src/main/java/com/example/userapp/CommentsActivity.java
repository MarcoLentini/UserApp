package com.example.userapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import com.example.userapp.Comments.CommentsDataModel;
import com.example.userapp.Comments.CommentsListAdapter;
import com.example.userapp.Information.LoginActivity;
import com.example.userapp.RestaurantMenu.HeaderOrMenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import com.example.userapp.Information.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class CommentsActivity extends AppCompatActivity
        {

    private FirebaseAuth auth;
    private final static String TAG = "CommentsActivity";
    private ArrayList<CommentsDataModel> commentsData;
    private CommentsListAdapter commentsListAdapter;
    private FirebaseFirestore db;
    private static final String userDataFile = "UserDataFile";
    private String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Log.d(TAG, "onCreate called.");
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
        }
        //Get Firestore instance

        db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPref = getSharedPreferences(userDataFile, Context.MODE_PRIVATE);

        if (auth.getCurrentUser() == null) {
            finish();
        }else{
            //get the user id when user is validated
            String useID = auth.getCurrentUser().getUid();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("userKey", useID);
            editor.commit();
        }
        userKey = sharedPref.getString("userKey","");
        Log.d(TAG, "userKey"+userKey);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        commentsData = new ArrayList<>();
        fillWithData();

        RecyclerView recyclerView = findViewById(R.id.rvMyComments);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        commentsListAdapter = new CommentsListAdapter(this, commentsData);
        recyclerView.setAdapter(commentsListAdapter);

    }




    private void fillWithData(){
         db.collection("comments")
                .whereEqualTo("userId", userKey)
                .addSnapshotListener((document, e) -> {
                    if (e != null) return;

                    for(DocumentChange dc : document.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            CommentsDataModel tmpComment = new CommentsDataModel(
                                    dc.getDocument().getString("custName"),
                                    dc.getDocument().getId(),  //commentsId
                                    dc.getDocument().getLong("reservationId"),  //reservationId
                                    dc.getDocument().getString("restId"),  //restId
                                    dc.getDocument().getString("restName"),  //restName
                                    dc.getDocument().getString("bikerId"),  //bikerId
                                    dc.getDocument().getString("userId"),  //userId
                                    dc.getDocument().getDouble("voteForRestaurant"),  //voteForRestaurant
                                    dc.getDocument().getDouble("voteForBiker"),  //voteForBiker
                                    dc.getDocument().getString("notes"),//notes,
                                    dc.getDocument().getDate("date")
                            );

                            Log.d(TAG, "tmpComment"+tmpComment.getCommentsId());
                            commentsData.add(tmpComment);
                            Collections.sort(commentsData,CommentComparator);
                            commentsListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        commentsListAdapter.notifyDataSetChanged();
        // getDataAndUpdateArrayList();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(CommentsActivity.this, LoginActivity.class));
            finish();

        }
    }
    //sign out method
    public void signOut() {
        auth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

            public static Comparator<CommentsDataModel> CommentComparator = (com1, com2) -> {

                Date date1 = com1.getDate();
                Date date2 = com2.getDate();

                //ascending order
                //return catPosition1.compareTo(catPosition2);
                //descending order
                return date2.compareTo(date1);
            };
}
