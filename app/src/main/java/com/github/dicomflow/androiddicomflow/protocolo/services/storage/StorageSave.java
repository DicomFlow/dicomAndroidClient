package com.github.dicomflow.androiddicomflow.protocolo.services.storage;

import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Url;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class StorageSave extends Storage {

    @Element public final Url url;

    public StorageSave(@Attribute(name = "from") String from, Url url){
        super("SAVE", from);
        this.url = url;
    }



}
