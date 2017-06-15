package com.github.dicomflow.androiddicomflow.models.protocolo.services;

import com.github.dicomflow.androiddicomflow.models.protocolo.Action;

/**
 * Created by ricardobarbosa on 15/06/17.
 */

public class Certificate extends Service {
    public Certificate() {
        super("Certificate", "Serviço utilizado para a troca de certificados entre entidades de saude que pretendem comunicar-se para executar ações sobre imagens médicas");
        addAction(new RequestAction(this));
        addAction(new ResultAction(this));
        addAction(new ConfirmAction(this));
    }

    protected class RequestAction extends Action {
        public RequestAction(Service s) {
            super(s, "Request", "", "");
        }
    }

    protected class ResultAction extends Action {
        public ResultAction(Service s) {
            super(s, "Result", "", "");
        }
    }

    protected class ConfirmAction extends Action {
        public ConfirmAction(Service s) {
            super(s, "Confirm", "", "");
        }
    }
}
