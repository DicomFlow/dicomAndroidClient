package com.github.dicomflow.androiddicomflow.protocolo.services.request;

import com.github.dicomflow.androiddicomflow.protocolo.services.Service;

/**
 * Created by netolucena on 22/06/17.
 */
public abstract class Request extends Service {

    public Request(String action) {
        super("REQUEST", action);
    }

    public Request(String name, String action, String version, String timeout, String timestamp, String messageID) {
        super(name, action, version, timeout, timestamp, messageID);
    }
}
