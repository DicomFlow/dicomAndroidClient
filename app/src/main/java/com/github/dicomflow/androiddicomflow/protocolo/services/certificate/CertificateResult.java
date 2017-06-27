package com.github.dicomflow.androiddicomflow.protocolo.services.certificate;

import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Domain;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Mail;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Port;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public class CertificateResult extends Certificate {

    @Element public final String credential;
    @Element public final String status;

    public CertificateResult(@Attribute(name = "from") String from, Domain domain, Mail mail, Port port, String credential, String status) {
        super("RESULT", from, domain,mail,port);
        this.credential = credential;
        this.status = status;
    }

}
