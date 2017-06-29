package com.github.dicomflow.androiddicomflow.activities.requests;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.dicomflow.androiddicomflow.R;

import com.github.dicomflow.androiddicomflow.mail.GMailBackgroundSender;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Completed;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Credentials;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Data;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Patient;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Result;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Serie;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Study;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Url;
import com.github.dicomflow.androiddicomflow.protocolo.services.*;
import com.github.dicomflow.androiddicomflow.protocolo.services.Service;
import com.github.dicomflow.androiddicomflow.protocolo.services.request.RequestPut;
import com.github.dicomflow.androiddicomflow.protocolo.services.request.RequestResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RequestListFragment extends Fragment {

    private static final String TAG = "RequestListFragment";
    private static final int CONTACT_PICKER_RESULT = 1000;
    private static final int REPORT_PICKER_RESULT = 2000;

    private FirebaseDatabase mDatabase;

    private FirebaseRecyclerAdapter<Request, RequestPutViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private int index;

    public RequestListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_request_list, container, false);

        mDatabase = FirebaseDatabase.getInstance();

        mRecycler = (RecyclerView) rootView.findViewById(R.id.requests_list);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<Request , RequestPutViewHolder>(Request.class, R.layout.requests_list_content, RequestPutViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final RequestPutViewHolder requestViewHolder, final Request model, final int position) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                //TODO incluir depois o clique pra ver o detalhe de um request put
//                requestViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });

                requestViewHolder.bindToPost(model, position);

                requestViewHolder.iconicsImageViewDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO implementar codigo de DOWNLOAD aqui
                        Toast.makeText(v.getContext(), "Implement", Toast.LENGTH_SHORT).show();
                    }
                });
                requestViewHolder.iconicsImageViewForward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        index = (int) v.getTag();
                        doLaunchContactPicker();
                    }
                });
                requestViewHolder.iconicsImageViewReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO implementar codigo de RESULT aqui
                        //Toast.makeText(v.getContext(), "Implement", Toast.LENGTH_SHORT).show();
                        doLaunchReportPicker(v);
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    private void doLaunchContactPicker() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }


    public void doLaunchReportPicker(View view) {
        int index = (int) view.getTag();

        Intent reportPickerIntent = new Intent(Intent.ACTION_GET_CONTENT, ContactsContract.Contacts.CONTENT_URI);
        this.index = index;
        reportPickerIntent.setType("application/pdf");
        startActivityForResult(reportPickerIntent, REPORT_PICKER_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_PICKER_RESULT && resultCode == getActivity().RESULT_OK) {

            Cursor cursor = null;
            String email = "", name = "";
            try {
                Uri result = data.getData();
                Log.v(TAG, "Got a contact result: " + result.toString());

                // get the contact id from the Uri
                String id = result.getLastPathSegment();

                // query for everything email
                cursor = getContext().getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                + " = ?", new String[]{id}, null);

                int nameId = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

                // let's just get the first email
                if (cursor.moveToFirst()) {
                    email = cursor.getString(emailIdx);
                    name = cursor.getString(nameId);
                    Log.v(TAG, "Got email: " + email);

                    try {
                        solicitarSegundaOpiniao(email, getView());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Snackbar.make(getView(), "Algo deu errado na solicitacao de segunda opiniao", Snackbar.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getContext(), email, Toast.LENGTH_SHORT).show();

                } else {
                    Log.w(TAG, "No results");
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to get email data", e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }




            /*// Get the URI and query the content provider for the email
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS};
            Cursor cursor = getContext().getContentResolver().query(contactUri, projection, null, null, null);

            // If the cursor returned is valid, get the email
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                String email = cursor.getString(numberIndex);
                try {
                    solicitarSegundaOpiniao(email, getView());
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(getView(), "Algo deu errado na solicitacao de segunda opiniao", Snackbar.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), email, Toast.LENGTH_SHORT).show();
            }
            if (cursor != null) {
                cursor.close();
            }*/
        }

        if (requestCode == REPORT_PICKER_RESULT && resultCode == getActivity().RESULT_OK) {
            if (index >= 0) {
                Request r =  mAdapter.getItem(index);

                String filePath = data.getData().getPath();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("from", "dicomflow@gmail.com");

                com.github.dicomflow.androiddicomflow.protocolo.services.Service service = ServiceFactory.getService(ServiceTypes.REQUESTRESULT, params);
                //Service service = createAService2();
                GMailBackgroundSender.enviarEmailWithGmailBackground(getView(), service);
            }
        }


    }

    protected Service createAService() {
        return null;
    }


    private void solicitarSegundaOpiniao(String email, final View view) throws Exception {

        final Request request = mAdapter.getItem(index);

        //TODO FABRICA AQUI - pedir a fabrica um request
        final Service requestPutSegundaOpiniao = mockfabrica();

        MessageServiceSender.newBuilder(getContext())
                .withService(requestPutSegundaOpiniao)
                .withMailto(email)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                        String userId = getUid();
                        Map<String, Object> params = new HashMap<>();

                        params.put("segundaOpinaoDe", request.messageID);
                        DatabaseUtil.writeNewService(userId, requestPutSegundaOpiniao, params);

                        Snackbar.make(view, "Segunda opinião solicitada. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                        Snackbar.make(view, "Não conseguimos enviar o email. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                })
                .send();

    }



    //TODO esse metodo deve sair apos a fabrica
    protected Service mockfabrica() {
        //Creating Service Object
        ArrayList<Patient> patients = new ArrayList<Patient>();
        ArrayList<Study> studies = new ArrayList<>();
        List<Serie> series = new ArrayList<>();
        series.add(new Serie("1", "bodypart", "description", 1));
        studies.add(new Study("1", "tipo", "descricao do estudo", 1, 1l, series));
        studies.add(new Study("2", "tipo", "descricao do estudo 2", 2, 2l, series));
        patients.add(new Patient("053", "ricardo", "M", "31/10/1985", studies));
        patients.add(new Patient("054", "maria", "F", "31/10/1980", studies));
        Credentials credentials = new Credentials("valor de credential 1");
        Url url = new Url("www.com...", credentials, patients);
        RequestPut requestPut = new RequestPut("dicomflow@gmail.com", "REPORT", url);
        return requestPut;
    }


    private com.github.dicomflow.androiddicomflow.protocolo.services.Service createAService2() {
        //Creating Service Object
        Completed completed = new Completed("1", "OK");
        Data data = new Data(null, "Data");

        ArrayList<Patient> patients = new ArrayList<Patient>();
        ArrayList<Study> studies = new ArrayList<>();
        List<Serie> series = new ArrayList<>();
        series.add(new Serie("1", "bodypart", "description", 1));
        studies.add(new Study("1", "tipo", "descricao do estudo", 1, 1l, series));
        studies.add(new Study("2", "tipo", "descricao do estudo 2", 2, 2l, series));
        patients.add(new Patient("053", "ricardo", "M", "31/10/1985", studies));
        patients.add(new Patient("054", "maria", "F", "31/10/1980", studies));
        Credentials credentials = new Credentials("valor de credential 1");
        Url url = new Url("www.com...", credentials, patients);
        List<Url> urls = new ArrayList<Url>();
        urls.add(url);

        Result result = new Result(completed, data, "123", "1234", urls);

        List<Result> results = new ArrayList<Result>();
        results.add(result);
        RequestResult requestResult = new RequestResult("dicomflow@gmail.com", results);

        return requestResult;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(FirebaseDatabase databaseReference);

}