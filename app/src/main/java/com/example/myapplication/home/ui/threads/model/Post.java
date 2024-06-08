package com.example.myapplication.home.ui.threads.model;

public class Post {
    public String postId;
    public String userId;
    public String content;
    public String imageUrl;

    // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    public Post() {}

    public Post(String postId, String userId, String content, String imageUrl) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
}

