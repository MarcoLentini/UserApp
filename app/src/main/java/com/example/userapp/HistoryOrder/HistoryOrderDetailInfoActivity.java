package com.example.userapp.HistoryOrder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.userapp.R;

public class HistoryOrderDetailInfoActivity  extends AppCompatActivity {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_order_detail_info);

        String title = getString(R.string.history_order_details_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
        Intent receivedIntent = getIntent();

        Integer itemPosition = receivedIntent.getExtras().getInt("historyOrderData");


        TextView textViewIdReservation = findViewById(R.id.textViewIdReservation);
        textViewIdReservation.setText(String.valueOf(rm.getRs_id()));
        TextView textViewRemainingTimeReservation = findViewById(R.id.textViewRemainingTimeReservation);
        textViewRemainingTimeReservation.setText(String.valueOf(rm.getTimestamp()));
        TextView textViewTotalIncomeReservation = findViewById(R.id.textViewTotalIncomeReservation);
        textViewTotalIncomeReservation.setText(String.valueOf(rm.getTotal_income()));
        TextView textViewNotesReservation = findViewById(R.id.textViewNotesReservation);
        textViewNotesReservation.setText(rm.getNotes());
        TextView textViewCustomerIdReservation = findViewById(R.id.customer_name);
        textViewCustomerIdReservation.setText(String.valueOf(rm.getCust_id()));
        TextView textViewCustomerPhoneNumberReservation = findViewById(R.id.customer_phone_number);
        textViewCustomerPhoneNumberReservation.setText(rm.getCust_phone());
        LinearLayout pending_order_detail_info = findViewById(R.id.pending_reservation_detail_info);
             LinearLayout ll = new LinearLayout(this);
            // 16dp
            float scale = getResources().getDisplayMetrics().density;
            int _16dp = (int) (16*scale + 0.5f);

            ll.setPadding(_16dp,0,_16dp,0);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params_ll = new LinearLayout.LayoutParams(  ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(params_ll);
            // Name of food
            TextView tv = new TextView(this);
            tv.setText("▶" + rd.getDishName());

            tv.setTextColor(Color.parseColor("#FF000000"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

            LinearLayout.LayoutParams params_name = new LinearLayout.LayoutParams(  0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,2f);
            tv.setLayoutParams(params_name);

            // Quantity
            TextView tv1 = new TextView(this);
            tv1.setText("x" + rd.getDishQty());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                tv1.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            }
            tv1.setTextColor(Color.parseColor("#FF000000"));
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            LinearLayout.LayoutParams params_qty = new LinearLayout.LayoutParams(   0, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
            params_qty.gravity = Gravity.END;

            tv1.setLayoutParams(params_qty);

            // Single price
            TextView tv2 = new TextView(this);
            tv2.setText(String.format("%.2f", rd.getDishPrice()) + "€" );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                tv2.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            }
            tv2.setTextColor(Color.parseColor("#FF000000"));
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            LinearLayout.LayoutParams params_price = new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
            params_price.gravity = Gravity.END;

            tv2.setLayoutParams(params_price);

            ll.addView(tv);
            ll.addView(tv1);
            ll.addView(tv2);
            pending_order_detail_info.addView(ll);
        }


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
    }*/
}
