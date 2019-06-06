package com.example.userapp.Information;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userapp.R;
;
import com.example.userapp.ShoppingCart.AddressModel;
import com.example.userapp.ShoppingCart.GeocodingLocation;
import com.example.userapp.ShoppingCart.ShoppingCartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddAddrActivity extends AppCompatActivity {

    private TextInputLayout textInputDeliveryTown,textInputDeliveryStreet,textInputDeliveryNumber,textInputDeliveryNotes;
    private EditText etDeliveryTown,etDeliveryStreet,etDeliveryNumber,etDeliveryNotes;
    private TextView tvPastAddr;
    private Button btnCancel, btnSave;
    private String town,street,notes,old_notes,new_notes;
    private Long number;
    private AddressModel address,old_address,new_address;
    private ProgressBar pb;
private FirebaseAuth auth;
private FirebaseFirestore db;
    private HashMap<String,AddressModel> availableAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_add_address);

        String title = getString(R.string.title_delivery_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

         //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
        }
        db = FirebaseFirestore.getInstance();
        availableAddress=new HashMap<>();

        textInputDeliveryTown = findViewById(R.id.text_input_city);
        textInputDeliveryStreet = findViewById(R.id.text_input_route);
        textInputDeliveryNumber = findViewById(R.id.text_input_number);
        textInputDeliveryNumber = findViewById(R.id.text_input_delivery_notes);
        textInputDeliveryNotes=findViewById(R.id.text_input_delivery_notes);
        pb=findViewById(R.id.progressBarAddOrder);
        etDeliveryTown=findViewById(R.id.edit_text_input_city);
        etDeliveryStreet=findViewById(R.id.edit_text_input_route);
        etDeliveryNumber=findViewById(R.id.edit_text_input_number);
        etDeliveryNotes= findViewById(R.id.edit_text_input_delivery_notes);
        tvPastAddr=findViewById(R.id.past_addr);



        btnCancel = findViewById(R.id.etAddressBtnCancel);
        btnCancel.setOnClickListener(v -> finish());
        btnSave = findViewById(R.id.etAddressBtnSave);
        btnSave.setOnClickListener(v -> {
            address= new AddressModel(etDeliveryTown.getText().toString(),etDeliveryStreet.getText().toString(),Long.parseLong(etDeliveryNumber.getText().toString()));
            GeocodingLocation locationAddress = new GeocodingLocation();
            locationAddress.getAddressFromLocation(address.toString(),
                    this, new GeocoderHandler());
            pb.setVisibility(View.VISIBLE);
        });

        db.collection("address").whereEqualTo("user_id",auth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();
                        if (!document.isEmpty()) {
                            AddressModel tmpAddr;
                            for (DocumentSnapshot doc : document) {
                                HashMap<String,Object> hash_addr= (HashMap<String, Object>) doc.get("address");
                                tmpAddr=new AddressModel((String)hash_addr.get("town"),(String)hash_addr.get("street"),(Long)hash_addr.get("number"),(String)hash_addr.get("notes"));
                                availableAddress.put(tmpAddr.toString(), tmpAddr);

                            }
                            tvPastAddr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                usePastAddr();
                                }
                            });
                    }
                    }
                    else{
                        Log.d("AddingAddressActivity", "not find past address");
                    }
                });






    }

    private void fillEditText(String s) {
        AddressModel addr=availableAddress.get(s);
        etDeliveryTown.setText(addr.getTown());
        etDeliveryStreet.setText(addr.getStreet());
        etDeliveryNumber.setText(addr.getNumber().toString());
        etDeliveryNotes.setText(addr.getNotes());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    protected void onResume() {
        super.onResume();
        if (auth.getCurrentUser() == null) {

            finish();

        }
    }

    private void usePastAddr(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select one address");
        String[] pastAddress= availableAddress.keySet().toArray(new String[availableAddress.keySet().size()]);
        builder.setMultiChoiceItems(pastAddress, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked) {
                    new_address=availableAddress.get(pastAddress[which]);
                    fillEditText(new_address.toString());
                    dialog.cancel();
                }
            }
        });
        builder.setPositiveButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            if(locationAddress != null){

                 town = etDeliveryTown.getText().toString();
                street=etDeliveryStreet.getText().toString();
                number=Long.parseLong(etDeliveryNumber.getText().toString());
                notes=etDeliveryNotes.getText().toString();
                AddressModel addr= new AddressModel(town,street,number,notes);
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),getString(R.string.restaurants_filter_done),Toast.LENGTH_SHORT).show();

                if(!availableAddress.containsValue(addr)) {
                    Map<String, Object> address_map = new HashMap<>();
                    address_map.put("user_id", auth.getCurrentUser().getUid());
                    address_map.put("address", addr);
                    db.collection("address").document().set(address_map)
                            .addOnSuccessListener(task -> {
                                Log.d("AddingAddress", "Address updated");
                            })
                            .addOnFailureListener(task -> {
                                Log.d("AddingAddress", "Failed to upload address");
                            });
                }

                finish();
            }else{
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),getString(R.string.address_not_correct),Toast.LENGTH_SHORT).show();
                etDeliveryTown.selectAll();
            }

        }
    }
}
