// Declare the package for this class
package com.example.chatyy2.adapter;

// Import necessary Android libraries
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

// Import RecyclerView and related classes from the androidx library
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Import custom classes and libraries used in the code
import com.example.chatyy2.ChatActivity;
import com.example.chatyy2.R;
import com.example.chatyy2.model.UserModel;
import com.example.chatyy2.utils.AndroidUtil;
import com.example.chatyy2.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

// Define the SearchUserRecyclerAdapter class, extending FirestoreRecyclerAdapter
public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelViewHolder> {

    // Declare a Context variable to hold the context of the calling activity
    Context context;

    // Constructor for SearchUserRecyclerAdapter, taking FirestoreRecyclerOptions and Context as parameters
    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    // Override the onBindViewHolder method to bind data to the view holder
    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {
        // Set the username and phone number in the view holder
        holder.usernameText.setText(model.getUsername());
        holder.phoneText.setText(model.getPhone());

        // Check if the user is the current user, and modify the username accordingly
        if (model.getUserId().equals(FirebaseUtil.currentUserId())) {
            holder.usernameText.setText(model.getUsername() + " (Me)");
        }

        // Use FirebaseUtil to get the download URL for the other user's profile picture
        FirebaseUtil.getOtherProfilePicStorageRef(model.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        // If successful, set the profile picture using AndroidUtil
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(context, uri, holder.profilePic);
                    }
                });

        // Set up a click listener to navigate to the chat activity when the item is clicked
        holder.itemView.setOnClickListener(v -> {
            // Navigate to chat activity
            Intent intent = new Intent(context, ChatActivity.class);
            AndroidUtil.passUserModelAsIntent(intent, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    // Override the onCreateViewHolder method to inflate the layout for each user item
    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the user item layout from XML
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent, false);
        // Return a new instance of UserModelViewHolder with the inflated view
        return new UserModelViewHolder(view);
    }

    // Define the UserModelViewHolder class, extending RecyclerView.ViewHolder
    class UserModelViewHolder extends RecyclerView.ViewHolder {
        // Declare views used in each user item
        TextView usernameText;
        TextView phoneText;
        ImageView profilePic;

        // Constructor for UserModelViewHolder, taking a View as a parameter
        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views by finding them in the inflated layout
            usernameText = itemView.findViewById(R.id.user_name_text);
            phoneText = itemView.findViewById(R.id.phone_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
