package com.example.myapplication.home;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.home.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private List<String> postList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ListView listView = findViewById(R.id.listView);
        postList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, postList);
        listView.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("reports");

        fetchPosts(databaseReference);
    }

    private void fetchPosts(DatabaseReference databaseReference) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        String formattedPost = formatPost(post);
                        postList.add(formattedPost);
                    }
                }

                if (postList.isEmpty()) {
                    postList.add("No data available");
                }

                runOnUiThread(() -> adapter.notifyDataSetChanged());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Failed to load posts.", Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addValueEventListener(postListener);
    }

    private String formatPost(Post post) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = sdf.format(new Date(post.getDatetime()));

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
