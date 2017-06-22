package com.github.dicomflow.androiddicomflow.models.protocolo.services;

import com.github.dicomflow.androiddicomflow.models.protocolo.Action;

/**
 * Created by ricardobarbosa on 15/06/17.
 */

public class Discovery extends Service {
    Discovery() {}

    @Override
    public String getName() {
        return "DISCOVERY";
    }

    @Override
    public String getAction() {
        return null;
    }

    protected static class VerifyAllServicesAction extends Action {
        public VerifyAllServicesAction() {
            super();
        }

        @Override
        public String getName() {
            return "VerifyAllServicesAction".toUpperCase();
        }
    }

    protected static class VerifyServicesAction extends Action {
        public VerifyServicesAction() {
            super();
        }

        @Override
        public String getName() {
            return "VerifyServicesAction".toUpperCase();
        }
    }

    protected static class VerifyResultAction extends Action {
        public VerifyResultAction() {
            super();
        }

        @Override
        public String getName() {
            return "VerifyResultAction".toUpperCase();
        }
    }

}
