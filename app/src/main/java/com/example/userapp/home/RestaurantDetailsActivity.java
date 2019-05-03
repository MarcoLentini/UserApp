package com.example.userapp.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.example.userapp.R;
public class RestaurantDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.appbarRestaurantDetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRestaurantDetails);

        //find the view pager that will allow user to swip between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerRestaurantDetails);
       //create an adapter that know which fragment should be shown on each page
        RestaurantDetailsAdapter pageAdapter = new RestaurantDetailsAdapter(this,getSupportFragmentManager());
        //set the adapter onto the view Pager
        viewPager.setAdapter(pageAdapter);
        //give the tabLayout the ViewPager
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabsRestaurantDetails);
        tabLayout.setupWithViewPager(viewPager);

    }

}








