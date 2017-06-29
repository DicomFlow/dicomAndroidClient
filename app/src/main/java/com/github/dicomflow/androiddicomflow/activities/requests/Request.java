package com.github.dicomflow.androiddicomflow.activities.requests;

public class Request extends Service {

    public Request() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    enum Status {
        enviada, lida, encaminhada, respondida
    }

//    private com.github.dicomflow.androiddicomflow.protocolo.services.request.RequestPut requestPut;
//    public Request(com.github.dicomflow.androiddicomflow.protocolo.services.request.RequestPut requestPut) {
//        super(requestPut);
//        this.requestPut = requestPut;
//    }

}