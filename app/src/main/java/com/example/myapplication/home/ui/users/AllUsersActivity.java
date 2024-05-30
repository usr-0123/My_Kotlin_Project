// AllUsersActivity.java
package com.example.myapplication.home.ui.users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.home.ui.users.chats.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

public class AllUsersActivity extends AppCompatActivity {

    private static final String TAG = "UsersActivity";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private LinearLayout usersContainer;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        usersContainer = findViewById(R.id.usersContainer);

        // Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get the currently logged-in user's ID
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser != null ? currentUser.getUid() : null;

        // Fetch users from Firestore
        fetchUsers();
    }

    private void fetchUsers() {
        if (currentUserId == null) {
            Log.e(TAG, "No logged-in user.");
            return;
        }

        CollectionReference usersRef = db.collection("users");

        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, "Checking user ID: " + document.getId());
                    if (!document.getId().equals(currentUserId)) {
                        Map<String, Object> user = document.getData();
                        Log.d(TAG, "Adding user: " + user.get("firstName") + " " + user.get("lastName"));
                        addUserToView(document.getId(), user);
                    } else {
                        Log.d(TAG, "Omitting logged-in user: " + document.getId());
                    }
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    private void addUserToView(String userId, Map<String, Object> user) {
        // Create a container for each user
        LinearLayout userContainer = new LinearLayout(this);
        userContainer.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        userContainer.setOrientation(LinearLayout.HORIZONTAL);
        userContainer.setPadding(16, 16, 16, 16);

        // Create an ImageView for the profile picture
        ImageView profileImageView = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(200, 200);
        profileImageView.setLayoutParams(imageParams);

        // Load the profile picture using Glide
        String profileImageUrl = (String) user.get("profileImageUrl");
        Glide.with(this).load(profileImageUrl).into(profileImageView);

        // Create a TextView for the user info
        TextView userView = new TextView(this);
        userView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        userView.setPadding(16, 16, 16, 16);

        String userInfo = user.get("firstName") + " " + user.get("lastName") + "\n" + user.get("email");
        userView.setText(userInfo);

        // Add the ImageView and TextView to the user container
        userContainer.addView(profileImageView);
        userContainer.addView(userView);

        // Set an OnClickListener to open the ChatActivity
        userContainer.setOnClickListener(v -> {
            Intent intent = new Intent(AllUsersActivity.this, ChatActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("userName", user.get("firstName") + " " + user.get("lastName"));
            startActivity(intent);
        });

        // Add the user container to the main users container
        usersContainer.addView(userContainer);

        // Create and add a divider view
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                1 // 1dp height for the divider
        ));
        divider.setBackgroundResource(R.drawable.divider);
        usersContainer.addView(divider);
    }
}