package com.github.dicomflow.androiddicomflow.models.protocolo.services.find;

import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Result;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Search;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class FindResult extends Find {

    @ElementList(name = "results", inline = true) public final List<Result> results;

    public FindResult(List<Result> results) {
        super("RESULT");
        this.results = results;
    }

}
