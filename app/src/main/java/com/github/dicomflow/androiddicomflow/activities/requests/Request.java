package com.github.dicomflow.androiddicomflow.activities.requests;

public class Request extends Service {

    public String segundaOpiniaoDe;
    public String segundaOpiniaoPara;
    public String segundaOpiniaoStatus;

    public Request() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    enum Status {
        enviada, lida, encaminhada, respondida
    }

//    private com.github.dicomflow.dicomflowjavalib.services.request.RequestPut requestPut;
//    public Request(com.github.dicomflow.dicomflowjavalib.services.request.RequestPut requestPut) {
//        super(requestPut);
//        this.requestPut = requestPut;
//    }

}