package com.example.userapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.userapp.Comments.CommentsDataModel;
import com.example.userapp.CurrentOrder.CurrentOrderItemModel;
import com.example.userapp.CurrentOrder.CurrentOrderModel;
import com.example.userapp.Favorites.FavoritesModel;
import com.example.userapp.Helper.Haversine;
import com.example.userapp.Information.LoginActivity;
import com.example.userapp.Restaurant.FilterRestaurantsActivity;
import com.example.userapp.Restaurant.PopularRestaurantsListAdapter;
import com.example.userapp.Restaurant.RestaurantModel;
import com.example.userapp.Restaurant.RestaurantsListAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int FILTER_RESTAURANTS_ACTIVITY = 1;
    private static final int PERMISSIONS_REQUEST = 100;

    private static final String TAG = "MainActivity";
    public static ArrayList<RestaurantModel> restaurantsData;
    public static ArrayList<CurrentOrderModel> currentOrders;
    private RecyclerView.Adapter restaurantsAdapter;
    private RecyclerView.Adapter popularRestaurantsAdapter;
    private ArrayList<String> receivedFilters;
    private FirebaseFirestore db;
    private ProgressBar pbRestaurants;
    private TextView tvRestaurantsCountValue;
    private TextView tvRestaurantsFiltersValue;
    private NavigationView navigationView;
    public static ArrayList<FavoritesModel> favoritesData;
    private FirebaseAuth auth;
    private static final String userDataFile = "UserDataFile";
    private String userKey;
    //comments
    public static ArrayList<CommentsDataModel> commentsData;
    public FusedLocationProviderClient fusedLocationClient;
    public static ArrayList<RestaurantModel> top5Restaurant;

    private GeoPoint geo_location;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        } else {
            //get the user id when user is validated
            String useID = auth.getCurrentUser().getUid();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("userKey", useID);
            editor.commit();
        }
        userKey = sharedPref.getString("userKey", "");

        //adding toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        restaurantsData = new ArrayList<>();
        favoritesData = new ArrayList<>();
        currentOrders = new ArrayList<>();
        top5Restaurant = new ArrayList<>();
        commentsData = new ArrayList<>();

        // Location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        int res = check_GPS();
        if(res == 0){
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            geo_location = new GeoPoint(location.getLatitude(), location.getLongitude()); // Logic to handle location object
                            getDataAndUpdateArrayList();
                            fillWithData();
                            getCurrentOrder();
                            getRating();
                        }
                    });
        } else{
            finish();
        }
        //adding drawerLayout and  navigationView
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        pbRestaurants = findViewById(R.id.progress_bar_restaurants);
        tvRestaurantsCountValue = findViewById(R.id.textViewRestaurantsCountValue);
        tvRestaurantsFiltersValue = findViewById(R.id.textViewFiltersCountValue);


        //recyclerView for the popular restaurant
        RecyclerView recyclerViewPopular = findViewById(R.id.recyclerViewTopRestaurants);
        RecyclerView.LayoutManager layoutManagerHorizential = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPopular.setLayoutManager(layoutManagerHorizential);
        // specify an Adapter
        popularRestaurantsAdapter = new PopularRestaurantsListAdapter(this, top5Restaurant,favoritesData);
        recyclerViewPopular.setAdapter(popularRestaurantsAdapter);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewRestaurants);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        restaurantsAdapter = new RestaurantsListAdapter(this, restaurantsData,favoritesData);
        recyclerView.setAdapter(restaurantsAdapter);


        View hView =  navigationView.getHeaderView(0);
        ImageView imageViewNavHeader = hView.findViewById(R.id.imageViewNavHeader);
        TextView name= hView.findViewById(R.id.textViewNavHeaderTitle);

        db.collection("users").document(userKey).get().addOnCompleteListener(t -> {
            if(t.isSuccessful()){
                DocumentSnapshot doc = t.getResult();
                name.setText(doc.getString("username"));
                Uri url = Uri.parse(doc.getString("image_url"));
                Glide.with(this).load(url).placeholder(R.drawable.user_logo).into(imageViewNavHeader);
            }
        });
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
        getMenuInflater().inflate(R.menu.main_search, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search_restaurants);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint(getString(R.string.hint_search_restaurants));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ((RestaurantsListAdapter)restaurantsAdapter).getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((RestaurantsListAdapter)restaurantsAdapter).getFilter().filter(newText);
                return true;
            }
        });

        MenuItem menuItemSearch = menu.findItem(R.id.action_search_restaurants);
        menuItemSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                menu.findItem(R.id.action_filter_restaurants).setVisible(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                menu.findItem(R.id.action_filter_restaurants).setVisible(true);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_filter_restaurants:
                Intent intent = new Intent(this, FilterRestaurantsActivity.class);
                if(receivedFilters != null) {
                    Bundle bn = new Bundle();
                    bn.putStringArrayList("selectedFilters", receivedFilters);
                    intent.putExtras(bn);
                }
                startActivityForResult(intent, FILTER_RESTAURANTS_ACTIVITY);
                // Check if we're running on Android 5.0 or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Apply activity transition
                    overridePendingTransition(R.anim.restaurants_filter_slide_in, R.anim.restaurants_filter_slide_out);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*implement actions for NavigationItemSelect*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //handle navigation view item clicks here
        int id = menuItem.getItemId();

        if (id == R.id.nav_setting){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_help){
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_orders){
            Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_star){
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            signOut();
        }else if (id == R.id.nav_home){

        }else if (id == R.id.nav_comments){
            Intent intent = new Intent(MainActivity.this, CommentsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_history_order){
            Intent intent = new Intent(MainActivity.this, HistoryOrderActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {

            if(requestCode == FILTER_RESTAURANTS_ACTIVITY) {
                receivedFilters = data.getStringArrayListExtra("selectedFilters");
                if(receivedFilters != null) {
                    if(receivedFilters.isEmpty()) {

                        ((RestaurantsListAdapter) restaurantsAdapter).removeFilters();
                        tvRestaurantsFiltersValue.setText("0");
                        int count = ((RestaurantsListAdapter) restaurantsAdapter).getItemCount();
                        tvRestaurantsCountValue.setText(String.valueOf(count));
                    }
                    else {


                        ((RestaurantsListAdapter) restaurantsAdapter).setFilters(receivedFilters);
                        int count = ((RestaurantsListAdapter) restaurantsAdapter).getItemCount();
                        tvRestaurantsCountValue.setText(String.valueOf(count));
                        tvRestaurantsFiltersValue.setText(String.valueOf(receivedFilters.size()));
                    }
                }
            }
        }
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
                            doc.getTimestamp("delivery_time")
                    );
                    currentOrders.add(tmpReservationModel);
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
                            doc.getTimestamp("delivery_time")
                    );
                    currentOrders.remove(tmpReservationModel);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.leave_comment_title);
                    builder.setMessage(R.string.leave_comment);
                    builder.setPositiveButton(getString(R.string.ok_button),(dialog, which) -> {
                        Intent intent = new Intent(MainActivity.this, HistoryOrderActivity.class);
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

    public void fillWithData(){
        db.collection("favorites")
                .whereEqualTo("userID", userKey)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        QuerySnapshot docs = task.getResult();
                        if(!docs.isEmpty()){
                            for(DocumentSnapshot doc : docs.getDocuments()){
                                RestaurantModel restaurantModel = null;
                                if (doc.get("restaurantModel") != null){
                                    HashMap<String, Object> rest = (HashMap<String, Object>) doc.get("restaurantModel");
                                    restaurantModel =new RestaurantModel(
                                            true,
                                            (String) rest.get("id"),
                                            (String) rest.get("name"),
                                            (Double)  rest.get("deliveryFee"),
                                            (String) rest.get("description"),
                                            (String) rest.get("restaurantLogo"),
                                            (String) rest.get("address"),
                                            (Double) rest.get("distance"),
                                            (Double) rest.get("rating")
                                    );
                                }

                                FavoritesModel favoritesModel = new FavoritesModel(
                                        doc.getId(),
                                        doc.getString("userID"),
                                        doc.getString("restaurantID"),
                                        restaurantModel );

                                Log.d(TAG, "favoritesModel"+favoritesModel.getId());
                                favoritesData.add(favoritesModel);
                            }
                        }
                    }
          });

    }

    public void getRating(){
        db.collection("restaurant").whereGreaterThan("rating", 0).orderBy("rating").limit(5).get().addOnCompleteListener(t->{
            if(t.isSuccessful()){
                QuerySnapshot documents = t.getResult();
                if(!documents.isEmpty()){
                    for(DocumentSnapshot doc : documents){
                        RestaurantModel rm = new RestaurantModel(
                                false,
                                doc.getId(),
                                doc.getString("rest_name"),
                                doc.getDouble("delivery_fee"),
                                doc.getString("rest_descr"),
                                doc.getString("rest_image"),
                                doc.getString("rest_address"),
                                Haversine.getHaversineDistance(
                                        doc.getGeoPoint("rest_position"),
                                        geo_location
                                ),
                                doc.getDouble("rating")
                        );
                        top5Restaurant.add(rm);
                    }
                    popularRestaurantsAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    public void getDataAndUpdateArrayList() {

        pbRestaurants.setVisibility(View.VISIBLE);
        db.collection("restaurant").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        restaurantsData.clear();
                        QuerySnapshot document = task.getResult();
                        if (!document.isEmpty()) {
                            for(DocumentSnapshot doc : document){
                                Map<String, Boolean> receivedTags;
                                ArrayList<String> tags = null;
                                if(doc.get("tags") != null) {
                                    receivedTags = (Map<String, Boolean>)doc.get("tags");
                                    tags = new ArrayList<>(receivedTags.keySet());
                                }
                                restaurantsData.add(new RestaurantModel(
                                        doc.getId(),
                                        doc.getString("rest_name"),
                                        doc.getDouble("delivery_fee"),
                                        doc.getString("rest_address"),
                                        doc.getString("rest_descr"),
                                        doc.getString("rest_image"),
                                        tags,
                                        Haversine.getHaversineDistance(
                                                doc.getGeoPoint("rest_position"),
                                                geo_location
                                        ),
                                        doc.getDouble("rating")));
                            }
                            Collections.sort(restaurantsData);
                            pbRestaurants.setVisibility(View.GONE);
                            int count = restaurantsAdapter.getItemCount();
                            tvRestaurantsCountValue.setText(String.valueOf(count));
                            restaurantsAdapter.notifyDataSetChanged();
                            popularRestaurantsAdapter.notifyDataSetChanged();

                        } else {
                            Log.d("QueryRestaurants", "No such document");
                        }
                    } else {
                        Log.d("QueryRestaurants", "get failed with ", task.getException());
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_home);
        getDataAndUpdateArrayList();
        if (auth.getCurrentUser() == null ) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();

        }

    }
    //sign out method
    public void signOut() {
        auth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();

    }

    private int check_GPS(){
        LocationManager lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Snackbar.make(findViewById(R.id.constraintLayoutRestaurants), "Please active GPS!", Snackbar.LENGTH_LONG).show();
//            Toast.makeText(getContext(), "Please active GPS!", Toast.LENGTH_LONG).show();
            return 1;
        }

        //Check whether this app has access to the location permission//
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        //If the location permission has been granted, then start the TrackerService//
        if (permission == PackageManager.PERMISSION_GRANTED) {
            return 0;
        } else {
            //If the app doesn’t currently have access to the user’s location, then request access//
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
            return 2;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //If the permission has been granted...//
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



        } else {
            //If the user denies the permission request, then display a snackBar with some more information//
            Snackbar.make(findViewById(R.id.constraintLayoutRestaurants), "Please enable location services to allow GPS tracking",
                    Snackbar.LENGTH_LONG).show();
        }
    }
}