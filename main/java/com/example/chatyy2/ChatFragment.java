package com.example.chatyy2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatyy2.adapter.RecentChatRecyclerAdapter;

import com.example.chatyy2.model.ChatroomModel;
import com.example.chatyy2.model.UserModel;
import com.example.chatyy2.utils.FirebaseUtil;
import com.example.chatyy2.adapter.RecentChatRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class ChatFragment extends Fragment {
    // Declare RecyclerView and RecentChatRecyclerAdapter variables
    RecyclerView recyclerView;
    RecentChatRecyclerAdapter adapter;

    // Constructor for ChatFragment
    public ChatFragment() {
    }

    // Override the onCreateView method to inflate the fragment's layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment_chat layout
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        // Find the RecyclerView in the layout
        recyclerView = view.findViewById(R.id.recyler_view);
        // Set up the RecyclerView
        setupRecyclerView();

        return view;
    }

    // Method to set up the RecyclerView
    void setupRecyclerView() {
        // Define a query to retrieve chatrooms where the current user is a participant
        Query query = FirebaseUtil.allChatroomCollectionReference()
                .whereArrayContains("userIds", FirebaseUtil.currentUserId())
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING);

        // Set up FirestoreRecyclerOptions for the adapter
        FirestoreRecyclerOptions<ChatroomModel> options = new FirestoreRecyclerOptions.Builder<ChatroomModel>()
                .setQuery(query, ChatroomModel.class).build();

        // Create and set up the RecentChatRecyclerAdapter
        adapter = new RecentChatRecyclerAdapter(options, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    // Override the onStart method to start listening for changes when the fragment is visible
    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    // Override the onStop method to stop listening for changes when the fragment is not visible
    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    // Override the onResume method to notify the adapter when the fragment is resumed
    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}