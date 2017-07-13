package com.github.dicomflow.androiddicomflow.activities.requests;

/**
 * Created by ricardobarbosa on 27/06/17.
 */

public class Service {
    public String messageID;
    public String timeout;
    public String timestamp;
    public String version;
    public String from;


    public Service() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Service(com.github.dicomflow.dicomflowjavalib.services.Service serviceDicom) {
        this.messageID = serviceDicom.messageID;
        this.timeout = serviceDicom.timeout;
        this.version = serviceDicom.version;
        this.timestamp = serviceDicom.timestamp;
        this.from = serviceDicom.from;
    }

    @Override
    public String toString() {
        return messageID;
    }
}
