package com.example.userapp.shoppingCart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.userapp.R;

public class AddingAddressActivity extends AppCompatActivity {

    private TextInputLayout textInputDeliveryAddress;
    private EditText etDeliveryAddress;
    private Button btnCancel, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_add_address);

        String title = getString(R.string.title_delivery_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        textInputDeliveryAddress = findViewById(R.id.text_input_address);
        etDeliveryAddress = findViewById(R.id.edit_text_input_address);
        etDeliveryAddress.setHorizontallyScrolling(false);
        etDeliveryAddress.setLines(1);
        btnCancel = findViewById(R.id.etAddressBtnCancel);
        btnCancel.setOnClickListener(v -> finish());
        btnSave = findViewById(R.id.etAddressBtnSave);
        btnSave.setOnClickListener(v -> {

                Intent retIntent = new Intent(getApplicationContext(), ShoppingCartActivity.class);
                Bundle bn = new Bundle();
                String address = etDeliveryAddress.getText().toString();
                bn.putString("address", address);
                retIntent.putExtras(bn);
                setResult(RESULT_OK, retIntent);
                finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
