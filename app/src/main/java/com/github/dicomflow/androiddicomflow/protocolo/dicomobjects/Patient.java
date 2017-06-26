package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

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
}
