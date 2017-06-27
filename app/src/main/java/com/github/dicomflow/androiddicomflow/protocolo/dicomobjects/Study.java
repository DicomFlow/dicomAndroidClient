package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ricardobarbosa on 21/06/17.
 */

@Root
public class Study {
    @Attribute public final String id;
    @Attribute public final String type;
    @Attribute public final String description;
    @Attribute public final Integer datetime;
    @Attribute public final Long size;

    @ElementList(name = "series", inline = true) public final List<Serie> series;

    public Study(@Attribute(name="id") String id,
                 @Attribute(name="type") String type,
                 @Attribute(name="description") String description,
                 @Attribute(name="datetime") Integer datetime,
                 @Attribute(name="size") Long size,
                 @ElementList(name = "series", inline = true) List<Serie> series) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.datetime = datetime;
        this.size = size;
        this.series = series;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("type", type);
        map.put("description", description);
        map.put("datetime", datetime);
        map.put("size", size);
        Map<String, Object> mapList = new HashMap<String, Object>();
        for (Serie o : series) mapList.put(o.id, o.toMap());
        map.put("series", mapList);
        return map;
    }
}
