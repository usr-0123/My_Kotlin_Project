package com.example.myapplication.home.ui.threads;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.home.ui.threads.adapter.CommentAdapter;
import com.example.myapplication.home.ui.threads.model.Comment;
import com.example.myapplication.home.ui.threads.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    private TextView contentTextView;
    private ImageView postImageView;
    private TextView timestampTextView;
    private RecyclerView commentsRecyclerView;
    private EditText commentEditText;
    private TextView addCommentButton;

    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        contentTextView = findViewById(R.id.contentTextView);
        postImageView = findViewById(R.id.postImageView);
        timestampTextView = findViewById(R.id.timestampTextView);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentEditText = findViewById(R.id.commentEditText);
        addCommentButton = findViewById(R.id.addCommentButton);

        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        commentsRecyclerView.setAdapter(commentAdapter);

        String postId = getIntent().getStringExtra("postId");

        databaseReference = FirebaseDatabase.getInstance().getReference("posts").child(postId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                if (post != null) {
                    contentTextView.setText(post.content);
                    if (post.imageUrl != null && !post.imageUrl.isEmpty()) {
                        Glide.with(PostDetailActivity.this).load(post.imageUrl).into(postImageView);
                    }
                    String formattedTimestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date(post.timestamp));
                    timestampTextView.setText(formattedTimestamp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        DatabaseReference commentsReference = databaseReference.child("comments");
        commentsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentEditText.getText().toString().trim();
                if (!commentText.isEmpty()) {
                    String commentId = commentsReference.push().getKey();
                    Comment comment = new Comment(commentId, "userId", postId, commentText, System.currentTimeMillis());
                    assert commentId != null;
                    commentsReference.child(commentId).setValue(comment);
                    commentEditText.setText("");
                }
            }
        });
    }
}
