package com.github.dicomflow.androiddicomflow.protocolo.dicomobjects;

import org.simpleframework.xml.Attribute;

/**
 * Created by ricardobarbosa on 21/06/17.
 */

public abstract class ValueObject {
    @Attribute(name = "value") public final String value;

    public ValueObject(@Attribute(name = "value") String value) {
        this.value = value;
    }
}
