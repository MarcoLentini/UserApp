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

import com.example.userapp.Comments.CommentsDataModel;
import com.example.userapp.Comments.CommentsListAdapter;
import com.example.userapp.Information.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class CommentsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        commentsData = new ArrayList<>();
        fillWithData();

        RecyclerView recyclerView = findViewById(R.id.rvMyComments);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        commentsListAdapter = new CommentsListAdapter(this, commentsData);
        recyclerView.setAdapter(commentsListAdapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
         if(id == android.R.id.home){
            onBackPressed();
            //getSupportFragmentManager().popBackStack();
        }

        return super.onOptionsItemSelected(item);
    }

     @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home){
            Intent intent = new Intent(CommentsActivity.this, MainActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_setting){
            Intent intent = new Intent(CommentsActivity.this, SettingsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_help){
            Intent intent = new Intent(CommentsActivity.this, HelpActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_orders){
            Intent intent = new Intent(CommentsActivity.this, OrdersActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_star){
            Intent intent = new Intent(CommentsActivity.this, FavoritesActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            signOut();
        }else if (id == R.id.nav_comments){

        }else if (id == R.id.nav_history_order){
            Intent intent = new Intent(CommentsActivity.this, HistoryOrderActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

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

}
