package com.example.login_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class withdrawalStep03 extends AppCompatActivity {

    EditText digit_01;
    EditText digit_02;
    EditText digit_03;
    EditText digit_04;
    EditText digit_05;
    EditText digit_06;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal_step03);

        digit_01 = findViewById(R.id.digit01);
        digit_02 = findViewById(R.id.digit02);
        digit_03 = findViewById(R.id.digit03);
        digit_04 = findViewById(R.id.digit04);
        digit_05 = findViewById(R.id.digit05);
        digit_06 = findViewById(R.id.digit06);

        digit_01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 1){
                    digit_02.requestFocus();
                }

            }
        });

    }
}