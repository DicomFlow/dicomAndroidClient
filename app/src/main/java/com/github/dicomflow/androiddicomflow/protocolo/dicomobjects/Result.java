package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ricardobarbosa on 22/06/17.
 */

@Root
public class Result {
    @Element public final Completed completed;
    @Element public final Data data;
    @Element public final String originalMessageID;
    @Element  public final String timestamp;
    @ElementList public final List<Url> urls;

    public Result(
                @Element(name = "completed" ) Completed completed,
                @Element(name = "data") Data data,
                @Element(name = "originalMessageID" ) String originalMessageID,
                @Element(name = "timestamp" ) String timestamp,
                @ElementList(name = "urls")   List<Url> urls) {
        this.completed = completed;
        this.data = data;
        this.originalMessageID = originalMessageID;
        this.timestamp = timestamp;
        this.urls = urls;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("completed", completed.toMap());
        map.put("originalMessageID", originalMessageID);
        map.put("timestamp", timestamp);


        Map<String, Object> mapList = new HashMap<String, Object>();

        Map<String, Object> mapList2 = new HashMap<String, Object>();

        Map<String, Object> mapList3 = new HashMap<String, Object>();
        for (Url o : urls) mapList3.put(o.value, o.toMap());
        map.put("urls", mapList3);

        return map;
    }

}
