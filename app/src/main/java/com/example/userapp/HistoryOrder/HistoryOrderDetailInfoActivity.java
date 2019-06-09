package com.example.userapp.HistoryOrder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.userapp.CurrentOrder.CurrentOrderItemModel;
import com.example.userapp.CurrentOrder.CurrentOrderModel;
import com.example.userapp.HistoryOrderActivity;
import com.example.userapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryOrderDetailInfoActivity  extends AppCompatActivity {

    private static final String TAG = "HistoryOrderDetailInfo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_order_detail_info);

        String title = getString(R.string.history_order_detail_informantion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
        Intent receivedIntent = getIntent();

        Integer itemPosition = receivedIntent.getExtras().getInt("historyOrderData");
        CurrentOrderModel rm = HistoryOrderActivity.historyOrders.get(itemPosition);


        TextView textViewStatusHistoryOrder = findViewById(R.id.tvStatusHistoryOrder);
        textViewStatusHistoryOrder.setText(String.valueOf(rm.getRs_status()));
        TextView textViewRestaurantName = findViewById(R.id.tvRestaurantNameHistoryOrder);
        textViewRestaurantName.setText(rm.getRest_name());

        TextView textViewTotalCost = findViewById(R.id.tv_history_order_total_cost);
        textViewTotalCost.setText(String.format("%.2f", rm.getTotal_income())+" €");

        TextView textViewAddress = findViewById(R.id.tvHistoryOrderDeliveryAddress);
        textViewAddress.setText(rm.getCust_address());
        TextView textViewTime = findViewById(R.id.tvHistoryOrderDeliveryTime);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date date=rm.getDelivery_time().toDate();
        textViewTime.setText( dateFormat.format(date));



        TextView textViewOrderId = findViewById(R.id.tvHistoryOrderID);
        textViewOrderId.setText(String.valueOf(rm.getRs_id()));

        LinearLayout current_order_detail_info = findViewById(R.id.llHistoryOrderDetailInfo);
        for (CurrentOrderItemModel rd : rm.getDishes()) {
            LinearLayout ll = new LinearLayout(this);
            // 16dp
            float scale = getResources().getDisplayMetrics().density;
            int _16dp = (int) (16 * scale + 0.5f);

            ll.setPadding(_16dp, 0, _16dp, 0);
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

            TextView textViewCurrentOrderCreateTime = findViewById(R.id.tvHistoryOrderCreateTime);
            date=rm.getTimestamp().toDate();
            textViewCurrentOrderCreateTime.setText( dateFormat.format(date));

            tv2.setLayoutParams(params_price);

            ll.addView(tv);
            ll.addView(tv1);
            ll.addView(tv2);
            current_order_detail_info.addView(ll);
        }


        Button btnLeaveComments = findViewById(R.id.btnCommentForThisOrder);

        if (!rm.getIs_commented()){ //this order is not left a comment
            btnLeaveComments.setVisibility(View.VISIBLE);
        }else {
            btnLeaveComments.setVisibility(View.GONE);
        }

        btnLeaveComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnLeaveComments is clicked,AddCommentsActivity is called.");
                Intent intent = new Intent(HistoryOrderDetailInfoActivity.this, com.example.userapp.Comments.AddCommentsActivity.class);
                Bundle bn = new Bundle();
                bn.putInt("historyOrderData", itemPosition);
                intent.putExtras(bn);
                startActivity(intent);
            }
        });

    }

  /*  private void returnVal(int itemPosition, String ret){
        Intent retIntent = new Intent();
        Bundle bn = new Bundle();
        bn.putInt("pos", itemPosition);
        bn.putString("result", ret);
        retIntent.putExtras(bn);
        setResult(RESULT_OK, retIntent);
        finish();
    }
*/
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
