package com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by ricardobarbosa on 22/06/17.
 */
@Root
public class ServiceDescriptor {
    @Attribute public final String name;
    @Attribute public final String status;
    @ElementList(name = "serviceDescriptor") public final List<ActionDescriptor> actionDescriptors;

    public ServiceDescriptor(String name, String status, List<ActionDescriptor> actionDescriptors) {
        this.name = name;
        this.status = status;
        this.actionDescriptors = actionDescriptors;
    }
}
