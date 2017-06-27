package com.github.dicomflow.androiddicomflow.protocolo.services.sharing;

import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Result;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class SharingResult extends Sharing {

    @ElementList(name = "results", inline = true) public final List<Result> results;

    public SharingResult(@Attribute(name = "from") String from, List<Result> results){
        super("RESULT", from);
        this.results = results;
    }

}
