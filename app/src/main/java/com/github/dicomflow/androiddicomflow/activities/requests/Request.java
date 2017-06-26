package com.github.dicomflow.androiddicomflow.activities.requests;

public class Request {
    public String id;
    public String content;
    public String details;

    public Request() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Request(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }
}