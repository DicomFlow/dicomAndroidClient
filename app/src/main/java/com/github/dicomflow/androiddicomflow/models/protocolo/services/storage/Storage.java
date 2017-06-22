package com.github.dicomflow.androiddicomflow.models.protocolo.services.storage;

import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public abstract class Storage extends Service {

    public Storage() {}

    @Override
    public String getName() {
        return "STORAGE";
    }

}
