package com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects;

import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by ricardobarbosa on 21/06/17.
 */

@Root
public class Study {
    @Attribute public final String id;
    @Attribute public final String type;
    @Attribute public final String description;
    @Attribute public final Integer datetime;
    @Attribute public final Long size;

    @ElementList(name = "serie", inline = true) public final List<Serie> series;

    public Study(String id, String type, String description, Integer datetime, Long size, List<Serie> series) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.datetime = datetime;
        this.size = size;
        this.series = series;
    }
}
