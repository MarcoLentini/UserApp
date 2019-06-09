package com.example.userapp.RestaurantMenu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.userapp.Favorites.FavoritesModel;
import com.example.userapp.FavoritesActivity;
import com.example.userapp.MainActivity;
import com.example.userapp.OrdersActivity;
import com.example.userapp.R;
import com.example.userapp.Restaurant.RestaurantModel;
import com.example.userapp.ShoppingCart.OrderItemModel;
import com.example.userapp.ShoppingCart.ShoppingCartActivity;
import com.firebase.client.DataSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.security.SignatureException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantMenuActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private static final int SHOP_CART_ACTIVITY = 1;

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
    private ArrayList<OrderItemModel> selectedItemsList;
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
        mFab = findViewById(R.id.restaurant_menu_cardView);
        TextView tvDeliveryFee = findViewById(R.id.tvDeliveryFeeRestaurant);
        DecimalFormat format = new DecimalFormat("0.00");
        String formattedPrice = format.format(rm.getDeliveryFee());
        tvDeliveryFee.setText(" " +formattedPrice);
        TextView tvDistance = findViewById(R.id.tvDistanceRestaurant);
        tvDistance.setText(rm.getAddress()); // TODO change with dinstance in lab4

        //Get Firestore instance
        db = FirebaseFirestore.getInstance();
        //toolbar
        Toolbar toolbar1 = findViewById(R.id.toolbarRestaurantDetails);
        //toolbar1.setNavigationOnClickListener(v -> onBackPressed());
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AppBarLayout appbarLayout = findViewById(R.id.appbarRestaurantDetails);
        appbarLayout.addOnOffsetChangedListener(this);

        restaurantMenuData = new ArrayList<>();
        getDataAndUpdateArrayList();
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
        Basket.setOnClickListener(v -> {
            //passing hashmap to new activity
            //fill the Arraylist and pass as parameter to ShoppingCartActivity
            selectedItemsList = new ArrayList<>();
            if(selectedItemsHashMap.size() > 0) {
                for (OrderItemModel orderItem : selectedItemsHashMap.values()) {
                    selectedItemsList.add(orderItem);
                }
            }
            Intent intent = new Intent(RestaurantMenuActivity.this, ShoppingCartActivity.class);
            intent.putExtra("selectedItems", selectedItemsList);
            Bundle bundle = new Bundle();
            bundle.putSerializable("rest", rm);
            intent.putExtras(bundle);
            startActivityForResult(intent, SHOP_CART_ACTIVITY);
        });


     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater =  getMenuInflater();
        menuInflater.inflate(R.menu.menu_restaurant, menu);
        MenuItem like = menu.findItem(R.id.action_like);
        like.setVisible(true);
        String userId = auth.getCurrentUser().getUid();

        if (rm.getLiked()) {
            like.setIcon(R.drawable.ic_liked);
        }else {
            like.setIcon(R.drawable.ic_like);
        }

        like.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // LAB5 send my favorite to firebase
                // first check whether this restaurant is liked by user or not if not then liked else cancel liked
                // get user id and information of the current restaurant
                FavoritesModel myFavorite = new FavoritesModel(userId,rm.getId(),rm);

                if (!rm.getLiked()){//this click means user like this restaurant
                    DocumentReference dr =  db.collection("favorites").document();
                    dr.set(myFavorite).addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            Toast.makeText(RestaurantMenuActivity.this,getString(R.string.added_to_favorite), Toast.LENGTH_LONG).show();
                            item.setIcon(R.drawable.ic_liked);
                            rm.setLiked(true);
                            myFavorite.setId(dr.getId());
                            MainActivity.favoritesData.add(myFavorite);
                        } else {
                            // Probably only on timeout, from test the request are stored offline nothing happened
                            Toast.makeText(RestaurantMenuActivity.this,getString(R.string.internet_down), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{//this click means user cancel like this restaurant
                    //TODO Lab5 delete this favorite form firebase
                    // get the id and delete
                    for (FavoritesModel favoritesModel : MainActivity.favoritesData){
                            if (favoritesModel.getRestaurantID().equals(rm.getId()) && favoritesModel.getUserID().equals(userId)){

                            db.collection("favorites")
                                    .document(favoritesModel.getId())
                                    .delete()
                                    .addOnCompleteListener(task->{
                                if(task.isSuccessful()){
                                    MainActivity.favoritesData.remove(favoritesModel);
                                    Toast.makeText(RestaurantMenuActivity.this,getString(R.string.removed_from_favorite), Toast.LENGTH_LONG).show();
                                    item.setIcon(R.drawable.ic_like);
                                    rm.setLiked(false);
                                }else{
                                    // Probably only on timeout, from test the request are stored offline nothing happened
                                    Toast.makeText(RestaurantMenuActivity.this,getString(R.string.internet_down), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
                return false;
            }
        });

        return true;
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
                return temp.getDish_qty();
            }
        }
    }

        public void handlerShoppingCarNum(int type, RestaurantMenuItemModel menuItem, boolean refreshGoodList){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            if (type == 0) {//reduce the count of one menuItem
            OrderItemModel temp = selectedItemsHashMap.get(menuItem.getCategoryId()+"_"+menuItem.getName());
            if(temp!=null){
                if(temp.getDish_qty()<2){
                    temp.setDish_qty(0);
                    selectedItemsHashMap.remove(menuItem.getCategoryId()+"_"+menuItem.getName());

                }else{
                    int i =  temp.getDish_qty();
                    temp.setDish_qty(--i);
                }
            }
        } else if (type == 1) { //increase the count of one menuItem
            if (selectedItemsHashMap.size() < 1){
                OrderItemModel temp = new OrderItemModel(menuItem.getName(),menuItem.getPrice(),1,menuItem.getCategoryId());
                selectedItemsHashMap.put(temp.getDish_category()+"_"+temp.getDish_name(), temp);
            }else{
                if (!selectedItemsHashMap.containsKey(menuItem.getCategoryId()+"_"+menuItem.getName())){
                    OrderItemModel temp = new OrderItemModel(menuItem.getName(),menuItem.getPrice(),1,menuItem.getCategoryId());
                    selectedItemsHashMap.put(temp.getDish_category()+"_"+temp.getDish_name(), temp);
                }else {
                    OrderItemModel temp = selectedItemsHashMap.get(menuItem.getCategoryId()+"_"+menuItem.getName());
                    int i = temp.getDish_qty();
                    temp.setDish_qty(++i);
                }
            }
        }
        update(refreshGoodList);
    }

    //update the count of related items and the total cost of the all order items
        private void update(boolean refreshGoodList){
            if (selectedItemsHashMap.size() >0) { //user select some items
                for (OrderItemModel orderItem : selectedItemsHashMap.values()) {
                    totalMoney += orderItem.getDish_price()*orderItem.getDish_qty();
                }
            }else{
                //nothing selected then hidden the bottom shopping cart
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
            //set the total money in the bottom shopping cart
            DecimalFormat format = new DecimalFormat("0.00");
            String formattedPrice = format.format(totalMoney);
            textViewTotalMoney.setText("€ " +formattedPrice);
            totalMoney = 0.00;

            if(restaurantMenuListAdapter!=null){
                restaurantMenuListAdapter.notifyDataSetChanged();
            }

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == SHOP_CART_ACTIVITY) {
            selectedItemsList=new ArrayList<>();
            selectedItemsList =  (ArrayList<OrderItemModel>) data.getSerializableExtra("refselectedItems");
            if(selectedItemsList.size() > 0){
                //refresh the shop cart
                selectedItemsHashMap = new HashMap<>();
                for (OrderItemModel orderItem : selectedItemsList){
                   // selectedItemsHashMap.put(orderItem.getDish_name(),orderItem);
                    selectedItemsHashMap.put(orderItem.getDish_category()+"_"+orderItem.getDish_name(),orderItem);
                }
            }else{
                //clear all things in the shopcart
                selectedItemsHashMap = new HashMap<>();
            }
            //update the
            update(true);
        }else if(resultCode==2&& requestCode == SHOP_CART_ACTIVITY ){
            startActivity(new Intent(RestaurantMenuActivity.this, OrdersActivity.class));
            finish();
        }


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (selectedItemsHashMap.size() > 0){
            //show self defined popup window
            Button btnYesLeave;
            Button btnNoStay;
            TextView txtCloseWindow;
            Dialog myDialog = new Dialog(this);
            myDialog.setContentView(R.layout.popup_window_shopping_cart);
            txtCloseWindow = myDialog.findViewById(R.id.tvCloseWindow);
            btnYesLeave = myDialog.findViewById(R.id.btnYesLeave);
            btnNoStay = myDialog.findViewById(R.id.btnNoStay);

            txtCloseWindow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            btnNoStay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });

            btnYesLeave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RestaurantMenuActivity.super.onBackPressed();
                }
            });

            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();

        } else super.onBackPressed();

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
                            List<DocumentSnapshot> docu= document.getDocuments();
                          Collections.sort(docu,CategoryComparator);

                            for(DocumentSnapshot doc : docu) {

                                HeaderOrMenuItem tmpHeader = HeaderOrMenuItem.onCreateHeader(
                                                                new RestaurantMenuHeaderModel(
                                                            (String) doc.getId(),
                                                            (String) doc.get("category_name")
                                                                        ));


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
                                                                        doc1.getDouble("price"), (String) doc1.get("image")));
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

    public static Comparator<DocumentSnapshot> CategoryComparator
            = new Comparator<DocumentSnapshot>() {

        public int compare(DocumentSnapshot cat1, DocumentSnapshot cat2) {

            Long catPosition1 = (Long)cat1.get("category_position");
            Long catPosition2 = (Long)cat2.get("category_position");

            //ascending order
            return catPosition1.compareTo(catPosition2);
            //descending order
            //return fruitName2.compareTo(fruitName1);
        }

    };

}








