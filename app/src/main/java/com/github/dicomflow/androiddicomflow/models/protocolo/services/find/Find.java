package com.github.dicomflow.androiddicomflow.models.protocolo.services.find;

import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public abstract class Find extends Service {
    public Find(){}

    @Override
    public String getName() {
        return "FIND";
    }

}
