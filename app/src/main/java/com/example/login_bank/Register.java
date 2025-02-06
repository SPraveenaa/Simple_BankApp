package com.example.login_bank;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    EditText accountNumber;
    EditText userName;
    EditText email;
    EditText password;
    EditText reEnterPassword;
    EditText NIC;
    Button backBTN;
    Button doneBTN;
    DatabaseReference accountsReference;
    DatabaseReference customerReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase database references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        accountsReference = database.getReference("Accounts");
        customerReference = database.getReference("customer");

        // Initialize EditText and Button
        userName = findViewById(R.id.uname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.ps);
        reEnterPassword = findViewById(R.id.reps);
        NIC = findViewById(R.id.nic);
        backBTN = findViewById(R.id.back1);
        doneBTN = findViewById(R.id.done);
        accountNumber = findViewById(R.id.numberAccInput);

        doneBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userNameInput = userName.getText().toString().trim();
                final String emailInput = email.getText().toString().trim();
                final String passwordInput = password.getText().toString().trim();
                final String reEnterPasswordInput = reEnterPassword.getText().toString().trim();
                final String NICInput = NIC.getText().toString().trim();
                final String accountNumberInput = accountNumber.getText().toString().trim();

                if (!userNameInput.isEmpty() && !accountNumberInput.isEmpty()) {
                    // Check if the user-entered account number exists in "Accounts" path
                    accountsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(accountNumberInput)) {
                                // The user-entered account number exists in "Accounts"
                                // Proceed with storing the data in the "customer" path

                                // Get a reference to the user's unique "customer" path in Firebase
                                DatabaseReference userReference = customerReference.child(accountNumberInput);

                                // Store the user data under the user's unique path
                                userReference.child("userName").setValue(userNameInput);
                                userReference.child("email").setValue(emailInput);
                                userReference.child("password").setValue(passwordInput);
                                userReference.child("reEnterPassword").setValue(reEnterPasswordInput);
                                userReference.child("NIC").setValue(NICInput);
                                userReference.child("accountNumber").setValue(accountNumberInput);

                                Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();

                                // Clear input fields
                                userName.setText("");
                                email.setText("");
                                password.setText("");
                                reEnterPassword.setText("");
                                NIC.setText("");
                                accountNumber.setText("");


                            } else {
                                // The user-entered account number does not exist in "Accounts"
                                Toast.makeText(Register.this, "Invalid account number", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Register.this, "Database error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Register.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
