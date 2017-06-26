package com.github.dicomflow.androiddicomflow.protocolo.services.discovery;

import org.simpleframework.xml.Element;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public abstract class DiscoveryVerifyAllServices extends Discovery {

    @Element public final int detail;

    public DiscoveryVerifyAllServices(int detail, int priority, String timezone) {
        super("VERIFYALLSERVICES", priority, timezone);
        this.detail = detail;
    }


}
