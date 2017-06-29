package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricardobarbosa on 21/06/17.
 */

@Root
public class Credentials {
    @Element(name = "value") public final String value;

    public Credentials(@Element(name = "value") String value) {
        this.value = value;
    }

    public Credentials(Map<String, Object> params) {
        this.value = (String) params.get("value");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("value", value);
        return map;
    }
}
