package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

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
    @ElementList(inline = true) public final List<FieldDescriptor> fieldDescriptors;

    public ActionDescriptor(@Attribute(name = "name") String name,
                            @Attribute(name = "status") String status,
                            @ElementList(inline = true) List<FieldDescriptor> fieldDescriptors) {
        this.name = name;
        this.status = status;
        this.fieldDescriptors = fieldDescriptors;
    }
}

