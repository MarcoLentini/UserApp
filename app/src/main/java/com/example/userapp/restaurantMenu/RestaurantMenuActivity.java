package com.example.userapp.restaurantMenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.userapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RestaurantMenuActivity extends AppCompatActivity  {


    public static ArrayList<HeaderOrMenuItem> restaurantMenuData;
    private RecyclerView.Adapter restaurantMenuListAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

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


    private void getDataAndUpdateArrayList() {

        db.collection("category").whereEqualTo("rest_id", "U6RltH7ED7bylM7TwyXa").get()
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








