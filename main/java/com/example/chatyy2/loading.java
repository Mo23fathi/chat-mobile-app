package com.example.chatyy2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.chatyy2.model.UserModel;
import com.example.chatyy2.utils.AndroidUtil;
import com.example.chatyy2.utils.FirebaseUtil;


public class loading extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Check if there are extras in the Intent (data from a notification)
        if (getIntent().getExtras() != null) {
            // Data received from notification
            String userId = getIntent().getExtras().getString("userId");

            // Retrieve user details from Firestore based on the userId
            FirebaseUtil.allUserCollectionReference().document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Convert the retrieved data to a UserModel
                            UserModel model = task.getResult().toObject(UserModel.class);

                            // Start the MainActivity
                            Intent mainIntent = new Intent(this, MainActivity.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(mainIntent);

                            // Start the ChatActivity with the retrieved UserModel
                            Intent intent = new Intent(this, ChatActivity.class);
                            AndroidUtil.passUserModelAsIntent(intent, model);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            // Finish the current activity
                            finish();
                        }
                    });
        } else {
            // No extras in the Intent (not from notification)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Check if the user is logged in to determine the next activity
                    if (FirebaseUtil.isLoggedIn()) {
                        // If logged in, start MainActivity
                        startActivity(new Intent(loading.this, MainActivity.class));
                    } else {
                        // If not logged in, start loginphonenum activity
                        startActivity(new Intent(loading.this, loginphonenum.class));
                    }
                    // Finish the current activity
                    finish();
                }
            }, 1000); // Delay for 1000 milliseconds (1 second)
        }
    }
}