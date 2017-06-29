package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;


import android.support.annotation.NonNull;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.Diff;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Url(Map<String, Object> params) {
        this.value = (String) params.get("value");
        this.credentials = new Credentials((Map<String, Object>) params.get("credentials"));

        Map<String, Object> paramsClientes = (Map<String, Object>) params.get("pacients");
        this.patients =  new ArrayList<>();
        for (String key : paramsClientes.keySet()) {
            this.patients.add(new Patient((Map<String, Object>) paramsClientes.get(key)));
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("value", value);
        map.put("credentials", credentials.toMap());

        Map<String, Object> mapPatients = new HashMap<String, Object>();
        for (Patient p : patients) mapPatients.put(p.id, p.toMap());
        map.put("pacients", mapPatients);
        return map;
    }
}
