package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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
