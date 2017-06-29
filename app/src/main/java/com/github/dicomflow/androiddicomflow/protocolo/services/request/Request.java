package com.github.dicomflow.androiddicomflow.protocolo.services.request;

import com.github.dicomflow.androiddicomflow.protocolo.services.Service;

import java.util.Map;

/**
 * Created by netolucena on 22/06/17.
 */
public abstract class Request extends Service {

    public Request(String action, String from) {
        super("REQUEST", action, from);
    }

    public Request(String name, String action, String from, String version, String timeout, String timestamp, String messageID) {
        super(name, action, from, version, timeout, timestamp, messageID);
    }

    public Request(Map<String, Object> params) {
        super(params);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        return map;
    }
}
