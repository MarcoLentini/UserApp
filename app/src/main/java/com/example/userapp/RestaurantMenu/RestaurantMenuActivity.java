package com.example.userapp.RestaurantMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
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
import com.example.userapp.Restaurant.RestaurantModel;
import com.example.userapp.ShoppingCart.OrderItemModel;
import com.example.userapp.ShoppingCart.ShoppingCartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantMenuActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;
    private View mFab;
    public static ArrayList<HeaderOrMenuItem> restaurantMenuData;
    private RecyclerView.Adapter restaurantMenuListAdapter;
    private FirebaseFirestore db;
    private RestaurantModel rm;


    //for shopping cart   key->itemId value->OrderItemModel
    private HashMap<String,OrderItemModel> selectedItemsHashMap;
    private double totalMoney = 0.00;
    // BottomSheetBehavior variable
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView textViewTotalMoney;
    private TextView Basket;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
        }
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

        getDataAndUpdateArrayList();
        restaurantMenuData = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRestaurantMenu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        restaurantMenuListAdapter = new RestaurantMenuListAdapter(this, restaurantMenuData);
        recyclerView.setAdapter(restaurantMenuListAdapter);


        //for shopping cart
        selectedItemsHashMap = new HashMap<>();
        textViewTotalMoney = findViewById(R.id.tv_bottom_shop_cart_total_money);
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_shop_cart));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        Basket = findViewById(R.id.tv_pay_for_order);
        Basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantMenuActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
            }
        });
     }

    //following code for shoppingCart
    //get the current count with the given name of a dish
    public int getSelectedItemCountById(String name){
        if (selectedItemsHashMap.size() < 1){
            return 0;
        }else {
            if (!selectedItemsHashMap.containsKey(name)) {
                return 0;
            } else {
                OrderItemModel temp = selectedItemsHashMap.get(name);
                return temp.getCount();
            }
        }
    }

        public void handlerShoppingCarNum(int type, RestaurantMenuItemModel menuItem, boolean refreshGoodList){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            if (type == 0) {//reduce the count of one menuItem
            OrderItemModel temp = selectedItemsHashMap.get(menuItem.getName());
            if(temp!=null){
                if(temp.getCount()<2){
                    temp.setCount(0);
                    selectedItemsHashMap.remove(menuItem.getName());

                }else{
                    int i =  temp.getCount();
                    temp.setCount(--i);
                }
            }
        } else if (type == 1) { //increase the count of one menuItem
            if (selectedItemsHashMap.size() < 1){
                OrderItemModel temp = new OrderItemModel(menuItem.getName(),menuItem.getPrice(),1);
                selectedItemsHashMap.put(temp.getName(), temp);
            }else{
                if (!selectedItemsHashMap.containsKey(menuItem.getName())){
                    OrderItemModel temp = new OrderItemModel(menuItem.getName(),menuItem.getPrice(),1);
                    selectedItemsHashMap.put(temp.getName(), temp);
                }else {
                    OrderItemModel temp = selectedItemsHashMap.get(menuItem.getName());
                    int i = temp.getCount();
                    temp.setCount(++i);
                }
            }
        }
        update(refreshGoodList);
    }

        private void update(boolean refreshGoodList){
            if (selectedItemsHashMap.size() >0) { //user select some items
                for (OrderItemModel orderItem : selectedItemsHashMap.values()) {
                    totalMoney += orderItem.getCount()*orderItem.getPrice();
                }
            }else{
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
            //set the total money in the bottom shopping cart
            textViewTotalMoney.setText("€"+String.valueOf(totalMoney));
            totalMoney = 0.00;

            if(restaurantMenuListAdapter!=null){
                restaurantMenuListAdapter.notifyDataSetChanged();
            }

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

    protected void onResume() {
        super.onResume();
        if (auth.getCurrentUser() == null) {
            getDataAndUpdateArrayList();

            finish();

        }
    }
}








