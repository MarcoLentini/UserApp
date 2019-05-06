package com.example.userapp.restaurant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.example.userapp.R;
import com.example.userapp.restaurantMenu.HeaderOrMenuItem;
import com.example.userapp.restaurantMenu.RestaurantMenuHeaderModel;
import com.example.userapp.restaurantMenu.RestaurantMenuItemModel;
import com.example.userapp.restaurantMenu.RestaurantMenuListAdapter;

import java.util.ArrayList;

public class RestaurantMenuActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;
    private View mFab;
    public static ArrayList<HeaderOrMenuItem> restaurantMenuData;
    private RecyclerView.Adapter restaurantMenuListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
        //toolbar
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbarRestaurantDetails);
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.appbarRestaurantDetails);
        appbarLayout.addOnOffsetChangedListener(this);
        mFab = findViewById(R.id.fabRestaurantDetails);

        // TODO put data in restaurantMenuData
        restaurantMenuData = new ArrayList<>();
        //getDataAndUpdateArrayList();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRestaurantMenu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        restaurantMenuListAdapter = new RestaurantMenuListAdapter(this, restaurantMenuData);
        recyclerView.setAdapter(restaurantMenuListAdapter);
     }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(i)) * 100
                / mMaxScrollSize;

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true;
                ViewCompat.animate(mFab).scaleY(0).scaleX(0).start();
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;
                ViewCompat.animate(mFab).scaleY(1).scaleX(1).start();
            }
        }

    }
    public static void start(Context c) {
        c.startActivity(new Intent(c, RestaurantMenuActivity.class));
    }

    /*private void getDataAndUpdateArrayList() {
        RestaurantMenuHeaderModel h1m = new RestaurantMenuHeaderModel("Category1");
        HeaderOrMenuItem h1 = HeaderOrMenuItem.onCreateHeader(h1m);
        RestaurantMenuHeaderModel h2m = new RestaurantMenuHeaderModel("Category2");
        HeaderOrMenuItem h2 = HeaderOrMenuItem.onCreateHeader(h2m);
        RestaurantMenuHeaderModel h3m = new RestaurantMenuHeaderModel("Category3");
        HeaderOrMenuItem h3 = HeaderOrMenuItem.onCreateHeader(h3m);
        RestaurantMenuItemModel m1m = new RestaurantMenuItemModel("Pizza1", 3, "Buona", MyRestaurantsData.restaurantLogo[0]);
        RestaurantMenuItemModel m2m = new RestaurantMenuItemModel("Pizza2", 4, "Buona", MyRestaurantsData.restaurantLogo[0]);
        RestaurantMenuItemModel m3m = new RestaurantMenuItemModel("Pizza3", 5, "Buona", MyRestaurantsData.restaurantLogo[0]);
        RestaurantMenuItemModel m4m = new RestaurantMenuItemModel("Pizza4", 6, "Buona", MyRestaurantsData.restaurantLogo[0]);
        RestaurantMenuItemModel m5m = new RestaurantMenuItemModel("Pizza5", 7, "Buona", MyRestaurantsData.restaurantLogo[0]);
        RestaurantMenuItemModel m6m = new RestaurantMenuItemModel("Pizza6", 8, "Buona", MyRestaurantsData.restaurantLogo[0]);
        HeaderOrMenuItem m1 = HeaderOrMenuItem.onCreateMenuItem(m1m);
        HeaderOrMenuItem m2 = HeaderOrMenuItem.onCreateMenuItem(m2m);
        HeaderOrMenuItem m3 = HeaderOrMenuItem.onCreateMenuItem(m3m);
        HeaderOrMenuItem m4 = HeaderOrMenuItem.onCreateMenuItem(m4m);
        HeaderOrMenuItem m5 = HeaderOrMenuItem.onCreateMenuItem(m5m);
        HeaderOrMenuItem m6 = HeaderOrMenuItem.onCreateMenuItem(m6m);
        restaurantMenuData.add(h1);     restaurantMenuData.add(m1);      restaurantMenuData.add(m2);
        restaurantMenuData.add(h2);     restaurantMenuData.add(m3);      restaurantMenuData.add(m4);
        restaurantMenuData.add(h3);     restaurantMenuData.add(m5);      restaurantMenuData.add(m6);
    }*/
}








