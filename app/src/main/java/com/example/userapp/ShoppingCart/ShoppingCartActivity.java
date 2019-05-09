package com.example.userapp.ShoppingCart;

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

import java.util.ArrayList;

public class ShoppingCartActivity extends AppCompatActivity {

    private  final static String TAG = "ShoppingCartActivity";
    private static final int ADD_ADDRESS_ACTIVITY = 1;
    private RecyclerView.Adapter OrderItemListAdapter;
    //for shopping cart   key->itemId value->OrderItemModel
    private ArrayList<OrderItemModel> orderItems;
    private double totalMoney = 0.00;
    private TextView textViewTotalMoney;
    private  TextView tvDeliveryAddress;
    private  TextView tvRestaurantName;
    private Button btnPayForOrder;

    private String address;
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
        //static data for orderItems
        orderItems = new ArrayList<>();
        initStaticData();

        //this recycleView for the list of ordered items

        RecyclerView recyclerView = findViewById(R.id.rcOrderItemInfo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        OrderItemListAdapter = new OrderItemListAdapter(this,orderItems);
        recyclerView.setAdapter(OrderItemListAdapter);

        textViewTotalMoney = findViewById(R.id.tv_order_total_cost);
        //update(true);
        //TODO : set tvRestaurantName with the value get from previous activity
        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        tvDeliveryAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Toast.makeText(v.getContext(),"Add address",Toast.LENGTH_SHORT).show();
              //Invoke Address Activity
                invokeAddressActivity(address);
            }
        });

        Switch switchASAP = findViewById(R.id.switch1);
        //TODO : get switchASAP status
        switchASAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"You choose ASAP ",Toast.LENGTH_SHORT).show();
            }
        });

        btnPayForOrder = findViewById(R.id.btnPayForOrder);
        //TODO : implement specified action for onclickListener data should be send to Firebase
        btnPayForOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"You click btnPayForOrder",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void invokeAddressActivity(String address){
        Intent intent = new Intent(getApplicationContext(), AddingAddressActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivityForResult(intent, ADD_ADDRESS_ACTIVITY);
     }

    private void initStaticData() {
        OrderItemModel item0 = new OrderItemModel("pizza",2.00,5);
        OrderItemModel item1 = new OrderItemModel("Torta",5.00,2);
        OrderItemModel item2 = new OrderItemModel("Acqua",1.00,1);
        OrderItemModel item3 = new OrderItemModel("Panino",3.00,1);
        OrderItemModel item4 = new OrderItemModel("Yogurt",1.00,1);
        orderItems.add(item0);
        orderItems.add(item1);
        orderItems.add(item2);
        orderItems.add(item3);
        orderItems.add(item4);

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

        if(OrderItemListAdapter!=null){
            OrderItemListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == ADD_ADDRESS_ACTIVITY) {
            String myAddress = data.getStringExtra("address");
                 if (!myAddress.equals("")) {
                    tvDeliveryAddress.setText(myAddress);
                 }
         }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
