package com.github.dicomflow.androiddicomflow.models.protocolo.services.discovery;

import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Search;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public abstract class Discovery extends Service {

    @Element public final int priority;
    @Element public final String timezone;

    public Discovery(int priority, String timezone) {
        this.priority = priority;
        this.timezone = timezone;
    }

    @Override
    public String getName() {
        return "DISCOVERY";
    }

}
