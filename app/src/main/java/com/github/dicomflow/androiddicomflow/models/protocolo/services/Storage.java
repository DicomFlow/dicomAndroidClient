package com.github.dicomflow.androiddicomflow.models.protocolo.services;

import com.github.dicomflow.androiddicomflow.models.protocolo.Action;

/**
 * Created by ricardobarbosa on 15/06/17.
 */

public class Storage extends Service {
    public Storage() {
        super("Storage", "");
        addAction(new SaveAction(this));
        addAction(new UpdateAction(this));
        addAction(new DeleteAction(this));
        addAction(new ResultAction(this));
    }

    protected class SaveAction extends Action {
        public SaveAction(Service s) {
            super(s,"Save", "", "");
        }
    }

    protected class UpdateAction extends Action {
        public UpdateAction(Service s) {
            super(s,"Update", "", "");
        }
    }

    protected class DeleteAction extends Action {
        public DeleteAction(Service s) {
            super(s,"Delete", "", "");
        }
    }

    protected class ResultAction extends Action {
        public ResultAction(Service s) {
            super(s,"Result", "", "");
        }
    }
}
