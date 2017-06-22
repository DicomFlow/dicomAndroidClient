package com.github.dicomflow.androiddicomflow.models.protocolo.services.storage;

import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.DicomObject;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Url;

import org.simpleframework.xml.Element;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class StorageUpdate extends Storage {

    @Element public final Url url;
    @Element public final DicomObject object;

    public StorageUpdate(Url url, DicomObject object){
        super("UPDATE");
        this.url = url;
        this.object = object;
    }

}
