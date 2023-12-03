package com.example.chatyy2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.chatyy2.model.UserModel;
import com.example.chatyy2.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
public class loginusername extends AppCompatActivity {


    // Declare UI elements and variables
    EditText usernameInput;
    EditText passwordInput;
    Button letMeInBtn;
    ProgressBar progressBar;
    String phoneNumber;
    UserModel userModel;

    // Override the onCreate method to initialize the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginusername);

        // Find and initialize UI elements
        usernameInput = findViewById(R.id.login_username);
        passwordInput = findViewById(R.id.login_password);
        letMeInBtn = findViewById(R.id.login_let_me_in_btn);
        progressBar = findViewById(R.id.login_progress_bar);

        // Get the phone number passed from the previous activity
        phoneNumber = getIntent().getExtras().getString("phone");

        // Call the method to get the username associated with the user
        getUsername();

        // Set up a click listener for the "Let Me In" button
        letMeInBtn.setOnClickListener((v) -> {
            // Call the method to set the username
            setUsername();
        });
    }

    // Method to set the username for the user
    void setUsername() {
        // Get the entered username and password
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        // Validate the length of the username and password
        if (username.isEmpty() || username.length() < 3 && password.isEmpty() || password.length() < 6) {
            usernameInput.setError("Username length should be at least 3 chars and password length should be at least 6 chars ");
            return;
        }

        // Set the UI to indicate progress
        setInProgress(true);

        // Check if a UserModel already exists
        if (userModel != null) {
            // Update the existing UserModel with the new username and password
            userModel.setUsername(username);
            userModel.setPassword(password);
        } else {
            // Create a new UserModel if it doesn't exist
            userModel = new UserModel(phoneNumber, username, Timestamp.now(), FirebaseUtil.currentUserId(), password);
        }

        // Save the UserModel to Firestore
        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Set the UI to indicate the completion of the operation
                setInProgress(false);
                if (task.isSuccessful()) {
                    // If successful, start the MainActivity and clear the activity stack
                    Intent intent = new Intent(loginusername.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    // Method to get the username associated with the user
    void getUsername() {
        // Set the UI to indicate progress
        setInProgress(true);

        // Retrieve the user details from Firestore
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // Set the UI to indicate the completion of the operation
                setInProgress(false);
                if (task.isSuccessful()) {
                    // Convert the result to a UserModel
                    userModel = task.getResult().toObject(UserModel.class);
                    if (userModel != null) {
                        // Display the existing username in the EditText
                        usernameInput.setText(userModel.getUsername());
                    }
                }
            }
        });
    }

    // Method to set the UI state based on the progress of an operation
    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            letMeInBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            letMeInBtn.setVisibility(View.VISIBLE);
        }
    }
}
