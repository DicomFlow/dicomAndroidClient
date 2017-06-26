package com.github.dicomflow.androiddicomflow.protocolo.services.certificate;

import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Domain;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Mail;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Port;

import org.simpleframework.xml.Element;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class CertificateConfirm extends Certificate {

    @Element public final String credential;
    @Element public final String status;

    public CertificateConfirm(Domain domain, Mail mail, Port port, String credential, String status) {
        super("CONFIRM", domain,mail,port);
        this.credential = credential;
        this.status = status;
    }

}
