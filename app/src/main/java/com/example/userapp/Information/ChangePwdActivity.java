package com.example.userapp.Information;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.userapp.MainActivity;
import com.example.userapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePwdActivity extends AppCompatActivity {


    private EditText etEditPwd2;
    private EditText etEditPwd1;
    private Button btnOk;
    private Button btnCancel;
    private String fieldName;
    private int type; //indica in che caso siamo
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private static final String bikerDataFile = "BikerDataFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        etEditPwd2 = findViewById(R.id.editTextRepeatPwd);
        etEditPwd1 = findViewById(R.id.editTextAskPwd);
        btnOk = findViewById(R.id.buttonOk);
        btnCancel = findViewById(R.id.buttonCancel);
        String title;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        title=getString(R.string.password_title);
        getSupportActionBar().setTitle(title);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
        }


        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();


        //Get Firestore instance
        db = FirebaseFirestore.getInstance();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etEditPwd1, InputMethodManager.SHOW_IMPLICIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        etEditPwd2.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btnOk.performClick();
                InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.showSoftInput(etEditPwd2, InputMethodManager.SHOW_IMPLICIT);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }

            return false;
        });

        btnOk.setOnClickListener(v -> {
            if(etEditPwd1.getText().toString().equals(etEditPwd2.getText().toString())) {
                user.updatePassword(etEditPwd1.getText().toString())
                        .addOnSuccessListener(task->{
                            Intent retIntent = new Intent(getApplicationContext(), UserInformationActivity.class);
                            startActivity(retIntent);
                            finish();
                        })
                        .addOnFailureListener(task->{Toast mioToast = Toast.makeText(ChangePwdActivity.this,
                                getString(R.string.password_failed_updated),
                                Toast.LENGTH_LONG);
                            mioToast.setGravity(Gravity.BOTTOM, 0, 64);
                            etEditPwd2.requestFocus();
                            InputMethodManager imm12 = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm12.showSoftInput(etEditPwd2, InputMethodManager.SHOW_IMPLICIT);
                            mioToast.show();

                            etEditPwd2.selectAll();});


            }else{
                Toast mioToast = Toast.makeText(ChangePwdActivity.this,
                        getString(R.string.different_password),
                        Toast.LENGTH_LONG);
                mioToast.setGravity(Gravity.BOTTOM, 0, 64);
                etEditPwd2.requestFocus();
                InputMethodManager imm12 = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm12.showSoftInput(etEditPwd2, InputMethodManager.SHOW_IMPLICIT);
                mioToast.show();

                etEditPwd2.selectAll();

            }

        });

        btnCancel.setOnClickListener(v -> finish());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}