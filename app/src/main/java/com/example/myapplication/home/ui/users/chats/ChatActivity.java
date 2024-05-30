package com.example.myapplication.home.ui.users.chats;

// ChatActivity.java
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class ChatActivity extends AppCompatActivity {

    private String userId;
    private String userName;
    private TextView chatTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get the user data from the intent
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");

        chatTitle = findViewById(R.id.chatTitle);
        chatTitle.setText("Chat with " + userName);

        // TODO: Implement chat functionality
    }
}
