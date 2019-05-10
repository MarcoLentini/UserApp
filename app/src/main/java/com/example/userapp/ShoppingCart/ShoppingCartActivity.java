package com.example.userapp.ShoppingCart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.example.userapp.ShoppingCart.ReservationModel;
import com.example.userapp.Restaurant.RestaurantModel;
import com.example.userapp.RestaurantMenu.RestaurantMenuActivity;
import com.example.userapp.RestaurantMenu.RestaurantMenuListAdapter;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingCartActivity extends AppCompatActivity {

    private FirebaseFirestore db ;
    private final static String TAG = "ShoppingCartActivity";
    private static final int ADD_ADDRESS_ACTIVITY = 1;
    private static final int ADD_NOTES_ACTIVITY = 2;
    private RecyclerView.Adapter OrderItemListAdapter;
    //for shopping cart   key->itemId value->OrderItemModel
    private ArrayList<OrderItemModel> orderItems;
    private RestaurantModel rm;
    private double totalMoney = 0.00;
    private TextView textViewTotalMoney;
    private  TextView tvDeliveryAddress;
    private  TextView tvNotes;
    private  TextView tvRestaurantName;
    private Button btnPayForOrder;
    private  TextView textViewtotalCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_info);


        String title = getString(R.string.order_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
//Get Firebase auth instance
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
        }

        //get Firestore instance
        db = FirebaseFirestore.getInstance();

        //static data for orderItems
        orderItems = new ArrayList<>();
       // initStaticData();
        orderItems =  (ArrayList<OrderItemModel>) getIntent().getSerializableExtra("selectedItems");
        Intent receivedIntent = getIntent();
        rm = (RestaurantModel) receivedIntent.getExtras().getSerializable("rest");

        //this recycleView for the list of ordered items

        RecyclerView recyclerView = findViewById(R.id.rcOrderItemInfo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        OrderItemListAdapter = new OrderItemListAdapter(this,orderItems);
        recyclerView.setAdapter(OrderItemListAdapter);

        textViewTotalMoney = findViewById(R.id.tv_order_total_cost);

        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvRestaurantName.setText(rm.getName());
        tvNotes = findViewById(R.id.orderDetailInfoNotes);
        tvNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Add notes",Toast.LENGTH_SHORT).show();
                //Invoke Address Activity
                invokeAddNotesActivity(tvNotes.getText().toString());
            }
        });
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        tvDeliveryAddress.setOnClickListener(v -> {
          Toast.makeText(v.getContext(),"Add address",Toast.LENGTH_SHORT).show();
          //Invoke Address Activity
            invokeAddressActivity(tvDeliveryAddress.getText().toString());
        });
        textViewtotalCount = findViewById(R.id.tv_order_total_count);
        Switch switchASAP = findViewById(R.id.switch1);
        switchASAP.setChecked(false);
        switchASAP.setOnClickListener(v -> {
            Toast.makeText(v.getContext(),"You choose ASAP ",Toast.LENGTH_SHORT).show();
            switchASAP.setChecked(true);
        });

        btnPayForOrder = findViewById(R.id.btnPayForOrder);

        btnPayForOrder.setOnClickListener(v -> {
            if(isValidOrder()){
                String deliveryTime = "No mention";
                if (switchASAP.isChecked()){
                    deliveryTime = "AS SOON AS POSSIBLE";
                }
                String  totalCount = textViewtotalCount.getText().toString();
                int orderItemsCount = Integer.parseInt(totalCount);
                String totalMoney = textViewTotalMoney.getText().toString();
                Double orderTotalCost = Double.parseDouble(totalMoney.replace("€", " "));
                String orderDeliveryAddress = tvDeliveryAddress.getText().toString();

                ReservationModel reservationModel = new ReservationModel(
                        auth.getCurrentUser().getUid(),
                        // Todo - timestamp and notes
                        Timestamp.now(), // delivery time
                        null, //notes,
                        orderItems,
                        orderTotalCost,
                        rm.getId(), rm.getName(), rm.getAddress());

                reservationModel.setCust_address(orderDeliveryAddress);

                db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(task->{
                   if(task.isSuccessful()){
                       DocumentSnapshot doc = task.getResult();
                       if(doc != null){
                           reservationModel.setCust_name((String) doc.get("username"));
                           reservationModel.setCust_phone((String) doc.get("phone"));

                           db.collection("reservations").document().set(reservationModel).addOnCompleteListener(task1 -> {
                               if(task1.isSuccessful()){

                                   Toast.makeText(this,"Your order is sending to restaurant !", Toast.LENGTH_LONG).show();
                               } else {
                                   // Probably only on timeout, from test the request are stored offline
                                   Toast.makeText(this,"Internet problem, retry!", Toast.LENGTH_LONG).show();
                               }
                           });
                       }
                   }
                });
            }
        });

        update(true);
    }

    public boolean isValidOrder(){
        String  totalCount = textViewtotalCount.getText().toString();
        int orderItemsCount = Integer.parseInt(totalCount);
        String totalMoney = textViewTotalMoney.getText().toString();
        Double orderCost = Double.parseDouble(totalMoney.replace('€',' '));
        String orderRestaurantName = tvRestaurantName.getText().toString();
        String orderDeliveryAddress = tvDeliveryAddress.getText().toString();
        if(orderCost == 0 && orderItemsCount == 0){
            Toast.makeText(this,"Please check you basket ,it it empty !",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(orderRestaurantName.isEmpty()) {
            Toast.makeText(this,"Can not find this restaurant !",Toast.LENGTH_SHORT).show();
            return false;
        }  ;
        if(orderDeliveryAddress.isEmpty() || orderDeliveryAddress.equals(getString(R.string.str_nnt_please_addding_your_delivery_address_here))) {
            Toast.makeText(this,"Please input your address !",Toast.LENGTH_SHORT).show();
            tvDeliveryAddress.setError("Field can't be empty");
            return false;
        } else
            tvDeliveryAddress.setError(null);

        return true;
    }
    //activity for adding  the notes
    public void invokeAddNotesActivity(String notes){
        Intent intent = new Intent(getApplicationContext(), AddingNotesActivity.class);
        Bundle bundle = new Bundle();
        if(notes.equals(getString(R.string.str_please_leave_your_notes_here_if_needed))){
            notes = "";
        }else{
            notes = tvNotes.getText().toString();
        }
        bundle.putString("orderNotes", notes);
        intent.putExtras(bundle);
        startActivityForResult(intent, ADD_NOTES_ACTIVITY);
    }


    //activity for adding  the address
    public void invokeAddressActivity(String address){
        Intent intent = new Intent(getApplicationContext(), AddingAddressActivity.class);
        Bundle bundle = new Bundle();
        if(address.equals(getString(R.string.str_nnt_please_addding_your_delivery_address_here))){
            address = "";
        }else{
            address = tvDeliveryAddress.getText().toString();
        }

        bundle.putString("address", address);
        intent.putExtras(bundle);
        startActivityForResult(intent, ADD_ADDRESS_ACTIVITY);
     }

    //dealing with the increase or decrease of the item count
    public void handlerShoppingCarNum(int type, int position,boolean refreshGoodList) {
        if (type == 0) { //decrease the count
            OrderItemModel tmpItem = orderItems.get(position);
            if (tmpItem != null){
                if(tmpItem.getCount() < 2 ){
                    //when the count was 0 remove item from the list
                    tmpItem.setCount(0);
                    orderItems.remove(position);
                }else{
                    int i = tmpItem.getCount();
                    tmpItem.setCount(--i);
                }
            }
        }else if (type == 1) { //increase the count
            OrderItemModel tmpItem = orderItems.get(position);
            int i= tmpItem.getCount();
            tmpItem.setCount(++i);
            }
        //update the list of already ordered items
        update(refreshGoodList);
    }

    //refresh the layout total payment ,quantity and so on
    private void update(boolean refreshGoodList){
        int size = orderItems.size();
        int count =0;
        for(int i=0;i<size;i++){
            OrderItemModel item = orderItems.get(i);
            count += item.getCount();
            totalMoney += item.getCount()*item.getPrice();
        }
        textViewTotalMoney.setText("€"+String.valueOf(totalMoney));
        totalMoney = 0.00;


        textViewtotalCount.setText(String.valueOf(count));

        if(OrderItemListAdapter!=null){
            OrderItemListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == ADD_ADDRESS_ACTIVITY) {
                String myAddress = data.getStringExtra("address");
                if (!myAddress.equals("")) {
                    tvDeliveryAddress.setText(myAddress);
                }
            }else if (requestCode == ADD_NOTES_ACTIVITY){
                String myNotes = data.getStringExtra("orderNotes");
                if (!myNotes.equals("")) {
                    tvNotes.setText(myNotes);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShoppingCartActivity.this, RestaurantMenuActivity.class);
        intent.putExtra("refselectedItems",(Serializable)orderItems);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
