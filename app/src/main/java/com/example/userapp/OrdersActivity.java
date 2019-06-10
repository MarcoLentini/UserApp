package com.example.userapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.example.userapp.CurrentOrder.CurrentOrderListAdapter;
import com.example.userapp.Information.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

public class OrdersActivity extends AppCompatActivity
        {

    private static final String TAG = "OrderdActivity";
    private static  final  int  CURRENT_ORDER_DETAIL_INFO_CODE =1;
    private RecyclerView recyclerViewCurrentOrder;
    private CurrentOrderListAdapter currentOrderListAdapter;

    private  FirebaseAuth auth;
    public FirebaseFirestore db;
    private static final String userDataFile = "UserDataFile";
    private String userKey;
    private ProgressBar progressBarCurrentOrder;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title=getString(R.string.menu_orders);
        getSupportActionBar().setTitle(title);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBarCurrentOrder=findViewById(R.id.progress_bar_current_orders);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

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


         Log.d("Tag", "RecycleView initialization ");

         //RecycleView show the list of current orders
        recyclerViewCurrentOrder = findViewById(R.id.rvCurrentOrders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewCurrentOrder.setLayoutManager(layoutManager);
        //specify an Adapter

        currentOrderListAdapter = new CurrentOrderListAdapter(this, MainActivity.currentOrders);
        recyclerViewCurrentOrder.setAdapter(currentOrderListAdapter);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        currentOrderListAdapter.notifyDataSetChanged();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(OrdersActivity.this, LoginActivity.class));
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


