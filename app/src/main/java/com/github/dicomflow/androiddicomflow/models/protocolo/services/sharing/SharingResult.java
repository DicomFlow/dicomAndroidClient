package com.github.dicomflow.androiddicomflow.models.protocolo.services.sharing;

import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Result;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Url;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class SharingResult extends Sharing {

    @ElementList(name = "results", inline = true) public final List<Result> results;

    public SharingResult(List<Result> results){
        super("RESULT");
        this.results = results;
    }

}
