package com.example.userapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.userapp.Information.LoginActivity;
import com.example.userapp.Restaurant.FilterRestaurantsActivity;
import com.example.userapp.Restaurant.RestaurantModel;
import com.example.userapp.Restaurant.RestaurantsListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int FILTER_RESTAURANTS_ACTIVITY = 1;
    private static final String TAG = "MainActivity";
    public static ArrayList<RestaurantModel> restaurantsData;
    private RecyclerView.Adapter restaurantsAdapter;
    private FirebaseFirestore db;
    private ProgressBar pbRestaurants;
    private FirebaseAuth auth;
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

        //adding toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //adding drawerLayout and  navigationView
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        pbRestaurants = findViewById(R.id.progress_bar_restaurants);

        restaurantsData = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRestaurants);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        restaurantsAdapter = new RestaurantsListAdapter(this, restaurantsData);
        recyclerView.setAdapter(restaurantsAdapter);
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
        //TODO : adding the corresponding action for each item click
        int id = menuItem.getItemId();
        /*if (id == R.id.nav_account){
            //go to the login activity
            Intent intent = new Intent(this, UserInformationActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Account",Toast.LENGTH_SHORT).show();
        }else
         */
        if (id == R.id.nav_setting){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Setting",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_help){
            Toast.makeText(this,"Help",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_orders){
            Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Orders",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_star){
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Favorite",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_logout){
            Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_home){

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
                // TODO apply filters on recyclerview
                ArrayList<String> receivedFilters = data.getStringArrayListExtra("selectedFilters");
                if(receivedFilters != null) {
                    for(String filterString : receivedFilters)
                        ((RestaurantsListAdapter)restaurantsAdapter).getFilter().filter(filterString);
                }
            }
        }
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
                                restaurantsData.add(new RestaurantModel(
                                        doc.getId(),
                                        (String) doc.get("rest_name"),
                                        (Double) doc.get("delivery_fee"),
                                        (String) doc.get("rest_address"),
                                        (String) doc.get("rest_descr"),
                                        (String) doc.get("rest_image")));
                            }
                            pbRestaurants.setVisibility(View.GONE);
                            restaurantsAdapter.notifyDataSetChanged();
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
        getDataAndUpdateArrayList();
        if (auth.getCurrentUser() == null ) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();

        }

    }
}