package com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by ricardobarbosa on 21/06/17.
 */

@Root
public class Credentials {
    @ElementList(name = "value", inline = true, entry = "value") public final List<String> values;

    public Credentials(List<String> values) {
        this.values = values;
    }
}
