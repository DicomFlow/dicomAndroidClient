package com.github.dicomflow.androiddicomflow.models.protocolo.services.sharing;

import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Domain;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Mail;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Port;

import org.simpleframework.xml.Element;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public abstract class Sharing extends Service {
    public Sharing(){}

    @Override
    public String getName() {
        return "SHARING";
    }

}
