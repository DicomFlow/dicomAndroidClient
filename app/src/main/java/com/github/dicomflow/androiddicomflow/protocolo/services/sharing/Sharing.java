package com.github.dicomflow.androiddicomflow.protocolo.services.sharing;

import com.github.dicomflow.androiddicomflow.protocolo.services.Service;

/**
 * Created by ricardobarbosa on 15/06/17.
 */
public abstract class Sharing extends Service {
    public Sharing(String action){
        super("SHARING", action);
    }
}
