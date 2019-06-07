package com.example.userapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.widget.Toolbar;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = findViewById(R.id.helpToolbar);
        toolbar.setTitle(R.string.menu_help);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HelpActivity.this,MainActivity.class);
        startActivity(intent);
    }

}
