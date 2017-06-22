package com.github.dicomflow.androiddicomflow.models.protocolo;

import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

public abstract class Action {

    public Service service;

    public Action() {}

    public abstract String getName();

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}