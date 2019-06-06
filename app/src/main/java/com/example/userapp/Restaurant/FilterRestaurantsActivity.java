package com.example.userapp.Restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.userapp.MainActivity;
import com.example.userapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FilterRestaurantsActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    private ArrayList<String> selectedFilters;
    private LinearLayout linearLayoutCheckboxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_restaurants);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        getSupportActionBar().setTitle(R.string.select_filters);

//Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
        }

        linearLayoutCheckboxes = findViewById(R.id.linearLayoutCheckBoxes);

        Intent receivedIntent = getIntent();
        ArrayList<String> receivedArrayList = receivedIntent.getStringArrayListExtra("selectedFilters");
        if(receivedArrayList == null)
            selectedFilters = new ArrayList<>();
        else {
            int count = linearLayoutCheckboxes.getChildCount();
            for (int i = 0; i < count; i++) {
                View v = linearLayoutCheckboxes.getChildAt(i);
                if (v instanceof CheckBox) {
                    String currentCheckBoxText = ((CheckBox) v).getText().toString();
                    if(receivedArrayList.contains(currentCheckBoxText))
                        ((CheckBox) v).setChecked(true);
                }
            }
            selectedFilters = receivedArrayList;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_restaurants, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_confirm_filter_restaurants) {
            selectedFilters.clear();
            int count = linearLayoutCheckboxes.getChildCount();
            for (int i = 0; i < count; i++) {
                View v = linearLayoutCheckboxes.getChildAt(i);
                if (v instanceof CheckBox) {
                    if(((CheckBox) v).isChecked())
                        selectedFilters.add(((CheckBox) v).getText().toString());
                }
            }
            Intent retIntent = new Intent(getApplicationContext(), MainActivity.class);
            Bundle bn = new Bundle();
            bn.putStringArrayList("selectedFilters", selectedFilters);
            retIntent.putExtras(bn);
            setResult(RESULT_OK, retIntent);
            finish();
        }

        if(item.getItemId() == R.id.action_clear_all_filter_restaurants) {
            int count = linearLayoutCheckboxes.getChildCount();
            for (int i = 0; i < count; i++) {
                View v = linearLayoutCheckboxes.getChildAt(i);
                if (v instanceof CheckBox) {
                    if(((CheckBox) v).isChecked())
                        ((CheckBox) v).setChecked(false);
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (auth.getCurrentUser() == null) {

            finish();

        }
    }
}
