package com.example.userapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.userapp.CurrentOrder.CurrentOrderItemModel;
import com.example.userapp.CurrentOrder.CurrentOrderModel;
import com.example.userapp.HistoryOrder.HistoryOrderListAdapter;
import com.example.userapp.Information.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

;

public class HistoryOrderActivity extends AppCompatActivity {
    private static final int HISTORY_INTENT = 55;
    private static final String TAG = "HistoryOrderActivity";
    private RecyclerView recyclerViewHistoryOrder;
    private HistoryOrderListAdapter historyOrderListAdapter;
    public static ArrayList<CurrentOrderModel> historyOrders;
    private FirebaseAuth auth;
    public FirebaseFirestore db;
    private static final String userDataFile = "UserDataFile";
    private String userKey;
    private ProgressBar progressBarCurrentOrder;
    private View viewEmpytHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title=getString(R.string.menu_history_order);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBarCurrentOrder=findViewById(R.id.progress_bar_history_orders);
        viewEmpytHint = findViewById(R.id.viewHistoryOrdersEmptyHint);

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


          //data initialization
        historyOrders = new ArrayList<>();
        fillWithData();

        Log.d(TAG, "RecycleView initialization ");
        //RecycleView show the list of current orders
        recyclerViewHistoryOrder = findViewById(R.id.rvHistoryOrders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewHistoryOrder.setLayoutManager(layoutManager);
        //specify an Adapter
        historyOrderListAdapter = new HistoryOrderListAdapter(this, historyOrders);
        recyclerViewHistoryOrder.setAdapter(historyOrderListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        historyOrderListAdapter.notifyDataSetChanged();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(HistoryOrderActivity.this, LoginActivity.class));
            finish();

        }
    }

    private void fillWithData(){
        Log.d("QueryHistoryOrder", "Start fill with data...");
        progressBarCurrentOrder.setVisibility(View.VISIBLE);

        db.collection("reservations").whereEqualTo("cust_id", userKey)
                .whereEqualTo("is_current_order", false).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                QuerySnapshot document = task.getResult();
                if(!document.isEmpty()) {
                    for(DocumentSnapshot doc : document) {
                        ArrayList<CurrentOrderItemModel> tmpArrayList = new ArrayList<>();
                        if (doc.get("dishes") != null) {
                            Log.d("QueryHistoryOrder", "dishes not empty");

                            for (HashMap<String, Object> dish : (ArrayList<HashMap<String, Object>>) doc.get("dishes")) {
                                tmpArrayList.add(new CurrentOrderItemModel(
                                        (String) dish.get("dish_name"),
                                        (Double) dish.get("dish_price"),
                                        (Long) dish.get("dish_qty")));
                            }
                            CurrentOrderModel tmpHistoryOrderModel = new CurrentOrderModel(
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
                            //add this current order into the arraylist
                            Log.d("QueryHistoryOrder", "add  tmpHistoryOrderModel successful to arraylist!");
                            historyOrders.add(tmpHistoryOrderModel);
                            //progressBarCurrentOrder.setVisibility(View.GONE);
                            Collections.sort(historyOrders,OrdersComparator);
                            historyOrderListAdapter.notifyDataSetChanged();
                        }

                    }


                } else {

                }
                progressBarCurrentOrder.setVisibility(View.GONE);
            }
        });
    }
    //sign out method
    public void signOut() {
        auth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == HISTORY_INTENT){
                Toast.makeText(this, "AAAAAAA", Toast.LENGTH_SHORT).show();
            }
        }
    }

             @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

