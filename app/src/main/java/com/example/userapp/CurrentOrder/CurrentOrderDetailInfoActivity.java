package com.example.userapp.CurrentOrder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userapp.MainActivity;
import com.example.userapp.OrdersActivity;
import com.example.userapp.R;
import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CurrentOrderDetailInfoActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_order_detail_info);

        String title = getString(R.string.current_order_details_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        //get data from the OrdersActivity
        Intent receivedIntent = getIntent();
        Integer itemPosition = receivedIntent.getExtras().getInt("currentOrderData");
        System.out.println("Received positiom"+itemPosition);
        CurrentOrderModel rm = OrdersActivity.currentOrders.get(itemPosition);
        System.out.println("Received currentOrders size "+ OrdersActivity.currentOrders.size() );


            TextView textViewStatus = findViewById(R.id.tvStatusCurrentOrder);
            textViewStatus.setText(rm.getRs_status());
            TextView textViewRestName = findViewById(R.id.tvRestaurantNameCurrentOrderDetailInfo);
            textViewRestName.setText(rm.getRest_name());

            TextView textViewTotalCost = findViewById(R.id.tvCurrentOrderTotalCostDetailInfo);
             DecimalFormat format = new DecimalFormat("0.00");
             String formattedPrice = format.format(rm.getTotal_income());
             textViewTotalCost.setText(formattedPrice+" €");


            TextView textViewAddress = findViewById(R.id.tvCurrentOrderDeliveryAddress);
            textViewAddress.setText(rm.getCust_address());
            TextView textViewTime = findViewById(R.id.tvCurrentOrderDeliveryTime);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            Date date=rm.getDelivery_time();
            textViewTime.setText( dateFormat.format(date));



            TextView textViewConfirmationCode = findViewById(R.id.tvConfirmationCodeDetail);
            textViewConfirmationCode.setText(String.valueOf(rm.getConfirmation_code()));
            TextView textViewOrderId = findViewById(R.id.tvCurrentOrderID);
            textViewOrderId.setText(String.valueOf(rm.getRs_id()));

            LinearLayout current_order_detail_info = findViewById(R.id.llCurrentOrderDetailInfo);
            for (CurrentOrderItemModel rd : rm.getDishes()) {
                LinearLayout ll = new LinearLayout(this);
                // 16dp
                float scale = getResources().getDisplayMetrics().density;
                int _4dp = (int) (4 * scale + 0.5f);

                ll.setPadding(_4dp, 0, _4dp, 0);
                ll.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams params_ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                ll.setLayoutParams(params_ll);
                // Name of food
                TextView tv = new TextView(this);
                tv.setText("▶" + rd.getDish_name());

                tv.setTextColor(Color.parseColor("#FF000000"));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                LinearLayout.LayoutParams params_name = new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 2f);
                tv.setLayoutParams(params_name);

                // Quantity
                TextView tv1 = new TextView(this);
                tv1.setText("x" + rd.getDish_qty());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tv1.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                }
                tv1.setTextColor(Color.parseColor("#FF000000"));
                tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                LinearLayout.LayoutParams params_qty = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                params_qty.gravity = Gravity.END;

                tv1.setLayoutParams(params_qty);

                // Single price
                TextView tv2 = new TextView(this);
                tv2.setText(String.format("%.2f", rd.getDish_price()) + "€");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tv2.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                }
                tv2.setTextColor(Color.parseColor("#FF000000"));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                LinearLayout.LayoutParams params_price = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                params_price.gravity = Gravity.END;

                tv2.setLayoutParams(params_price);

                ll.addView(tv);
                ll.addView(tv1);
                ll.addView(tv2);
                current_order_detail_info.addView(ll);
            }

            TextView textViewCurrentOrderCreateTime = findViewById(R.id.tvCurrentOrderCreateTime);
            date=rm.getTimestamp().toDate();
            textViewCurrentOrderCreateTime.setText( dateFormat.format(date));


    }

    private void returnVal(int itemPosition, String ret){
        Intent retIntent = new Intent();
        Bundle bn = new Bundle();
        bn.putInt("pos", itemPosition);
        bn.putString("result", ret);
        retIntent.putExtras(bn);
        setResult(RESULT_OK, retIntent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

