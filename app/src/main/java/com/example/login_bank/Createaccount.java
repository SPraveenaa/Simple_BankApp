package com.example.login_bank;

import java.util.Calendar;
import java.util.Random;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Createaccount extends AppCompatActivity {

    EditText FirstName;
    EditText LastName;
    Button finishBTN;
    EditText NicNumber;
    EditText MobileNumber;
    EditText EmailAddress;
    EditText EmailPW; // Added field
    RadioButton GenderMale;
    RadioButton GenderFemale;
    EditText DateSelection;
    EditText Address01;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    LinearLayout cardPaymentLayout;
    LinearLayout bocAccountTransferLayout;

    //int userCount = 0;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);


        random = new Random();

        // Initialize Firebase database and reference
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Accounts");

        FirstName = findViewById(R.id.firstname);
        LastName = findViewById(R.id.lastname);
        NicNumber = findViewById(R.id.nic);
        MobileNumber = findViewById(R.id.mobilenumber);
        EmailAddress = findViewById(R.id.email);
        EmailPW = findViewById(R.id.emailPassword); // Initialize the field
        GenderMale = findViewById(R.id.maleRadioButton);
        GenderFemale = findViewById(R.id.femaleRadioButton);
        DateSelection = findViewById(R.id.dateEditText);
        Address01 = findViewById(R.id.address);

        cardPaymentLayout = findViewById(R.id.cardPaymentLayout);
        bocAccountTransferLayout = findViewById(R.id.bocAccountTransferLayout);


        Spinner paymentMethodSpinner = findViewById(R.id.spinner);
        paymentMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();

                // Show/hide layouts based on the selected option
                if (selectedOption.equals("Card payment")) {
                    cardPaymentLayout.setVisibility(View.VISIBLE);
                    bocAccountTransferLayout.setVisibility(View.GONE);
                } else if (selectedOption.equals("BOC account transfer")) {
                    cardPaymentLayout.setVisibility(View.GONE);
                    bocAccountTransferLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });

/*
        // Retrieve the last user ID from Firebase
        Query lastUserQuery = databaseReference.orderByKey().limitToLast(1);
        lastUserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String lastUserId = dataSnapshot.getChildren().iterator().next().getKey();
                    userCount = Integer.parseInt(lastUserId.substring(4)); // Extract the number from the user ID
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });

 */

        EditText dateEditText = findViewById(R.id.dateEditText);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        finishBTN = findViewById(R.id.createAccountDoneBTN);
        finishBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the input values
                String firstNameInput = FirstName.getText().toString().trim();
                String lastNameInput = LastName.getText().toString().trim();
                String nicNumberInput = NicNumber.getText().toString().trim();
                String mobileNumberInput = MobileNumber.getText().toString().trim();
                String emailAddressInput = EmailAddress.getText().toString().trim();
                String emailPasswordInput1 = EmailPW.getText().toString().trim(); // Retrieve the value

                // Perform validation
                if (TextUtils.isEmpty(firstNameInput) || !isStringOnlyAlphabet(firstNameInput)) {
                    setError(FirstName, "Please insert letters only for the first name");
                    return; // Stop further execution
                }

                if (TextUtils.isEmpty(lastNameInput) || !isStringOnlyAlphabet(lastNameInput) || TextUtils.isDigitsOnly(lastNameInput)) {
                    setError(LastName, "Please insert valid letters only for the last name");
                    return; // Stop further execution
                }

                if (TextUtils.isEmpty(nicNumberInput) || !isStringValidNIC(nicNumberInput)) {
                    setError(NicNumber, "Please insert a valid NIC number");
                    return; // Stop further execution
                }

                if (TextUtils.isEmpty(mobileNumberInput) || !isStringValidMobileNumber(mobileNumberInput)) {
                    setError(MobileNumber, "Please insert a valid mobile number");
                    return; // Stop further execution
                }

                if (TextUtils.isEmpty(emailAddressInput) || !isEmailValid(emailAddressInput)) {
                    setError(EmailAddress, "Please insert a valid email address");
                    return; // Stop further execution
                }

                // Generate the random number
                String randomNumber = generateRandomNumber();
                String accountNumber = generateAccountNumber();

                // If validation passes, continue with the remaining code
                // Create a new user object
                User user = new User();
                user.setFirstName(firstNameInput);
                user.setLastName(lastNameInput);
                user.setNicNumber(nicNumberInput);
                user.setMobileNumber(mobileNumberInput);
                user.setEmailAddress(emailAddressInput);
                user.setEmailPassword(emailPasswordInput1);
                user.setGender(GenderMale.isChecked() ? "Male" : "Female");
                user.setDateOfBirth(DateSelection.getText().toString());
                user.setAddress(Address01.getText().toString());
                user.setRandomNumber(randomNumber); // Set the random number
                user.setAccountNumber(accountNumber);

                /*
                // Generate the new user ID
                userCount++;
                String userID = "user" + userCount;

                 */

                // Save the user to Firebase database
                DatabaseReference userRef = databaseReference.child(accountNumber); // Use accountNumber as the key
                userRef.setValue(user)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Store the random number and account number inside the user node
                                    userRef.child("randomNumber").setValue(randomNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> randomNumberTask) {
                                            if (randomNumberTask.isSuccessful()) {
                                                // Store the account number
                                                userRef.child("accountNumber").setValue(accountNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> accountNumberTask) {
                                                        if (accountNumberTask.isSuccessful()) {
                                                            // Display success message
                                                            Toast.makeText(Createaccount.this, "User created successfully", Toast.LENGTH_SHORT).show();

                                                            // Clear input fields
                                                            FirstName.setText("");
                                                            LastName.setText("");
                                                            NicNumber.setText("");
                                                            MobileNumber.setText("");
                                                            EmailAddress.setText("");
                                                            EmailPW.setText("");
                                                            GenderMale.setChecked(false);
                                                            GenderFemale.setChecked(false);
                                                            DateSelection.setText("");
                                                            Address01.setText("");
                                                        } else {
                                                            // Display error message
                                                            Toast.makeText(Createaccount.this, "Failed to store account number", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                // Display error message
                                                Toast.makeText(Createaccount.this, "Failed to store random number", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    // Display error message
                                    Toast.makeText(Createaccount.this, "Failed to create user", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private String generateRandomNumber() {
        long randomNumber = (long) (random.nextDouble() * 9_000_000_000_000_000L) + 1_000_000_000_000_000L;
        return String.valueOf(randomNumber);
    }

    private String generateAccountNumber() {
        int accountNumber = random.nextInt(90000000) + 10000000;
        return String.valueOf(accountNumber);
    }

    private void showDatePickerDialog() {
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Createaccount.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        // Check if selected date is in the future
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, monthOfYear, dayOfMonth);

                        if (selectedDate.after(currentDate)) {
                            // Show error for future dates
                            setError(DateSelection, "Date of Birth cannot be in the future");
                        } else {
                            // Handle the selected date
                            String selectedDateStr = dayOfMonth + "/" + (monthOfYear + 1) + "/" + selectedYear;
                            DateSelection.setText(selectedDateStr);

                            // Clear error and reset color filter
                            DateSelection.setError(null);
                            DateSelection.getBackground().mutate().setColorFilter(null);
                        }
                    }
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private boolean isStringOnlyAlphabet(String input) {
        return input.matches("[a-zA-Z]+");
    }

    private boolean isStringValidNIC(String input) {
        return Pattern.matches("\\d{9}[VX]|\\d{12}", input);
    }

    private boolean isStringValidMobileNumber(String input) {
        // Remove any leading zeros
        input = input.replaceFirst("^0+(?!$)", "");

        // Add a leading zero if the number has 9 digits
        if (input.length() == 9) {
            input = "0" + input;
        }

        return Pattern.matches("0\\d{9}", input);
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void setError(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
        editText.requestFocus();

        // Change the EditText line color to red
        editText.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);

        // Display toast message at the bottom of the screen
        Toast toast = Toast.makeText(Createaccount.this, errorMessage, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 16);
        toast.show();

    }

}