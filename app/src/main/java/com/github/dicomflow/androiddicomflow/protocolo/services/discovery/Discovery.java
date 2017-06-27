package com.github.dicomflow.androiddicomflow.protocolo.services.discovery;

import com.github.dicomflow.androiddicomflow.protocolo.services.Service;

import org.simpleframework.xml.Element;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public abstract class Discovery extends Service {

    @Element public final int priority;
    @Element public final String timezone;

    public Discovery(String action, String from, int priority, String timezone) {
        super("DISCOVERY", action, from);
        this.priority = priority;
        this.timezone = timezone;
    }

}
