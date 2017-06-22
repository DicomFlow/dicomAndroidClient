package com.github.dicomflow.androiddicomflow.models.protocolo.services;

import com.github.dicomflow.androiddicomflow.models.protocolo.Action;

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
    public final List<Action> ACTIONS = new ArrayList<Action>();
    public final Map<String, Service> STRING_SERVICO_HASH_MAP = new HashMap<String, Service>();
    public final Map<String, List<Action>> STRING_SERVICE_LIST_OF_ACTION_HASH_MAP = new HashMap<String, List<Action>>();
    public final Map<String, Action> STRING_ACTION_HASH_MAP = new HashMap<String, Action>();

    private DicomFlowProtocol(){
//        Service service = new CertificateRequest(new RequestAction(new Domain("meu.com")));
//        addService(service);
//        addAction(service.getAction());
//        service = new CertificateRequest(new ResultAction());
//        addAction(service.getAction());
//        service = new CertificateRequest(new ConfirmAction());
//        addAction(service.getAction());

//        service = new Storage();
//        addService(service);
//        addAction(new Storage.SaveAction());
//        addAction(new Storage.UpdateAction());
//        addAction(new Storage.DeleteAction());
//        addAction(new Storage.ResultAction());
//
//        service = new Sharing();
//        addService(service);
//        addAction(new Sharing.PutAction());
//        addAction(new Sharing.ResultAction());
//
//        service = new Request();
//        addService(service);
//        addAction(new Request.PutAction());
//        addAction(new Request.ResultAction());
//
//        service = new Find();
//        addService(service);
//        addAction(new Find.PutAction());
//        addAction(new Find.ResultAction());
//
//        service = new Discovery();
//        addService(service);
//        addAction(new Discovery.VerifyAllServicesAction());
//        addAction(new Discovery.VerifyServicesAction());
//        addAction(new Discovery.VerifyResultAction());
    }

    public static DicomFlowProtocol getInstance() {
        if (instance == null) {
            instance = new DicomFlowProtocol();
        }

        return instance;
    }

    private void addService(Service service) {
        SERVICOS.add(service);
        STRING_SERVICO_HASH_MAP.put(service.name, service);
    }
    protected void addAction(Action action) {
        ACTIONS.add(action);
        STRING_ACTION_HASH_MAP.put(action.getName(), action);
        if(STRING_SERVICE_LIST_OF_ACTION_HASH_MAP.get(action.service.name) == null) {
            STRING_SERVICE_LIST_OF_ACTION_HASH_MAP.put(action.service.name, new ArrayList<Action>());
        }
        STRING_SERVICE_LIST_OF_ACTION_HASH_MAP.get(action.service.name).add(action);
    }


}
