package com.github.dicomflow.androiddicomflow.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.request.Request;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.request.RequestPut;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.request.RequestResult;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.DicomFlowXmlSerializer;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.ActionDescriptor;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Completed;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Credentials;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Data;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.DicomObject;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.FieldDescriptor;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Patient;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Result;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Serie;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.ServiceDescriptor;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Study;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.dicomobjects.Url;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** Certificate */
//        Service service = new CertificateRequest(
//                new Domain("google.com"),
//                new Mail("rbrico@gmail.com"),
//                new Port("32")
//        );
//
//        Service service = new CertificateConfirm(
//                new Domain("google.com"),
//                new Mail("rbrico@gmail.com"),
//                new Port("32"),
//                "my credentials",
//                "success"
//        );
//        Service service = new CertificateResult(
//                new Domain("google.com"),
//                new Mail("rbrico@gmail.com"),
//                new Port("32"),
//                "my credentials",
//                "success"
//        );
        /** STORAGE */
        /** SAVE **/

        ArrayList<Patient> patients = new ArrayList<Patient>();
        ArrayList<Study> studies = new ArrayList<>();
        List<Serie> series = new ArrayList<>();
        series.add(new Serie("1", "bodypart", "description", 1));
        studies.add(new Study("1","tipo","descricao do estudo", 1, 1l, series));
        studies.add(new Study("2","tipo","descricao do estudo 2", 2, 2l, series));
        patients.add(new Patient("053", "ricardo", "M", "31/10/1985", studies));
        patients.add(new Patient("054", "maria", "F", "31/10/1980", studies));
        Credentials credentials = new Credentials("valor de credential 1");
        Url url = new Url("www.com...", credentials, patients);
//        Service service = new StorageSave(url);


        /** UPDATE **/
        DicomObject dicomObject = new DicomObject(credentials, "i", DicomObject.Type.Study.name());
//        Service service = new StorageUpdate(url, dicomObject);

        /** DELETE **/
        List<DicomObject> objects =  new ArrayList<>();
        objects.add(dicomObject);
        DicomObject dicomObject2 = new DicomObject(credentials, "3", DicomObject.Type.Instance.name());
        objects.add(dicomObject2);
        //Service service = new StorageDelete(objects);
        
        String requestType = "REPORT";
//        Service service = new RequestPut(requestType, url);


        RequestPut requestPut = new RequestPut(RequestResult.RequestType.Report.name(), url);

        //simular recebimento do xml por email
//        Service serviceXml = DicomFlowXmlSerializer.deserialize("/storage/emulated/0/DicomFiles/request_PUT.xml");
//
//        Service service = new RequestResult((RequestPut) serviceXml, results);
        List<Result> results = new ArrayList<>();
        List<ServiceDescriptor> serviceDescriptors = new ArrayList<>();
        List<ActionDescriptor> actionsDescritors = new ArrayList<>();
        List<FieldDescriptor> fieldDescritors = new ArrayList<>();
        FieldDescriptor fieldDescritor = new FieldDescriptor("name", "status");
        fieldDescritors.add(fieldDescritor);
        ActionDescriptor actionsDescritor= new ActionDescriptor("name", "status", fieldDescritors);
        actionsDescritors.add(actionsDescritor);
        ServiceDescriptor serviceDescriptor = new ServiceDescriptor("name", "status", actionsDescritors);
        serviceDescriptors.add(serviceDescriptor);
        String timestamp = "asasasa";
        List<Url> urls = new ArrayList<>();
        urls.add(url);
        Completed completed = new Completed(Completed.Status.SUCCESS.name(), "Sucesso!!!");
        Data data = new Data("", "");
        Result result = new Result(
                completed,
                data,
                requestType,
                objects,
                requestPut.messageID,
                serviceDescriptors,
                timestamp,
                urls);
        results.add(result);

        Service service = new RequestResult(results);


        /*************/
        String xmlString = DicomFlowXmlSerializer.serialize(requestPut);
        ((TextView) findViewById(R.id.text)).setText(xmlString);

        Serializer serializer = new Persister();
        File file = new File("/storage/emulated/0/DicomFiles/request_PUT.xml");
        try {
            RequestPut requestPut1 = serializer.read(RequestPut.class, file);
            Snackbar.make(getWindow().getDecorView().getRootView(), requestPut1.name, Snackbar.LENGTH_INDEFINITE).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Serializer serializer = new Persister();
//        File file = new File("/storage/emulated/0/DicomFiles/request_RESULT.xml");
//        try {
//            RequestResult requestResult1 = serializer.read(RequestResult.class, file);
//            Snackbar.make(getWindow().getDecorView().getRootView(), requestResult1.name, Snackbar.LENGTH_INDEFINITE).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        Serializer serializer = new Persister();
//        File file = new File("/storage/emulated/0/DicomFiles/Result.xml");
//        try {
//            Result result1 = serializer.read(Result.class, file);
//            Snackbar.make(getWindow().getDecorView().getRootView(), result1.originalMessageID, Snackbar.LENGTH_INDEFINITE).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
