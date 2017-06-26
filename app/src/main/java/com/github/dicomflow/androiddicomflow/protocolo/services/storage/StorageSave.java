package com.github.dicomflow.androiddicomflow.protocolo.services.storage;

import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Url;

import org.simpleframework.xml.Element;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class StorageSave extends Storage {

    @Element public final Url url;

    public StorageSave(Url url){
        super("SAVE");
        this.url = url;
    }



}
