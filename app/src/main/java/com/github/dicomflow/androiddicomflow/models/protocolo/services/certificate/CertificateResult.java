package com.github.dicomflow.androiddicomflow.models.protocolo.services.certificate;

import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Domain;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Mail;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Port;

import org.simpleframework.xml.Element;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class CertificateResult extends Certificate {

    @Element public final String credential;
    @Element public final String status;

    public CertificateResult(Domain domain, Mail mail, Port port, String credential, String status) {
        super(domain,mail,port);
        this.credential = credential;
        this.status = status;
    }

    @Override
    public String getAction() {
        return "RESULT";
    }

}
