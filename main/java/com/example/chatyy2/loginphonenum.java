package com.example.chatyy2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbb20.CountryCodePicker;

public class loginphonenum extends AppCompatActivity {

    // Declare UI elements
    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    Button sendOtpBtn;
    ProgressBar progressBar;

    // Override the onCreate method to initialize the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginphonenum);

        // Find and initialize UI elements
        countryCodePicker = findViewById(R.id.login_countrycode);
        phoneInput = findViewById(R.id.login_mobile_number);
        sendOtpBtn = findViewById(R.id.send_otp_btn);
        progressBar = findViewById(R.id.login_progress_bar);

        // Set the progress bar to be initially invisible
        progressBar.setVisibility(View.GONE);

        // Register the phone number EditText with the CountryCodePicker
        countryCodePicker.registerCarrierNumberEditText(phoneInput);

        // Set up a click listener for the "Send OTP" button
        sendOtpBtn.setOnClickListener((v) -> {
            // Check if the entered phone number is valid
            if (!countryCodePicker.isValidFullNumber()) {
                phoneInput.setError("Phone number not valid");
                return;
            }

            // If valid, create an Intent to start the otp activity and pass the phone number as an extra
            Intent intent = new Intent(loginphonenum.this, otp.class);
            intent.putExtra("phone", countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);
        });
    }
}