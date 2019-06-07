package com.example.userapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.example.userapp.Information.AddAddrActivity;
import com.example.userapp.Information.ResetPasswordActivity;
import com.example.userapp.Information.UserInformationActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private TextView tvAccountInfo;
    private TextView tvChangePwd;
    private TextView tvAddingAddress;
    private TextView tvPaymentMethod;
private FirebaseAuth auth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_setting);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

         //Get Firebase auth instance
         auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
        }
        tvAccountInfo = findViewById(R.id.tvSettingAccountInfo);
        tvAccountInfo.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, UserInformationActivity.class);
            startActivity(intent);
        });
        tvAddingAddress = findViewById(R.id.tvSettingAddress);
        tvAddingAddress.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, AddAddrActivity.class)));

        tvPaymentMethod =findViewById(R.id.tvSettingPayment);
        tvPaymentMethod.setOnClickListener(v -> Toast.makeText(SettingsActivity.this,"payment method activity ",Toast.LENGTH_SHORT).show());
    }

    protected void onResume() {
        super.onResume();
        if (auth.getCurrentUser() == null) {

            finish();

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
