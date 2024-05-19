package com.example.myapplication.home;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.home.models.Post;
import com.google.firebase.database.*;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private List<String> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ListView listView = findViewById(R.id.listView);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("reports");
        postList = new ArrayList<>();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, postList);
        listView.setAdapter(adapter);

        // Fetch data from Firebase database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear existing posts
                postList.clear();

                // Iterate through dataSnapshot and add posts to postList
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        // Format the post data as a string
                        String formattedPost = formatPost(post);
                        postList.add(formattedPost);
                    }
                }

                // Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    private String formatPost(Post post) {
        // Convert datetime to readable format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = sdf.format(new Date(post.getDatetime()));

        // Concatenate message, userEmail, datetime, and attachment URLs
        StringBuilder formattedPost = new StringBuilder();
        formattedPost.append("Message: ").append(post.getMessage()).append("\n")
                .append("User: ").append(post.getUserEmail()).append("\n")
                .append("Date: ").append(date).append("\n");

        if (post.getAttachmentURLs() != null && !post.getAttachmentURLs().isEmpty()) {
            formattedPost.append("Attachments:\n");
            for (String url : post.getAttachmentURLs()) {
                formattedPost.append(url).append("\n");
            }
        }

        return formattedPost.toString();
    }
}