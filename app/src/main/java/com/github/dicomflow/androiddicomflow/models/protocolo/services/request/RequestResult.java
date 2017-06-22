package com.github.dicomflow.androiddicomflow.models.protocolo.services.request;

import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Result;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by netolucena on 22/06/2017.
 */

public class RequestResult extends Request {

    @ElementList(name = "results" ) public final List<Result> results;

    public RequestResult(List<Result> results) {
        this.results = results;
    }

    @Override
    public String getAction() {
        return "RESULT";
    }

}
