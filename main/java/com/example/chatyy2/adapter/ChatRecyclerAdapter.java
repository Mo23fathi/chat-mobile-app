// Declare the package for this class
package com.example.chatyy2.adapter;

// Import necessary Android libraries
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

// Import RecyclerView and related classes from the androidx library
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Import custom classes and libraries used in the code
import com.example.chatyy2.ChatActivity;
import com.example.chatyy2.R;
import com.example.chatyy2.model.ChatMessageModel;
import com.example.chatyy2.utils.AndroidUtil;
import com.example.chatyy2.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

// Define the ChatRecyclerAdapter class, extending FirestoreRecyclerAdapter
public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatRecyclerAdapter.ChatModelViewHolder> {

    // Declare a Context variable to hold the context of the calling activity
    Context context;

    // Constructor for ChatRecyclerAdapter, taking FirestoreRecyclerOptions and Context as parameters
    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    // Override the onBindViewHolder method to bind data to the view holder
    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatMessageModel model) {
        // Log a message for debugging purposes
        Log.i("haushd", "asjd");

        // Check if the message sender is the current user
        if (model.getSenderId().equals(FirebaseUtil.currentUserId())) {
            // If yes, hide the left chat layout and show the right chat layout
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            // Set the message text in the right chat text view
            holder.rightChatTextview.setText(model.getMessage());
        } else {
            // If no, hide the right chat layout and show the left chat layout
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            // Set the message text in the left chat text view
            holder.leftChatTextview.setText(model.getMessage());
        }
    }

    // Override the onCreateViewHolder method to inflate the layout for each chat message
    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the chat message layout from XML
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false);
        // Return a new instance of ChatModelViewHolder with the inflated view
        return new ChatModelViewHolder(view);
    }

    // Define the ChatModelViewHolder class, extending RecyclerView.ViewHolder
    class ChatModelViewHolder extends RecyclerView.ViewHolder {

        // Declare views used in each chat message row
        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextview, rightChatTextview;

        // Constructor for ChatModelViewHolder, taking a View as a parameter
        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views by finding them in the inflated layout
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);
        }
    }
}
