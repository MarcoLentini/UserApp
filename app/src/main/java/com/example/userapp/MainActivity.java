package com.example.userapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.userapp.home.MyRestaurants;
import com.example.userapp.information.LoginActivity;
import com.example.userapp.home.Restaurant;
import com.example.userapp.information.UserInformationActivity;

//import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.HashMap;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    public static ArrayList<Restaurant> restaurantsData;
    /*for adding firebase */
    //private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //adding toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //adding drawerLayout and  navigationView
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Insert the fragment by replacing any existing fragment
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainFragment, fragment).commit();

        //initialize the date of restaurants
        restaurantsData = new ArrayList<>();
        // fillWithStaticData() is used to put data into the previous ArrayLists and the HashMap
        fillWithStaticData();

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
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu,this adds items to the action bar if it is present
      getMenuInflater().inflate(R.menu.main,menu);
      return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this,"Action setting",Toast.LENGTH_SHORT).show();
            /*
            Intent information = new Intent(this, UserInformationActivity.class);
            startActivity(information);*/
        }
        return super.onOptionsItemSelected(item);
    }

    /*implement actions for NavigationItemSelect*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //handle navigation view item clicks here
        //TODO : adding the corresponding action for each item click
        int id = menuItem.getItemId();
        if (id == R.id.nav_account){
            //go to the login activity
            Intent intent = new Intent(this, UserInformationActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Account",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_setting){
            Toast.makeText(this,"Setting",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_help){
            Toast.makeText(this,"Help",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_orders){
            Toast.makeText(this,"Orders",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_star){
            Toast.makeText(this,"Star",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_logout){
            Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_home){
            // Insert the fragment by replacing any existing fragment
            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFragment, fragment).commit();
         }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void fillWithStaticData(){
        for(int i = 0; i< MyRestaurants.restaurants.length; i++)
        {
            restaurantsData.add(new Restaurant(MyRestaurants.restaurants[i]));
        }

    }
}