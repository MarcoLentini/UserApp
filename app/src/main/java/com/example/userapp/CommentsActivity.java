package com.example.userapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;


import com.example.userapp.Information.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

//TODO : LAB5 get list of comments and shows here
public class CommentsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        /*
        RecyclerView recyclerView = findViewById(R.id.rvMyComments);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        RecyclerView.Adapter commentsListAdapter = new CommentsListAdapter(this, myCommentsData);
        recyclerView.setAdapter(commentsListAdapter);
        */

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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home){
            Intent intent = new Intent(CommentsActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Main",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_setting){
            Intent intent = new Intent(CommentsActivity.this, SettingsActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Setting",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_help){
            Intent intent = new Intent(CommentsActivity.this, HelpActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Help",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_orders){
            Intent intent = new Intent(CommentsActivity.this, OrdersActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Orders",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_star){
            Intent intent = new Intent(CommentsActivity.this, FavoritesActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Star",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_logout){
            signOut();
        }else if (id == R.id.nav_comments){

            Toast.makeText(this,"Comments",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_history_order){
            Toast.makeText(this,"History Orders",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CommentsActivity.this, HistoryOrderActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    //sign out method
    public void signOut() {
        auth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();

    }

}
