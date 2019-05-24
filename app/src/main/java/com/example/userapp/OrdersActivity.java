package com.example.userapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.userapp.CurrentOrder.CurrentOrderItemModel;
import com.example.userapp.CurrentOrder.CurrentOrderListAdapter;
import com.example.userapp.CurrentOrder.CurrentOrderModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

public class OrdersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "OrderdActivity";
    private static  final  int  CURRENT_ORDER_DETAIL_INFO_CODE =1;
    private RecyclerView recyclerViewCurrentOrder;
    private CurrentOrderListAdapter currentOrderListAdapter;
    public static ArrayList<CurrentOrderModel> currentOrders;

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

        progressBarCurrentOrder=findViewById(R.id.progress_bar_current_orders);
        //Get Firebase auth instance
        FirebaseAuth auth = FirebaseAuth.getInstance();

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

         currentOrders = new ArrayList<>();

         fillWithData();

         Log.d("Tag", "RecycleView initialization ");

         //RecycleView show the list of current orders
        recyclerViewCurrentOrder = findViewById(R.id.rvCurrentOrders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewCurrentOrder.setLayoutManager(layoutManager);
        //specify an Adapter
         // Todo : in CurrentOrderListAdapter the input parameter is ArrayList our data were stored in Hashmap
        currentOrderListAdapter = new CurrentOrderListAdapter(this, currentOrders);
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
            Intent intent = new Intent(OrdersActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show();
         }
        else if (id == R.id.nav_setting){
            Intent intent = new Intent(OrdersActivity.this, SettingsActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Setting",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_help){
            Intent intent = new Intent(OrdersActivity.this, HelpActivity.class);
            startActivity(intent);
                Toast.makeText(this,"Help",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_orders){

                Toast.makeText(this,"Orders",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_star){
            Intent intent = new Intent(OrdersActivity.this, FavoritesActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Favorite",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_logout){
                Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_comments){
            Intent intent = new Intent(OrdersActivity.this, CommentsActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Comments",Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void fillWithData(){
        Log.d("QueryCurrentOrder", "Start fill with data...");
        progressBarCurrentOrder.setVisibility(View.VISIBLE);
        db.collection("reservations")
                 .whereEqualTo("cust_id", userKey)
                 .whereEqualTo("is_current_order", true)
                 .addSnapshotListener(new EventListener<QuerySnapshot>() {

                     @Override
                     public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                         if (e != null) {
                             System.err.println("Listen failed" + e);
                             return;
                         }
                         //everytime first clear the array
                         currentOrders.clear();
                         if (!queryDocumentSnapshots.isEmpty()) {
                             Log.d("QueryCurrentOrder", "queryDocumentSnapshots not empty");
                             for (DocumentSnapshot dc : queryDocumentSnapshots) {
                                 Log.d("QueryCurrentOrder", "get DocumentSnapshot from queryDocumentSnapshots");
                                 System.out.println("number of results"+queryDocumentSnapshots.size());
                                 ArrayList<CurrentOrderItemModel> tmpArrayList = new ArrayList<>();
                                 if (dc.get("dishes") != null ) {
                                     Log.d("QueryCurrentOrder", "dishes not empty");

                                     for (HashMap<String, Object> dish : (ArrayList<HashMap<String, Object>>) dc.get("dishes")) {
                                         tmpArrayList.add(new CurrentOrderItemModel(
                                                 (String) dish.get("dish_name"),
                                                 (Double) dish.get("dish_price"),
                                                 (Long) dish.get("dish_qty")));
                                     }
                                     CurrentOrderModel tmpCurrentOrderModel = new CurrentOrderModel(
                                             (Long) dc.get("confirmation_code"),
                                             (Boolean) dc.get("is_current_order"),
                                             (String) dc.get("cust_id"),//customer id
                                             (String) dc.get("rs_status"), // order status
                                             (Long) dc.get("rs_id"), //order id
                                             (Timestamp) dc.get("timestamp"),     //order created time
                                             (String) dc.get("rest_name"), // rest_name
                                             tmpArrayList,
                                             (Double) dc.get("total_income"),
                                             (Timestamp) dc.get("delivery_time"),
                                             (String) dc.get("cust_address")
                                     );
                                     //add this current order into the arraylist
                                     Log.d("QueryCurrentOrder", "add  tmpCurrentOrderModel successful to arraylist!");
                                     currentOrders.add(tmpCurrentOrderModel);
                                    // currentOrdersHashMap.put(tmpCurrentOrderModel.getRs_id(), tmpCurrentOrderModel);
                                     System.out.println("size of current order"+currentOrders.size());
                                 }
                             }
                             for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                                 if (dc.getType() == DocumentChange.Type.ADDED) {

                                  }
                                 if (dc.getType() == DocumentChange.Type.MODIFIED) {
                                  }
                                 if (dc.getType() == DocumentChange.Type.REMOVED) {
                                  }
                             }
                             progressBarCurrentOrder.setVisibility(View.GONE);
                             //very important
                             currentOrderListAdapter.notifyDataSetChanged();
                         }else {
                             Log.d("QueryCurrentOrder", "No such document");
                         }
                     }
                 });
    }

}
