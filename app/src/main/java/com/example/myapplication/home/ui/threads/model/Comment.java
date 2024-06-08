package com.example.myapplication.home.ui.threads.model;

public class Comment {
    public String commentId;
    public String userId;
    public String postId;

    public String content;
    public long timestamp;

    // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    public Comment() {
    }

    public Comment(String commentId, String userId, String postId, String content, long timestamp) {
        this.commentId = commentId;
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters and setters
}
