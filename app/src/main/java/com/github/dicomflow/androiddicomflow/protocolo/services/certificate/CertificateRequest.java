package com.github.dicomflow.androiddicomflow.protocolo.services.certificate;

import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Domain;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Mail;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Port;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class CertificateRequest extends Certificate {

    public CertificateRequest(Domain domain, Mail mail, Port port) {
        super("REQUEST", domain, mail, port);
    }

}
