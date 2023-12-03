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
import android.widget.RelativeLayout;
import android.widget.TextView;

// Import RecyclerView and related classes from the androidx library
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Import custom classes and libraries used in the code
import com.example.chatyy2.ChatActivity;
import com.example.chatyy2.R;
import com.example.chatyy2.model.ChatroomModel;
import com.example.chatyy2.model.UserModel;
import com.example.chatyy2.utils.AndroidUtil;
import com.example.chatyy2.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

// Define the RecentChatRecyclerAdapter class, extending FirestoreRecyclerAdapter
public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatroomModel, RecentChatRecyclerAdapter.ChatroomModelViewHolder> {

    // Declare a Context variable to hold the context of the calling activity
    Context context;

    // Constructor for RecentChatRecyclerAdapter, taking FirestoreRecyclerOptions and Context as parameters
    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatroomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    // Override the onBindViewHolder method to bind data to the view holder
    @Override
    protected void onBindViewHolder(@NonNull ChatroomModelViewHolder holder, int position, @NonNull ChatroomModel model) {
        // Use FirebaseUtil to get information about the other user in the chatroom
        FirebaseUtil.getOtherUserFromChatroom(model.getUserIds())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Check if the last message was sent by the current user
                        boolean lastMessageSentByMe = model.getLastMessageSenderId().equals(FirebaseUtil.currentUserId());

                        // Convert the result to a UserModel
                        UserModel otherUserModel = task.getResult().toObject(UserModel.class);

                        // Use FirebaseUtil to get the download URL for the other user's profile picture
                        FirebaseUtil.getOtherProfilePicStorageRef(otherUserModel.getUserId()).getDownloadUrl()
                                .addOnCompleteListener(t -> {
                                    if (t.isSuccessful()) {
                                        // If successful, set the profile picture using AndroidUtil
                                        Uri uri = t.getResult();
                                        AndroidUtil.setProfilePic(context, uri, holder.profilePic);
                                    }
                                });

                        // Set various properties in the view holder based on the chatroom model and other user information
                        holder.usernameText.setText(otherUserModel.getUsername());
                        if (lastMessageSentByMe)
                            holder.lastMessageText.setText("You : " + model.getLastMessage());
                        else
                            holder.lastMessageText.setText(model.getLastMessage());
                        holder.lastMessageTime.setText(FirebaseUtil.timestampToString(model.getLastMessageTimestamp()));

                        // Set up a click listener to navigate to the chat activity when the item is clicked
                        holder.itemView.setOnClickListener(v -> {
                            // Navigate to chat activity
                            Intent intent = new Intent(context, ChatActivity.class);
                            AndroidUtil.passUserModelAsIntent(intent, otherUserModel);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        });
                    }
                });
    }

    // Override the onCreateViewHolder method to inflate the layout for each chatroom item
    @NonNull
    @Override
    public ChatroomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the chatroom item layout from XML
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent, false);
        // Return a new instance of ChatroomModelViewHolder with the inflated view
        return new ChatroomModelViewHolder(view);
    }

    // Define the ChatroomModelViewHolder class, extending RecyclerView.ViewHolder
    class ChatroomModelViewHolder extends RecyclerView.ViewHolder {
        // Declare views used in each chatroom item
        TextView usernameText;
        TextView lastMessageText;
        TextView lastMessageTime;
        ImageView profilePic;

        // Constructor for ChatroomModelViewHolder, taking a View as a parameter
        public ChatroomModelViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views by finding them in the inflated layout
            usernameText = itemView.findViewById(R.id.user_name_text);
            lastMessageText = itemView.findViewById(R.id.last_message_text);
            lastMessageTime = itemView.findViewById(R.id.last_message_time_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
