package com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by ricardobarbosa on 21/06/17.
 */
@Root
public class Url extends ValueObject{
    @Element public final Credentials credentials;
    @ElementList(name = "patient", inline = true) public final List<Patient> patients;

    public Url(String value, Credentials credentials, List<Patient> patients) {
        super(value);
        this.credentials = credentials;
        this.patients = patients;
    }
}
