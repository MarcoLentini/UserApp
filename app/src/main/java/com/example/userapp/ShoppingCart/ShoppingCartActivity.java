package com.example.userapp.ShoppingCart;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.userapp.OrdersActivity;
import com.example.userapp.R;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.example.userapp.Restaurant.RestaurantModel;
import com.example.userapp.RestaurantMenu.RestaurantMenuActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    private  TextView tvRestaurantName,tvDeliveryNotes;
    private Button btnPayForOrder;
    private  TextView textViewtotalCount;
    private AddressModel myAddress;
    private LinearLayout deliveryInfo;
    TimePickerDialog.OnTimeSetListener mOnTimeSetListener;
    private String deliverytime = "AS SOON AS POSSIBLE";
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
        tvNotes.setOnClickListener(v -> {
            Toast.makeText(v.getContext(),"Add notes",Toast.LENGTH_SHORT).show();
            //Invoke Address Activity
            invokeAddNotesActivity(tvNotes.getText().toString());
        });

        deliveryInfo=findViewById(R.id.deliveryInformation);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        tvDeliveryNotes=findViewById(R.id.tvDeliveryNotes);
        deliveryInfo.setOnClickListener(v -> {
             invokeAddressActivity(tvDeliveryAddress.getText().toString(),tvDeliveryNotes.getText().toString());
        });
        textViewtotalCount = findViewById(R.id.tv_order_total_count);
        Switch switchASAP = findViewById(R.id.switch1);
        //default status of switchASAP is true
        switchASAP.setChecked(true);
        switchASAP.setOnClickListener(v -> {
            Toast.makeText(v.getContext(),"You change the delivery time ",Toast.LENGTH_SHORT).show();
            // check current state of a Switch (true or false).
            Boolean switchState = switchASAP.isChecked();
            if (!switchState){
                //when status changes into false then all user choose the time
                Calendar mCalendar =  Calendar.getInstance();
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = mCalendar.get(Calendar.MINUTE);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = mCalendar.getTime();

                TimePickerDialog mTimePickerDialog = new TimePickerDialog(
                        ShoppingCartActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mOnTimeSetListener,
                        hour,minute,true);

                mTimePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mTimePickerDialog.setTitle(dateFormat.format(date));
                mTimePickerDialog.show();

            }else {
                switchASAP.setChecked(true);

            }
        });

        mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourofday, int minute) {
               //the time choosed
                deliverytime = hourofday+":"+minute;
            }
        };

        btnPayForOrder = findViewById(R.id.btnPayForOrder);

        btnPayForOrder.setOnClickListener(v -> {
            if(isValidOrder()){
                if (switchASAP.isChecked()){
                    deliverytime = "AS SOON AS POSSIBLE";
                }
                String  totalCount = textViewtotalCount.getText().toString();
                int orderItemsCount = Integer.parseInt(totalCount);
                String totalMoney = textViewTotalMoney.getText().toString();
                Double orderTotalCost = Double.parseDouble(totalMoney.replace("€", " "));
                String orderDeliveryAddress = tvDeliveryAddress.getText().toString();
                String orderNotes = tvNotes.getText().toString().replace(getString(R.string.str_please_leave_your_notes_here_if_needed),"");

                ReservationModel reservationModel = new ReservationModel(
                        auth.getCurrentUser().getUid(),
                        Timestamp.now(), // delivery time
                        orderNotes, //notes,
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

                                   //TODO : Task finished successful them go the the current order page show the order information
                                   Toast.makeText(this,"Your order is sending to restaurant !", Toast.LENGTH_LONG).show();

                                   Intent intent = new Intent(ShoppingCartActivity.this, OrdersActivity.class);

                                   startActivity(intent);
                               } else {
                                   // Probably only on timeout, from test the request are stored offline
                                   Toast.makeText(this,"Internet problem, retry!", Toast.LENGTH_LONG).show();
                               }
                           });
                       }
                   }
                });
                finish();
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
        String orderNotes = tvNotes.getText().toString();

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
    public void invokeAddressActivity(String address,String notes){
        Intent intent = new Intent(getApplicationContext(), AddingAddressActivity.class);
        Bundle bundle = new Bundle();
        AddressModel sentAddr;
        if(address.equals(getString(R.string.str_nnt_please_addding_your_delivery_address_here))){
            sentAddr = new AddressModel("","",0);
        }else{
            sentAddr = myAddress;
        }
        if(notes.equals(getString(R.string.str_nnt_please_addding_your_delivery_notes_here))){
            notes = "";
        }else{
            notes = tvDeliveryNotes.getText().toString();
        }


        bundle.putSerializable("address", sentAddr);
        bundle.putString("notes",notes);

        intent.putExtras(bundle);
        startActivityForResult(intent, ADD_ADDRESS_ACTIVITY);
     }

    //dealing with the increase or decrease of the item count
    public void handlerShoppingCarNum(int type, int position,boolean refreshGoodList) {
        if (type == 0) { //decrease the count
            OrderItemModel tmpItem = orderItems.get(position);
            if (tmpItem != null){
                if(tmpItem.getDish_qty() < 2 ){
                    //when the count was 0 remove item from the list
                    tmpItem.setDish_qty(0);
                    orderItems.remove(position);
                }else{
                    int i = tmpItem.getDish_qty();
                    tmpItem.setDish_qty(--i);
                }
            }
        }else if (type == 1) { //increase the count
            OrderItemModel tmpItem = orderItems.get(position);
            int i= tmpItem.getDish_qty();
            tmpItem.setDish_qty(++i);
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
            count += item.getDish_qty();
            totalMoney += item.getDish_qty()*item.getDish_price();
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

                Bundle bn= data.getExtras();
                myAddress = (AddressModel)bn.getSerializable("address");
                String notes= bn.getString("notes");
                tvDeliveryAddress.setText(myAddress.toString());
                tvDeliveryNotes.setText(notes);

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
