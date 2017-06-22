package com.github.dicomflow.androiddicomflow.models.protocolo.services.storage;

import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.DicomObject;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Url;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class StorageDelete extends Storage {

    @ElementList(name = "objects" ) public final List<DicomObject> objects;

    public StorageDelete(List<DicomObject> objects){
        super("DELETE");
        this.objects = objects;
    }


}
