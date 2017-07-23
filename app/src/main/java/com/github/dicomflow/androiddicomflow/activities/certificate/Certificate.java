package com.github.dicomflow.androiddicomflow.activities.certificate;

import com.github.dicomflow.androiddicomflow.activities.requests.Service;

public class Certificate extends Service{
    public String id;
    public String certificateFilePath;
    public String status;
    public String to;

    public Certificate() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Certificate(String id, String certificateFilePath, String status, String to) {
        this.id = id;
        this.certificateFilePath = certificateFilePath;
        this.status = status;
        this.to = to;
    }

    @Override
    public String toString() {
        return from;
    }
}