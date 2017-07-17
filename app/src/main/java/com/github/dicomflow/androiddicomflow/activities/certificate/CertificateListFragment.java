package com.github.dicomflow.androiddicomflow.activities.certificate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.GenericFragment;
import com.github.dicomflow.androiddicomflow.activities.activity.MainActivity;
import com.github.dicomflow.androiddicomflow.activities.requests.DatabaseUtil;
import com.github.dicomflow.androiddicomflow.activities.requests.MessageServiceSender;
import com.github.dicomflow.androiddicomflow.util.FileUtil;
import com.github.dicomflow.dicomflowjavalib.FactoryService;
import com.github.dicomflow.dicomflowjavalib.services.Service;
import com.github.dicomflow.dicomflowjavalib.services.certificate.CertificateResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CertificateListFragment extends GenericFragment {


    private static final String TAG = "CertificateListFragment";
    private static final int REPORT_PICKER_RESULT = 2000;
    private static final int REPORT_PICKER_CER_CERTIFICATE_CONFIRM = 3000;

    private FirebaseDatabase mDatabase;

    private FirebaseRecyclerAdapter<Certificate, CertificateViewHolder> mAdapter;
    private FirebaseRecyclerAdapter<Certificate, CertificateViewHolder> mAdapterParaOsPendentesDeEnvio;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private int index;

    public CertificateListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_certificate_list, container, false);

        mDatabase = FirebaseDatabase.getInstance();

        mRecycler = (RecyclerView) rootView.findViewById(R.id.certificates_list);
        mRecycler.setHasFixedSize(true);

        setHasOptionsMenu(true);

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
        Query query = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<Certificate , CertificateViewHolder>(Certificate.class, R.layout.certificate_list_content, CertificateViewHolder.class, query) {


            @Override
            protected void populateViewHolder(final CertificateViewHolder certificateViewHolder, final Certificate model, final int position) {
                final DatabaseReference certificateRef = getRef(position);

                // Set click listener for the whole post view
                final String key = certificateRef.getKey();

                /*TODO incluir depois o clique pra ver o detalhe de um certificate put
                requestViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                */

                certificateViewHolder.bindToPost(model, position);

                certificateViewHolder.imageButtonCertificateGray.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO implementar
                        index = (int) v.getTag();
                        Certificate item = mAdapter.getItem(index);
                        Toast.makeText(v.getContext(), item.certificateFilePath, Toast.LENGTH_SHORT).show();
                    }
                });
                certificateViewHolder.imageButtonCertificateGreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO implementar
                        Toast.makeText(v.getContext(), "Implement", Toast.LENGTH_SHORT).show();
                    }
                });

                certificateViewHolder.imageButtonCertificateEnviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO abrir o diretorio e enviar meu certificado
                        index = (int) v.getTag();
                        Intent reportPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        reportPickerIntent.setType("*/*");
                        startActivityForResult(reportPickerIntent, REPORT_PICKER_CER_CERTIFICATE_CONFIRM);
                    }
                });

            }
        };
        mRecycler.setAdapter(mAdapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REPORT_PICKER_CER_CERTIFICATE_CONFIRM && resultCode == FragmentActivity.RESULT_OK) {
            //TODO falta a criptografia
            if (index >= 0) {
                final Certificate certificateParaOqualEstouRespondendo =  mAdapterParaOsPendentesDeEnvio.getItem(index);
                DatabaseReference ref = DatabaseUtil.getServiceCertificateRequest(getUid(), certificateParaOqualEstouRespondendo.messageID, getContext());

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        try {
                            //String filePath = data.getData().getPath();
                            Uri uri = data.getData();
                            String filePath = FileUtil.getPath(getContext(), uri);

                            Map<String, Object> params = new HashMap<>();
                            params.put("from", getEmail());
                            params.put("mail", getEmail());
                            params.put("status", CertificateResult.Status.SUCCESS.name());
                            final Service service = FactoryService.getInstance().getService(CertificateResult.class, params);

                            MessageServiceSender.newBuilder(getContext())
                                    .withService(service)
                                    .withMailto(certificateParaOqualEstouRespondendo.from) //aqui eh o email de destino
                                    .withAttachments(filePath)
                                    .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                        @Override
                                        public void onSuccess() {
                                            //do some magic
                                            String userId = getUid();
                                            Map<String, Object> params = new HashMap<>();
                                            DatabaseUtil.writeNewService(userId, service, params);
                                            Snackbar.make(getView(), "[from app]Request result.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                            dataSnapshot.child("status").getRef().setValue("aguardando-receber-confirmacao-dele");
                                            dataSnapshot.child("pendente").getRef().setValue(null);
                                        }
                                    })
                                    .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                        @Override
                                        public void onFail() {
                                            //do some magic
                                            Snackbar.make(getView(), "Não conseguimos enviar o email.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
                                    })
                                    .send("Request result");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Snackbar.make(getView(), "Ocorreu um erro no envio. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        } catch (FactoryService.ServiceObjectException e) {
                            e.printStackTrace();
                            Snackbar.make(getView(), "Ocorreu um erro na fabrica. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Snackbar.make(getView(), "Cancelled. Ocorreu um erro no envio. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }

                });
            }

        }
        if (requestCode == REPORT_PICKER_RESULT && resultCode == getActivity().RESULT_OK) {
          /*  if (index >= 0) {
                final Certificate r =  mAdapter.getItem(index);
                DatabaseReference ref = DatabaseUtil.getService(getUid(), r.messageID);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> params = (Map<String, Object>) dataSnapshot.getValue();
                        params.put("from", getEmail());

                        //String filePath = data.getData().getPath();
                        Uri uri = data.getData();
                        String filePath = FileUtil.getPath(getContext(), uri);
                        String fileName = FileUtil.getFileNameFromFilePath(filePath);
                        params.put("filename", fileName);
                        params.put("bytes", filePath);
                        params.put("originalMessageID", r.messageID);

                        final Service service = ServiceFactory.getService(ServiceTypes.REQUESTRESULT, params);

                        try {
                            MessageServiceSender.newBuilder(getContext())
                                    .withService(service)
                                    .withMailto(r.from) //TODO trocar pelo from
                                    .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                        @Override
                                        public void onSuccess() {
                                            //do some magic
                                            String userId = getUid();
                                            Map<String, Object> params = new HashMap<>();

                                            DatabaseUtil.writeNewService(userId, service, params);

                                            Snackbar.make(getView(), "Segunda opinião solicitada. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
                                    })
                                    .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                        @Override
                                        public void onFail() {
                                            //do some magic
                                            Snackbar.make(getView(), "Não conseguimos enviar o email. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
                                    })
                                    .send();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Snackbar.make(getView(), "Ocorreu um erro no envio. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Snackbar.make(getView(), "Cancelled. sOcorreu um erro no envio. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }

                });
            }*/
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public Query getQuery(FirebaseDatabase databaseReference) {
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference requestsUserRef = databaseReference.getReference("user-certificate-request").child(user.getUid());
        Query recentRequestQuery = requestsUserRef.orderByChild("pendente").equalTo(null).limitToFirst(100);
        return recentRequestQuery;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case R.id.menu_certificate_messages:

                DatabaseReference requestsUserRef = FirebaseDatabase.getInstance()
                        .getReference("user-certificate-request")
                        .child(getUid());
                Query recentRequestQuery = requestsUserRef
                        .orderByChild("status")
                        .equalTo(getString(R.string.certificate_status_aguardando_enviar_confirmacao_minha))
                        .limitToFirst(100);

                mAdapterParaOsPendentesDeEnvio = new FirebaseRecyclerAdapter<Certificate , CertificateViewHolder>(Certificate.class, R.layout.certificate_list_content, CertificateViewHolder.class, recentRequestQuery) {
                    @Override
                    protected void populateViewHolder(final CertificateViewHolder certificateViewHolder, final Certificate model, final int position) {
                        final DatabaseReference certificateRef = getRef(position);
                        certificateViewHolder.bindToPost(model, position);
                        certificateViewHolder.imageButtonCertificateEnviar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                index = (int) v.getTag();
                                Intent reportPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                reportPickerIntent.setType("*/*");
                                startActivityForResult(reportPickerIntent, REPORT_PICKER_CER_CERTIFICATE_CONFIRM);
                            }
                        });
                    }
                };
                LinearLayoutManager mManager = new LinearLayoutManager(getContext());
                mManager.setReverseLayout(true);
                mManager.setStackFromEnd(true);
                new MaterialDialog.Builder(getContext())
                        .title("Aguardando meu certificado")
                        // second parameter is an optional layout manager. Must be a LinearLayoutManager or GridLayoutManager.
                        .adapter(mAdapterParaOsPendentesDeEnvio, mManager)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}