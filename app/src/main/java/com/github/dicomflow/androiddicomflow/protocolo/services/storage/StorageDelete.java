package com.github.dicomflow.androiddicomflow.protocolo.services.storage;

import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.DicomObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class StorageDelete extends Storage {

    @ElementList(name = "objects" ) public final List<DicomObject> objects;

    public StorageDelete(String from, List<DicomObject> objects){
        super("DELETE", from);
        this.objects = objects;
    }


}
