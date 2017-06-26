package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by ricardobarbosa on 21/06/17.
 */
@Root
public class Url{
    @Attribute(name = "value") public final String value;
    @Element public final Credentials credentials;
    @ElementList(name = "patients", inline = true) public final List<Patient> patients;

    public Url(@Attribute(name = "value") String value,
               @Element(name="credentials") Credentials credentials,
               @ElementList(name="patients", inline = true) List<Patient> patients) {
        this.value = value;
        this.credentials = credentials;
        this.patients = patients;
    }
}
