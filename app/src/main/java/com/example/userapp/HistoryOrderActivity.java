package com.example.userapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.Toast;
;
import com.example.userapp.CurrentOrder.CurrentOrderItemModel;
import com.example.userapp.CurrentOrder.CurrentOrderModel;
import com.example.userapp.HistoryOrder.HistoryOrderListAdapter;
import com.example.userapp.Information.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryOrderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(HistoryOrderActivity.this, LoginActivity.class));
            finish();

        }
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
            Intent intent = new Intent(HistoryOrderActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_setting){
            Intent intent = new Intent(HistoryOrderActivity.this, SettingsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_help){
            Intent intent = new Intent(HistoryOrderActivity.this, HelpActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_orders){
            Intent intent = new Intent(HistoryOrderActivity.this, OrdersActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_star){
            Intent intent = new Intent(HistoryOrderActivity.this, FavoritesActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            signOut();
        }else if (id == R.id.nav_comments){
            Intent intent = new Intent(HistoryOrderActivity.this, CommentsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_history_order){

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //TODO LAB5 get history order data form firebase realtime
    // (an idea related to comments) when there is a history order send a notification for user to make comments
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
                                    doc.getString("cust_name"),
                                    doc.getBoolean("is_commented"),
                                    doc.getString("rest_id"),
                                    doc.getString("rest_address"),

                                    doc.getString("biker_id"),
                                    doc.getTimestamp("delivery_time")
                            );
                            //add this current order into the arraylist
                            Log.d("QueryHistoryOrder", "add  tmpHistoryOrderModel successful to arraylist!");
                            historyOrders.add(tmpHistoryOrderModel);
                            //progressBarCurrentOrder.setVisibility(View.GONE);
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
}
