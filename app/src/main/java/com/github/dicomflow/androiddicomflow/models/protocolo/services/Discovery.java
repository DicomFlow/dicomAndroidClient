package com.github.dicomflow.androiddicomflow.models.protocolo.services;

import com.github.dicomflow.androiddicomflow.models.protocolo.Action;

/**
 * Created by ricardobarbosa on 15/06/17.
 */

public class Discovery extends Service {
    public Discovery() {
        super("Discovery", "");
        addAction(new VerifyAllServicesAction(this));
        addAction(new VerifyServicesAction(this));
        addAction(new VerifyResultAction(this));
    }

    protected class VerifyAllServicesAction extends Action {
        public VerifyAllServicesAction(Service s) {
            super(s,"VerifyAllServicesAction", "", "");
        }
    }

    protected class VerifyServicesAction extends Action {
        public VerifyServicesAction(Service s) {
            super(s,"VerifyServicesAction", "", "");
        }
    }

    protected class VerifyResultAction extends Action {
        public VerifyResultAction(Service s) {
            super(s,"VerifyResultAction", "", "");
        }
    }

}
