package com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects;

import org.simpleframework.xml.Root;

/**
 * Created by ricardobarbosa on 21/06/17.
 */
@Root
public class Domain extends ValueObject{
    public Domain(String value) {
        super(value);
    }
}
