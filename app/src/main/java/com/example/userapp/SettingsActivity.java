package com.example.userapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.example.userapp.information.ChangePwdActivity;
import com.example.userapp.information.ResetPasswordActivity;
import com.example.userapp.information.UserInformationActivity;

public class SettingsActivity extends AppCompatActivity {

    private TextView tvAccountInfo;
    private TextView tvChangePwd;
    private TextView tvAddingAddress;
    private TextView tvPaymentMethod;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbarRestaurantDetails);
        toolbar.setTitle(R.string.menu_setting);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        tvAccountInfo = findViewById(R.id.tvSettingAccountInfo);
        tvAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, UserInformationActivity.class);
                startActivity(intent);
            }
        });
        tvAddingAddress = findViewById(R.id.tvSettingAddress);
        tvAddingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this,"adding new address activity ",Toast.LENGTH_SHORT).show();
            }
        });
        tvChangePwd = findViewById(R.id.tvSettingChangePwd);
        tvChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(intent);
                Toast.makeText(SettingsActivity.this,"change password activity ",Toast.LENGTH_SHORT).show();

            }
        });
        tvPaymentMethod =findViewById(R.id.tvSettingPayment);
        tvPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this,"payment method activity ",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
