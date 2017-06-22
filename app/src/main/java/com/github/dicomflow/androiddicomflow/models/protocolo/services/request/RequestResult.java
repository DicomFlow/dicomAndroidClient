package com.github.dicomflow.androiddicomflow.models.protocolo.services.request;

import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Result;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by netolucena on 22/06/2017.
 */

public class RequestResult extends Request implements Result.IResult{
    public final RequestPut requestPut;

    @ElementList(name = "results" ) public final List<Result> results;

    public RequestResult(RequestPut requestPut, List<Result> results) {
        this.requestPut = requestPut;
        this.results = results;
    }

    @Override
    public String getAction() {
        return "RESULT";
    }

    @Override
    public String getOriginalMessageID() {
        return requestPut.messageID;
    }
}
