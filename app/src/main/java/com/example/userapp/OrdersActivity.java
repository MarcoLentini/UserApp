package com.example.userapp;

import android.app.AlertDialog;
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

import com.example.userapp.CurrentOrder.CurrentOrderItemModel;
import com.example.userapp.CurrentOrder.CurrentOrderListAdapter;
import com.example.userapp.CurrentOrder.CurrentOrderModel;
import com.example.userapp.Information.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class OrdersActivity extends AppCompatActivity
        {

    private static final String TAG = "OrderdActivity";
    private static  final  int  CURRENT_ORDER_DETAIL_INFO_CODE =1;
    private RecyclerView recyclerViewCurrentOrder;
    private CurrentOrderListAdapter currentOrderListAdapter;

    public static ArrayList<CurrentOrderModel> currentOrders;
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
         currentOrders = new ArrayList<>();
         getCurrentOrder();

         Log.d("Tag", "RecycleView initialization ");

         //RecycleView show the list of current orders
        recyclerViewCurrentOrder = findViewById(R.id.rvCurrentOrders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewCurrentOrder.setLayoutManager(layoutManager);
        //specify an Adapter

        currentOrderListAdapter = new CurrentOrderListAdapter(this,currentOrders);
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

            public void getCurrentOrder(){
                db.collection("reservations").whereEqualTo("cust_id", userKey).whereEqualTo("is_current_order", true).addSnapshotListener((document, e)->{
                    if (e != null) return;

                    for(DocumentChange dc : document.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            QueryDocumentSnapshot doc = dc.getDocument();
                            ArrayList<CurrentOrderItemModel> tmpArrayList = new ArrayList<>();
                            for (HashMap<String, Object> dish : (ArrayList<HashMap<String, Object>>) doc.get("dishes")) {
                                tmpArrayList.add(new CurrentOrderItemModel(
                                        (String) dish.get("dish_name"),
                                        (Double) dish.get("dish_price"),
                                        (Long) dish.get("dish_qty")));
                            }
                            CurrentOrderModel tmpReservationModel = new CurrentOrderModel(
                                    doc.getId(),
                                    doc.getLong("rs_id"),
                                    doc.getString("rs_status"),
                                    doc.getTimestamp("timestamp"),
                                    tmpArrayList,
                                    doc.getBoolean("is_current_order"),
                                    doc.getLong("confirmation_code"),
                                    doc.getDouble("total_income"),
                                    doc.getString("notes"),

                                    doc.getString("cust_id"),
                                    doc.getString("cust_name"),
                                    doc.getString("cust_address"),
                                    doc.getBoolean("is_commented"),

                                    doc.getString("rest_id"),
                                    doc.getString("rest_name"),

                                    doc.getString("biker_id"),
                                    doc.getDate("delivery_time")
                            );
                            currentOrders.add(tmpReservationModel);
                            Collections.sort(currentOrders,OrdersComparator);
                            currentOrderListAdapter.notifyDataSetChanged();

                        } else if(dc.getType() == DocumentChange.Type.MODIFIED){
                            QueryDocumentSnapshot doc = dc.getDocument();
                            for(CurrentOrderModel com : currentOrders){
                                if(com.getOrderID().equals(doc.getId())){
                                    com.setRs_status(doc.getString("rs_status"));
                                    break;
                                }
                            }
                        } else if(dc.getType() == DocumentChange.Type.REMOVED){
                            QueryDocumentSnapshot doc = dc.getDocument();
                            ArrayList<CurrentOrderItemModel> tmpArrayList = new ArrayList<>();
                            for (HashMap<String, Object> dish : (ArrayList<HashMap<String, Object>>) doc.get("dishes")) {
                                tmpArrayList.add(new CurrentOrderItemModel(
                                        (String) dish.get("dish_name"),
                                        (Double) dish.get("dish_price"),
                                        (Long) dish.get("dish_qty")));
                            }
                            CurrentOrderModel tmpReservationModel = new CurrentOrderModel(
                                    doc.getId(),
                                    doc.getLong("rs_id"),
                                    doc.getString("rs_status"),
                                    doc.getTimestamp("timestamp"),
                                    tmpArrayList,
                                    doc.getBoolean("is_current_order"),
                                    doc.getLong("confirmation_code"),
                                    doc.getDouble("total_income"),
                                    doc.getString("notes"),

                                    doc.getString("cust_id"),
                                    doc.getString("cust_name"),
                                    doc.getString("cust_address"),
                                    doc.getBoolean("is_commented"),

                                    doc.getString("rest_id"),
                                    doc.getString("rest_name"),

                                    doc.getString("biker_id"),
                                    doc.getDate("delivery_time")
                            );
                            currentOrders.remove(tmpReservationModel);
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle(R.string.leave_comment_title);
                            builder.setMessage(R.string.leave_comment);
                            builder.setPositiveButton(getString(R.string.ok_button),(dialog, which) -> {
                                Intent intent = new Intent(OrdersActivity.this, HistoryOrderActivity.class);
                                startActivity(intent);
                            })
                                    .setNegativeButton(getString(R.string.later_btn), (dialog, which) -> {
                                        dialog.dismiss();
                                    })
                                    .create().show();


                        }
                    }
                });
            }

            public static Comparator<CurrentOrderModel> OrdersComparator = (com1, com2) -> {

                Date date1 = com1.getDelivery_time();
                Date date2 = com2.getDelivery_time();

                //ascending order
                //return catPosition1.compareTo(catPosition2);
                //descending order
                return date2.compareTo(date1);
            };
}


