package com.github.dicomflow.androiddicomflow.models.protocolo.services;

import com.github.dicomflow.androiddicomflow.models.protocolo.Action;

/**
 * Created by ricardobarbosa on 15/06/17.
 */

public class Sharing extends Service {
    public Sharing() {
        super("Sharing", "");
        addAction(new PutAction(this));
        addAction(new ResultAction(this));
    }

    protected class PutAction extends Action {
        public PutAction(Service s) {
            super(s,"Put", "", "");
        }
    }

    protected class ResultAction extends Action {
        public ResultAction(Service s) {
            super(s,"Result", "", "");
        }
    }

}
