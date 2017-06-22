package com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by ricardobarbosa on 22/06/17.
 */
@Root
public class Completed {
    @Attribute public final String status;
    @Element public final String completedMessage;

    public Completed(String status, String completedMessage) {
        this.status = status;
        this.completedMessage = completedMessage;
    }
}

