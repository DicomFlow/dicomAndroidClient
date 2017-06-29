package com.github.dicomflow.androiddicomflow.protocolo.services;

import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Completed;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Credentials;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Data;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Patient;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Result;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Serie;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Study;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Url;
import com.github.dicomflow.androiddicomflow.protocolo.services.request.RequestPut;
import com.github.dicomflow.androiddicomflow.protocolo.services.request.RequestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by netolucena on 26/06/2017.
 */

public class ServiceFactory {
    public static Service getService(ServiceTypes serviceType, Map<String, Object> params) {
        Service service = null;
        try {
            if (serviceType == ServiceTypes.REQUESTPUT) {
                String from = (String)params.get("from");
                String requestType = (String)params.get("requestType");
                Url url = (Url) params.get("url");
                service = new RequestPut(from, requestType, url);
            } else if (serviceType == ServiceTypes.REQUESTRESULT) {
                ArrayList<Patient> patients = new ArrayList<Patient>();
                ArrayList<Study> studies = new ArrayList<>();
                List<Serie> series = new ArrayList<>();
                series.add(new Serie("1", "", "", 1));
                studies.add(new Study("1", "", "", 1, 1l, series));
                patients.add(new Patient("", "", "", "", studies));
                Credentials credentials = new Credentials("Credential");
                Url url = new Url("www.com...", credentials, patients);
                List<Url> urls = new ArrayList<Url>();
                urls.add(url);

                String from = (String) params.get("from");
                String filename = (String) params.get("filename");
                String bytes = (String) params.get("bytes");
                Data data = new Data(filename, bytes);

                Completed completed = new Completed("1", "OK"); //TODO
                Integer id = 1; //TODO
                Result result = new Result(completed, data, "1234", "1234", urls);
                List resultList = new ArrayList<>();
                resultList.add(result);
                service = new RequestResult(from, resultList);
            }
        } catch (Exception e) {
            return null;
        }
        return service;
    }

}
