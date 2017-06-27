package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", name);
        map.put("status", status);
        return map;
    }
}
