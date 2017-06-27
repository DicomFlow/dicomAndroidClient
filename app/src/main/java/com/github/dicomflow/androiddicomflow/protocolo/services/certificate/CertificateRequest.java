package com.github.dicomflow.androiddicomflow.protocolo.services.certificate;

import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Domain;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Mail;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Port;

import org.simpleframework.xml.Attribute;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class CertificateRequest extends Certificate {

    public CertificateRequest(String from, Domain domain, Mail mail, Port port) {
        super("REQUEST", from, domain, mail, port);
    }

}
