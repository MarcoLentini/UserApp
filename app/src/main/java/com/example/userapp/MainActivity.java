package com.example.userapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvUserName;
    private TextView tvUserEmail;
    private TextView tvUserPhoneNumber;
    private TextView tvUserDescription;
    private TextView tvUserPassword;
    private String userName;
    private String userEmail;
    private String userPhoneNumber;
    private String userDescription;
    private String userPassword;

    private SharedPreferences sharedPref;
    private static final String userFile = "UserDataFile" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String title=getString(R.string.InfoTitle);
        getSupportActionBar().setTitle(title);
        tvUserName = findViewById(R.id.textViewUserName);
        tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idField = getString(R.string.name_field_id);
                invokeModifyInfoActivity(idField, userName);
            }
        });

        tvUserEmail = findViewById(R.id.textViewUserEmail);
        tvUserEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idField = getString(R.string.email_field_id);
                invokeModifyInfoActivity(idField, userEmail);
            }
        });

        tvUserPhoneNumber = findViewById(R.id.textViewUserPhoneNumber);
        tvUserPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idField = getString(R.string.phone_number__field_id);
                invokeModifyInfoActivity(idField, userPhoneNumber);
            }
        });

        tvUserDescription = findViewById(R.id.textViewUserDescription);
        tvUserDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idField = getString(R.string.description_field_id);
                invokeModifyInfoActivity(idField, userDescription);
            }
        });

        tvUserPassword = findViewById(R.id.textViewChangePassword);
        tvUserPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userPassword.equals(""))
                invokeChangePwdActivity(getString(R.string.pwd_field_id));
                else{
                    String idField = getString(R.string.pwd_field_id);
                    invokeModifyInfoActivity(idField, userPassword);

                }
            }
        });

        sharedPref = getSharedPreferences(userFile, Context.MODE_PRIVATE);
        userName = sharedPref.getString("userName", "");
        if(!userName.equals(""))
            tvUserName.setText(userName);
        userEmail = sharedPref.getString("userEmail", "");
        if(!userEmail.equals(""))
            tvUserEmail.setText(userEmail);
        userPhoneNumber = sharedPref.getString("userPhoneNumber", "");
        if(!userPhoneNumber.equals(""))
            tvUserPhoneNumber.setText(userPhoneNumber);
        userDescription = sharedPref.getString("userDescription", "");
        if(!userDescription.equals(""))
            tvUserDescription.setText(R.string.description_title);
        userPassword = sharedPref.getString("userPassword", "");

    }

    private void invokeModifyInfoActivity(String fieldName, String fieldNameValue) {
        Intent intent = new Intent(getApplicationContext(), ModifyInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("field", fieldName);
        bundle.putString("value", fieldNameValue);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }
    private void invokeChangePwdActivity( String fieldName) {
        Intent intent = new Intent(getApplicationContext(), ChangePwdActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("field", fieldName);

        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(resultCode == 1) {
            switch (data.getExtras().getString("field")) {
                case "user_name":
                    userName = data.getExtras().getString("value");
                    if(!userName.equals("")) {
                        editor.putString("userName", userName);
                        editor.commit();
                        tvUserName.setText(userName);
                    }
                    break;
                case "user_email":
                    userEmail = data.getExtras().getString("value");
                    if(!userEmail.equals("")) {
                        editor.putString("userEmail", userEmail);
                        editor.commit();
                        tvUserEmail.setText(userEmail);
                    }
                    break;
                case "user_phone_number":
                    userPhoneNumber = data.getExtras().getString("value");
                    if(!userPhoneNumber.equals("")) {
                        editor.putString("userPhoneNumber", userPhoneNumber);
                        editor.commit();
                        tvUserPhoneNumber.setText(userPhoneNumber);
                    }
                    break;
                case "user_description":
                    userDescription = data.getExtras().getString("value");
                    if(!userDescription.equals("")) {
                        editor.putString("userDescription", userDescription);
                        editor.commit();
                        tvUserDescription.setText(R.string.description_title);
                    }
                    break;
                case "user_password":
                    userPassword = data.getExtras().getString("value");
                    if(!userPassword.equals("")) {
                        editor.putString("userPassword", userPassword);
                        editor.commit();
                    }

                    break;
            }
        }
    }
}
