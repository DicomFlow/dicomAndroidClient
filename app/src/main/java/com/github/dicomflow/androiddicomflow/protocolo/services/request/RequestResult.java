package com.github.dicomflow.androiddicomflow.protocolo.services.request;

import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Result;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by netolucena on 22/06/2017.
 */
@Root(name = "service")
public class RequestResult extends Request{

    @ElementList(name = "results", inline = true) public final List<Result> results;

    public RequestResult(
            String from,
            @ElementList(name = "results", inline = true) List<Result> results) {
        super("RESULT", from);
        this.results = results;
    }

    public RequestResult(@Attribute(name = "name") String name,
                         @Attribute(name = "action") String action,
                         String from,
                         @Attribute(name = "version") String version,
                         @Element(name = "timeout") String timeout,
                         @Element(name = "timestamp") String timestamp,
                         @Element(name = "messageID")String messageID,
                         @ElementList(name = "results", inline = true) List<Result> results) {
        super(name, action, from, version, timeout, timestamp, messageID);
        this.results = results;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();

        Map<String, Object> mapList = new HashMap<String, Object>();
        for (Result o : results) mapList.put(o.id.toString(), o.toMap());
        map.put("results", mapList);
        return map;
    }


}
