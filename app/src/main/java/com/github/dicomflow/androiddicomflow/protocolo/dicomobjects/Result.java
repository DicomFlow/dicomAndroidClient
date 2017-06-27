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
    @Attribute public final Integer id;
    @Element public final Completed completed;
    @Element public final Data data;
    @ElementList public final List<DicomObject> objects;
    @Element public final String originalMessageID;
    @ElementList(name = "services", inline = true, entry = "serviceDescriptor") public final List<ServiceDescriptor> serviceDescriptors;
    @Element  public final String timestamp;
    @ElementList public final List<Url> urls;

    public Result(
            @Element(name = "id" ) Integer id,
                @Element(name = "completed" ) Completed completed,
                @Element(name = "data") Data data,
                @ElementList(name = "objects")  List<DicomObject> objects,
                @Element(name = "originalMessageID" ) String originalMessageID,
                @ElementList(name = "services", inline = true, entry = "serviceDescriptor")   List<ServiceDescriptor> serviceDescriptors,
                @Element(name = "timestamp" ) String timestamp,
                @ElementList(name = "urls")   List<Url> urls) {
        this.id = id;
        this.completed = completed;
        this.data = data;
        this.objects = objects;
        this.originalMessageID = originalMessageID;
        this.serviceDescriptors = serviceDescriptors;
        this.timestamp = timestamp;
        this.urls = urls;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("completed", completed.toMap());
        map.put("data", data.toMap());
        map.put("originalMessageID", originalMessageID);
        map.put("timestamp", timestamp);


        Map<String, Object> mapList = new HashMap<String, Object>();
        for (DicomObject o : objects) mapList.put(o.id, o.toMap());
        map.put("objects", mapList);

        Map<String, Object> mapList2 = new HashMap<String, Object>();
        for (ServiceDescriptor o : serviceDescriptors) mapList2.put(o.name, o.toMap());
        map.put("serviceDescriptors", mapList2);

        Map<String, Object> mapList3 = new HashMap<String, Object>();
        for (Url o : urls) mapList3.put(o.value, o.toMap());
        map.put("urls", mapList3);

        return map;
    }

}
