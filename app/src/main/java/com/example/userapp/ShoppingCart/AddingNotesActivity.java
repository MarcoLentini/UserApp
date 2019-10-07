package com.example.userapp.ShoppingCart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.userapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class AddingNotesActivity extends AppCompatActivity {

    private TextInputLayout textInputNotes;
    private EditText etNotes;
    private Button btnCancel, btnSave;
    private FirebaseAuth auth;
    private String notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_add_notes);

        String title = getString(R.string.title_order_notes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        notes = getIntent().getExtras().getString("orderNotes");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
        }
        textInputNotes = findViewById(R.id.text_input_notes);
        etNotes = findViewById(R.id.edit_text_input_notes);
        etNotes.setText(notes);
        etNotes.selectAll();
        btnCancel = findViewById(R.id.etNotesBtnCancel);
        btnCancel.setOnClickListener(v -> finish());
        btnSave = findViewById(R.id.etNotesBtnSave);
        btnSave.setOnClickListener(v -> {

            Intent retIntent = new Intent(getApplicationContext(), ShoppingCartActivity.class);
            Bundle bn = new Bundle();
            notes = etNotes.getText().toString();
            bn.putString("orderNotes", notes);
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
    protected void onResume() {
        super.onResume();
        if (auth.getCurrentUser() == null) {

            finish();

        }
    }
}
