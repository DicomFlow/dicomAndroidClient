package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", name);
        map.put("status", status);
        Map<String, Object> mapList = new HashMap<String, Object>();
        for (FieldDescriptor o : fieldDescriptors) mapList.put(o.name, o.toMap());
        map.put("actionDescriptors", mapList);
        return map;
    }
}

