package com.example.login_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class withdrawalStep01 extends AppCompatActivity {

    EditText digi01;
    EditText digi02;
    EditText digi03;
    EditText digi04;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal_step01);

        digi01 = findViewById(R.id.digit01);
        digi02 = findViewById(R.id.digit02);
        digi03 = findViewById(R.id.digit03);
        digi04 = findViewById(R.id.digit04);


        digi01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    digi02.requestFocus();
                }
            }
        });


        digi02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    digi03.requestFocus();
                }
            }
        });

        digi03.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    digi04.requestFocus();
                }
            }
        });



    }
}