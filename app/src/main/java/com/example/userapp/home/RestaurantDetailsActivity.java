package com.example.userapp.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;


import com.example.userapp.R;
public class RestaurantDetailsActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{
    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;
    private View mFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
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
        mFab= findViewById(R.id.fabRestaurantDetails);
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
        c.startActivity(new Intent(c, RestaurantDetailsActivity.class));
    }
}








