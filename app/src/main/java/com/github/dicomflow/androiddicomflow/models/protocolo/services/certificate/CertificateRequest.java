package com.github.dicomflow.androiddicomflow.models.protocolo.services.certificate;

import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Domain;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Mail;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Port;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class CertificateRequest extends Certificate {

    public CertificateRequest(Domain domain, Mail mail, Port port) {
        super(domain,mail,port);
    }

    @Override
    public String getAction() {
        return "REQUEST";
    }

}
