package com.github.dicomflow.androiddicomflow.models.protocolo.services.discovery;

import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Result;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.ServiceDescriptor;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public abstract class DiscoveryVerifyResult extends Discovery {

    @ElementList(inline = true) public final List<Result> results;

    public DiscoveryVerifyResult(int priority, String timezone, List<Result> results) {
        super("VERIFYRESULT", priority, timezone);
        this.results = results;
    }

}
