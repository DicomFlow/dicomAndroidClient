package com.github.dicomflow.androiddicomflow.models.protocolo.services.certificate;

import com.github.dicomflow.androiddicomflow.models.protocolo.Action;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by ricardobarbosa on 15/06/17.
 */

@Root
public class Certificate extends Service {

    @Element
    public Action theAction = null;

    public Certificate() {
        super("Certificate", "Serviço utilizado para a troca de certificados entre entidades de saude que pretendem comunicar-se para executar ações sobre imagens médicas");
        addAction(new RequestAction(this));
        addAction(new ResultAction(this));
        addAction(new ConfirmAction(this));
    }

    public Certificate(CertificateAction certificateAction) {
        this();
        theAction = certificateAction;
    }


}
