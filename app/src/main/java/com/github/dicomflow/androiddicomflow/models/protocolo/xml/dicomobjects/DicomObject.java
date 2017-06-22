package com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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
}
