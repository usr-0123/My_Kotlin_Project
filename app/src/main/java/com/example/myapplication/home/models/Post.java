package com.example.myapplication.home.models;

import java.util.List;

public class Post {
    private String message;
    private String userEmail;
    private long datetime;
    private List<String> attachmentURLs;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public List<String> getAttachmentURLs() {
        return attachmentURLs;
    }

    public void setAttachmentURLs(List<String> attachmentURLs) {
        this.attachmentURLs = attachmentURLs;
    }
}
