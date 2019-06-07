package com.example.userapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.userapp.Favorites.FavoriteRestaurantListAdapter;
import com.example.userapp.Information.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class FavoritesActivity extends AppCompatActivity
        {

    private RecyclerView.Adapter restaurantsAdapter;
    private RecyclerView recyclerView;

    private ProgressBar pbFavRestaurants;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
        }

        //Get Firestore instance
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getString(R.string.menu_star);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        pbFavRestaurants = findViewById(R.id.progress_bar_fav_restaurants);

        /*list of favorite restaurants*/

        recyclerView = findViewById(R.id.rvFavoriteRestaurants);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        restaurantsAdapter = new FavoriteRestaurantListAdapter(this, MainActivity.favoritesData);
        recyclerView.setAdapter(restaurantsAdapter);


    }

    @Override
    public void onBackPressed() {
          super.onBackPressed();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        restaurantsAdapter.notifyDataSetChanged();
        // getDataAndUpdateArrayList();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(FavoritesActivity.this, LoginActivity.class));
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