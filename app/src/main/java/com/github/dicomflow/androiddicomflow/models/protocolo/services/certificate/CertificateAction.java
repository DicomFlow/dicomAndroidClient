package com.github.dicomflow.androiddicomflow.models.protocolo.services.certificate;

import com.github.dicomflow.androiddicomflow.models.protocolo.Action;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;

public abstract class CertificateAction extends Action {

    public CertificateAction(Service service, String name, String description, String details) {
        super(service, name, description, details);
    }
}

