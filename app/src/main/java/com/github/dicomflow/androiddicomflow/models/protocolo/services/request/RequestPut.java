package com.github.dicomflow.androiddicomflow.models.protocolo.services.request;

import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Url;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by netolucena on 22/06/2017.
 */

@Root(name = "service")
public class RequestPut extends Request {

    @Element(name = "requestType") public final String requestType;
    @Element(name = "url") public final Url url;

    public RequestPut(@Element(name = "requestType") String requestType,
                      @Element(name = "url") Url url) {
        super("PUT");
        this.requestType = requestType;
        this.url = url;
    }

}
