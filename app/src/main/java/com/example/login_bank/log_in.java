package com.example.login_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class log_in extends AppCompatActivity {

    // Implement the following methods to get user input for account number and password
    private String getAccountNumberFromInput() {
        EditText accountNumberEditText = findViewById(R.id.username);
        return accountNumberEditText.getText().toString().trim();
    }

    private String getPasswordFromInput() {
        EditText passwordEditText = findViewById(R.id.password);
        return passwordEditText.getText().toString().trim();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Login button click listener
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            String accountNumber = getAccountNumberFromInput(); // Get user input account number
            String password = getPasswordFromInput(); // Get user input password

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("customer").child(accountNumber);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String storedPassword = dataSnapshot.child("password").getValue(String.class);
                        if (storedPassword != null && storedPassword.equals(password)) {
                            // Passwords match, login success
                            Toast.makeText(log_in.this, "Login success", Toast.LENGTH_SHORT).show();
                            openHomePage(); // Open the HomeActivity (Register activity in your case)
                        } else {
                            // Incorrect password, handle the error
                            Toast.makeText(log_in.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Account number does not exist in the database, handle the error
                        Toast.makeText(log_in.this, "Account number does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Error occurred while reading data from the database, handle the error
                    Toast.makeText(log_in.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Custom method to open the HomeActivity (Register activity in your case)
    private void openHomePage() {
        Intent intent = new Intent(log_in.this, home.class);
        startActivity(intent);
        finish(); // Optionally, you can finish the current log_in activity to prevent the user from going back to it with the back button.
    }
}
