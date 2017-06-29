package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricardobarbosa on 21/06/17.
 */

@Root
public class Serie {
    @Attribute public final String id;
    @Attribute public final String bodypart;
    @Attribute public final String description;
    @Attribute public final Integer instances;

    public Serie(@Attribute(name = "id") String id,
                 @Attribute(name = "bodypart") String bodypart,
                 @Attribute(name = "description") String description,
                 @Attribute(name = "instances") Integer instances){
        this.id = id;

        this.bodypart = bodypart;
        this.description = description;
        this.instances = instances;
    }

    public Serie(Map<String, Object> params) {
        this.id = (String) params.get("id");
        this.bodypart = (String) params.get("bodypart");
        this.description = (String) params.get("description");
        this.instances = ((Long) params.get("instances")).intValue();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("bodypart", bodypart);
        map.put("description", description);
        map.put("instances", instances);
        return map;
    }
}
