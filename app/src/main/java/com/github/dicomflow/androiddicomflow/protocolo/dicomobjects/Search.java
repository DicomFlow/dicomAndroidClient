package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by ricardobarbosa on 22/06/17.
 */

@Root
public class Search {
    @ElementList(inline = true) public final List<Patient> patients;

    public Search(@ElementList(name="patients", inline = true) List<Patient> patients) {
        this.patients = patients;
    }
}
