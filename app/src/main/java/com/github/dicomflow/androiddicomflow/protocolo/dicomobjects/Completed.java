package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricardobarbosa on 22/06/17.
 */
@Root
public class Completed {
    public enum Status {
        SUCCESS, ERROR
    }

    @Attribute public final String status;
    @Element public final String completedMessage;

    public Completed(@Attribute(name = "status") String status,
                     @Element(name = "completedMessage") String completedMessage) {
        this.status = status;
        this.completedMessage = completedMessage;
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("completedMessage", completedMessage);
        return map;
    }

}

