package com.github.dicomflow.androiddicomflow.activities.requests;

public class Request extends Service {

    enum Status {
        enviada, lida, encaminhada, respondida
    }

    public Request() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Request(com.github.dicomflow.androiddicomflow.protocolo.services.request.RequestPut request) {
        super(request);
    }

    @Override
    public String toString() {
        //TODO sobresver com um request info aqui
        return messageID.toString();
    }
}