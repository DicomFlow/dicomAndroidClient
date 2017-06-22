package com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by ricardobarbosa on 22/06/17.
 */
@Root
public class ActionDescriptor {
    @Attribute public final String name;
    @Attribute public final String status;
    @ElementList(name = "actionDescriptor") public final List<FieldDescriptor> fieldDescriptors;

    public ActionDescriptor(@Attribute(name = "name") String name,
                            @Attribute(name = "status") String status,
                            @ElementList(name = "actionDescriptor") List<FieldDescriptor> fieldDescriptors) {
        this.name = name;
        this.status = status;
        this.fieldDescriptors = fieldDescriptors;
    }
}

