package com.github.dicomflow.androiddicomflow.activities.certificate;

import com.github.dicomflow.androiddicomflow.activities.requests.Service;

public class Certificate extends Service{
    public String id;
    public String certificateFilePath;
    public String status;

    public Certificate() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Certificate(String id, String certificateFilePath, String status) {
        this.id = id;
        this.certificateFilePath = certificateFilePath;
        this.status = status;
    }

    @Override
    public String toString() {
        return from;
    }
}