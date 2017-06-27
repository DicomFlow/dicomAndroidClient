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
public class Patient {
    @Attribute public final String id;
    @Attribute public final String name;
    @Attribute public final String gender;
    @Attribute public final String birthdate;

    @ElementList(name = "study", inline = true) public final List<Study> studies;

    public Patient(@Attribute(name = "id") String id,
                   @Attribute(name = "name") String name,
                   @Attribute(name = "gender") String gender,
                   @Attribute(name = "birthdate") String birthdate,
                   @ElementList(name = "study", inline = true) List<Study> studies) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
        this.studies = studies;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("gender", gender);
        map.put("birthdate", birthdate);
        Map<String, Object> mapList = new HashMap<String, Object>();
        for (Study p : studies) mapList.put(p.id, p.toMap());
        map.put("studies", mapList);
        return map;
    }
}
