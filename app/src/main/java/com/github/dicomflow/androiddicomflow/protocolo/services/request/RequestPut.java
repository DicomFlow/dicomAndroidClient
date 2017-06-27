package com.github.dicomflow.androiddicomflow.protocolo.services.request;

import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Url;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by netolucena on 22/06/2017.
 */

@Root(name = "service")
public class RequestPut extends Request {

    public enum RequestType { Report, Processing }

    @Element(name = "requestType") public final String requestType;
    @Element(name = "url") public final Url url;

    public RequestPut(@Attribute(name = "from") String from,
                      @Element(name = "requestType") String requestType,
                      @Element(name = "url") Url url) {
        super("PUT", from);
        this.requestType = requestType;
        this.url = url;
    }

    public RequestPut(@Attribute(name = "name") String name,
                      @Attribute(name = "action") String action,
                      @Attribute(name = "from") String from,
                      @Attribute(name = "version") String version,
                      @Element(name = "timeout") String timeout,
                      @Element(name = "timestamp") String timestamp,
                      @Element(name = "messageID")String messageID,
                      @Element(name = "requestType") String requestType,
                      @Element(name = "url") Url url) {
        super(name, action, from, version, timeout, timestamp, messageID);
        this.requestType = requestType;
        this.url = url;
    }
}
