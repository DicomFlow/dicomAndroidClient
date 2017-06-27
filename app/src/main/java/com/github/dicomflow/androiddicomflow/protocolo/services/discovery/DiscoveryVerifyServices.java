package com.github.dicomflow.androiddicomflow.protocolo.services.discovery;

import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.ServiceDescriptor;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public abstract class DiscoveryVerifyServices extends Discovery {

    @ElementList(name = "services", inline = true) public final List<ServiceDescriptor> serviceDescriptors;

    public DiscoveryVerifyServices(String from, int priority, String timezone, List<ServiceDescriptor> serviceDescriptors) {
        super("VERIFYSERVICES", from, priority, timezone);
        this.serviceDescriptors = serviceDescriptors;
    }

}
