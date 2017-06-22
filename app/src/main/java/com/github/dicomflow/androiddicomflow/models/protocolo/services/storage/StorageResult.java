package com.github.dicomflow.androiddicomflow.models.protocolo.services.storage;

import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.DicomObject;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Result;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class StorageResult extends Storage {

    @ElementList(name = "objects" ) public final List<DicomObject> objects;
    @ElementList(name = "results" ) public final List<Result> results;

    public StorageResult(List<DicomObject> objects, List<Result> results){
        this.objects = objects;
        this.results = results;
    }

    @Override
    public String getAction() {
        return "RESULT";
    }

}
