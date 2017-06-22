package com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by ricardobarbosa on 22/06/17.
 */
@Root
public class FieldDescriptor {
    @Attribute public final String name;
    @Attribute public final String status;

    public FieldDescriptor(@Attribute(name = "name") String name,
                           @Attribute(name = "status")String status) {
        this.name = name;
        this.status = status;
    }
}
