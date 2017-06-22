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
public class Credentials {
    @Element() public final String value;

    public Credentials(@Element(name = "value") String value) {
        this.value = value;
    }
}
