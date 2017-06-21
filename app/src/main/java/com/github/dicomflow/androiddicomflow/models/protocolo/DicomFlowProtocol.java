package com.github.dicomflow.androiddicomflow.models.protocolo;

import com.github.dicomflow.androiddicomflow.models.protocolo.services.certificate.Certificate;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.Discovery;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.Find;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.Request;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.Sharing;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represetação do protocolo como uma estrutura de classes.
 */
public class DicomFlowProtocol {

    private static DicomFlowProtocol instance;

    public final List<Service> SERVICOS = new ArrayList<Service>();

    public final Map<String, Service> STRING_SERVICO_HASH_MAP = new HashMap<String, Service>();

    private DicomFlowProtocol(){
        addItem(new Certificate());
        addItem(new Storage());
        addItem(new Sharing());
        addItem(new Request());
        addItem(new Find());
        addItem(new Discovery());
    }

    public static DicomFlowProtocol getInstance() {
        if (instance == null) {
            instance = new DicomFlowProtocol();
        }

        return instance;
    }

    private void addItem(Service service) {
        SERVICOS.add(service);
        STRING_SERVICO_HASH_MAP.put(service.getName(), service);
    }

}
