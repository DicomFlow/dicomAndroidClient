package com.github.dicomflow.androiddicomflow.models.protocolo.services.request;

import com.github.dicomflow.androiddicomflow.models.protocolo.Action;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;

/**
 * Created by netolucena on 22/06/17.
 */

public abstract class Request extends Service {

    public Request() {}

    @Override
    public String getName() {
        return "REQUEST";
    }
}
