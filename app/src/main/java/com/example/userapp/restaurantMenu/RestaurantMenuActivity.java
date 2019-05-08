package com.example.userapp.restaurantMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.userapp.R;
import com.example.userapp.restaurant.RestaurantModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RestaurantMenuActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;
    private View mFab;
    public static ArrayList<HeaderOrMenuItem> restaurantMenuData;
    private RecyclerView.Adapter restaurantMenuListAdapter;
    private FirebaseFirestore db;
    private RestaurantModel rm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        Intent receivedIntent = getIntent();
        rm = (RestaurantModel)receivedIntent.getExtras().getSerializable("rest");
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolBarRestaurantDetails);
        collapsingToolbarLayout.setTitle(rm.getName());
        ImageView restaurantImageView = findViewById(R.id.tvRestaurantLogo);
        Uri tmpUri = Uri.parse(rm.getRestaurantLogo());
        Glide.with(this).load(tmpUri).placeholder(R.drawable.img_rest_1).into(restaurantImageView);
        TextView tvDeliveryFee = findViewById(R.id.tvDeliveryFeeRestaurant);
        tvDeliveryFee.setText(String.valueOf(rm.getDeliveryFee()));
        TextView tvDistance = findViewById(R.id.tvDistanceRestaurant);
        tvDistance.setText(rm.getAddress()); // TODO change with dinstance in lab4

        //Get Firestore instance
        db = FirebaseFirestore.getInstance();
        //toolbar
        Toolbar toolbar1 = findViewById(R.id.toolbarRestaurantDetails);
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        AppBarLayout appbarLayout = findViewById(R.id.appbarRestaurantDetails);
        appbarLayout.addOnOffsetChangedListener(this);
        mFab = findViewById(R.id.fabRestaurantDetails);

        // TODO put data in restaurantMenuData
        restaurantMenuData = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRestaurantMenu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        restaurantMenuListAdapter = new RestaurantMenuListAdapter(this, restaurantMenuData);
        recyclerView.setAdapter(restaurantMenuListAdapter);
     }

    @Override
    protected void onResume() {
        super.onResume();
        getDataAndUpdateArrayList();
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

    private void getDataAndUpdateArrayList() {

        db.collection("category").whereEqualTo("rest_id", rm.getId()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();
                        if (!document.isEmpty()) {
                            for(DocumentSnapshot doc : document) {

                                HeaderOrMenuItem tmpHeader = HeaderOrMenuItem.onCreateHeader(
                                                                new RestaurantMenuHeaderModel(
                                                            (String) doc.getId(),
                                                            (String) doc.get("category_name")));


                                doc.getReference().collection("dishes").whereEqualTo("state", true).get()
                                        .addOnCompleteListener(task1 -> {

                                            if (task1.isSuccessful()) {
                                                QuerySnapshot document1 = task1.getResult();
                                                if (!document1.isEmpty()) {
                                                    restaurantMenuData.add(tmpHeader);
                                                    for(DocumentSnapshot doc1 : document1) {
                                                        HeaderOrMenuItem tmpMenuItem = HeaderOrMenuItem.onCreateMenuItem(
                                                                new RestaurantMenuItemModel(
                                                                        doc.getId(), (String) doc.get("category_name"),
                                                                        doc1.getId(),
                                                                        (String) doc1.get("name"), (String) doc1.get("description"),
                                                                        (Double) doc1.get("price"), (String) doc1.get("image")));
                                                        restaurantMenuData.add(tmpMenuItem);
                                                    }

                                                    restaurantMenuListAdapter.notifyDataSetChanged();
                                                } else {
                                                    Log.d("QueryRestaurantMenu", "No such document");
                                                }
                                            } else {
                                                Log.d("QueryRestaurantMenu", "get failed with ", task.getException());
                                            }
                                        });
                            }
                        } else {
                            Log.d("QueryRestaurantMenu", "No such document");
                        }
                    } else {
                        Log.d("QueryRestaurantMenu", "get failed with ", task.getException());
                    }
                });
    }
}








