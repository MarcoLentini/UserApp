package com.example.userapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyInfoActivity extends AppCompatActivity {

    private TextView tvInfoMessage;
    private EditText etEditInfo;
    private Button btnOk;
    private Button btnCancel;
    private String fieldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        tvInfoMessage = findViewById(R.id.textViewTypeInfo);
        etEditInfo = findViewById(R.id.editTextChangeInfo);
        btnOk = findViewById(R.id.buttonOk);
        btnCancel = findViewById(R.id.buttonCancel);
        String title;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent receivedIntent = getIntent();
        fieldName = receivedIntent.getExtras().getString("field");
        final String fieldValue = receivedIntent.getExtras().getString("value");
        switch (fieldName) {
            case "user_name":
                title=getString(R.string.username_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_username);
                etEditInfo.setText(fieldValue);
                etEditInfo.setHint("username");
                etEditInfo.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                //etEditInfo.setSelectAllOnFocus(true);
                etEditInfo.selectAll();
                break;
            case "user_email":
                title=getString(R.string.email_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_email);
                etEditInfo.setText(fieldValue);
                etEditInfo.setHint("example@domain.com");
                etEditInfo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case "user_phone_number":
                title=getString(R.string.phone_number_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_phone_number);
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case "user_description":
                title=getString(R.string.description_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_description);
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                etEditInfo.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
                etEditInfo.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etEditInfo.setMinLines(3);
                break;
            case "user_password":
                title=getString(R.string.password_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_old_password);
                etEditInfo.setText("");
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                etEditInfo.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);


                break;
        }
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etEditInfo , InputMethodManager.SHOW_IMPLICIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        etEditInfo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnOk.performClick();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                }

                return false;
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent retIntent;
                Bundle bn;
                if(fieldName.equals("user_password")) {
                    if(fieldValue.equals(etEditInfo.getText().toString())){
                    retIntent = new Intent(getApplicationContext(), ChangePwdActivity.class);
                    bn = new Bundle();

                    bn.putString("field", fieldName);
                    retIntent.putExtras(bn);
                    startActivityForResult(retIntent, 1);
                    }else{

                        Toast mioToast = Toast.makeText(ModifyInfoActivity.this,
                                getString(R.string.invalid_password),
                                Toast.LENGTH_LONG);
                        mioToast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 64);
                        InputMethodManager imm = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                        mioToast.show();

                        etEditInfo.selectAll();


                    }
                }else {
                    retIntent = new Intent(getApplicationContext(), MainActivity.class);
                    bn = new Bundle();
                    bn.putString("field", fieldName);
                    bn.putString("value", etEditInfo.getText().toString());
                    retIntent.putExtras(bn);
                    setResult(1, retIntent);
                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent receivedIntent = getIntent();
        Bundle bn=receivedIntent.getExtras();
        fieldName = receivedIntent.getExtras().getString("field");
        String fieldValue = receivedIntent.getExtras().getString("value");
        Log.d("marcole", "onActivityResult: "+ fieldName + "  "+ fieldValue);
        bn.putString("field", fieldName);
        bn.putString("value", fieldName);
        Intent retIntent = new Intent(getApplicationContext(), MainActivity.class);
        retIntent.putExtras(bn);
        setResult(1, retIntent);
        finish();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

