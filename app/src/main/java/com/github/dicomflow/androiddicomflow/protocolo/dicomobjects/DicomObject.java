package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricardobarbosa on 21/06/17.
 */

@Root(name = "object")
public class DicomObject {
    public static enum Type {
        Study, Serie, Instance
    }
    @Element(name = "credential") public final Credentials credentials;
    @Attribute public final String id;
    @Attribute public final String type;

    public DicomObject(@Element(name = "credential") Credentials credentials,
                       @Attribute(name = "id") String id,
                       @Attribute(name = "type") String type) {
        this.credentials = credentials;
        this.id = id;
        this.type = type;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("type", type);
        map.put("credentials", credentials.toMap());
        return map;
    }


}
