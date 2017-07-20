package com.github.dicomflow.androiddicomflow.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.GenericFragment;
import com.github.dicomflow.androiddicomflow.activities.requests.DatabaseUtil;
import com.github.dicomflow.androiddicomflow.activities.requests.MessageServiceSender;
import com.github.dicomflow.androiddicomflow.activities.requests.Request;
import com.github.dicomflow.androiddicomflow.util.FileUtil;
import com.github.dicomflow.androiddicomflow.util.criptografia.DecoradorServicoAssinado;
import com.github.dicomflow.androiddicomflow.util.criptografia.EncriptaDecriptaRSA;
import com.github.dicomflow.dicomflowjavalib.FactoryDicomFlowObjects;
import com.github.dicomflow.dicomflowjavalib.FactoryService;
import com.github.dicomflow.dicomflowjavalib.dicomobjects.Completed;
import com.github.dicomflow.dicomflowjavalib.dicomobjects.Data;
import com.github.dicomflow.dicomflowjavalib.dicomobjects.Result;
import com.github.dicomflow.dicomflowjavalib.services.Service;
import com.github.dicomflow.dicomflowjavalib.services.request.RequestPut;
import com.github.dicomflow.dicomflowjavalib.services.request.RequestResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tooltip.Tooltip;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class NovoRequestListFragment extends GenericFragment {

    private static final String TAG = "RequestListFragment";
    private static final int CONTACT_PICKER_RESULT = 1000;
    private static final int REPORT_PICKER_RESULT = 2000;

    private FirebaseDatabase mDatabase;

    private FirebaseRecyclerAdapter<Request, NovoRequestPutViewHolder> mAdapter;
    private FirebaseRecyclerAdapter<Request, RequestPutSegundaOpiniaoViewHolder> mAdapterParaRequestsPutsDeSegudnaOpiniao;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private int index;

    public NovoRequestListFragment() {}

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
        mAdapter = new FirebaseRecyclerAdapter<Request , NovoRequestPutViewHolder>(Request.class, R.layout.novo_requests_list_content, NovoRequestPutViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final NovoRequestPutViewHolder requestViewHolder, final Request model, final int position) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                requestViewHolder.bindToPost(model, position);

                requestViewHolder.buttonImagens.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO implementar codigo de DOWNLOAD aqui
                        Toast.makeText(v.getContext(), "Viasualizar as imagens aqui", Toast.LENGTH_SHORT).show();
                    }
                });
                requestViewHolder.buttonEnviarLaudo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        index = (int) v.getTag();
                        Intent reportPickerIntent = new Intent(Intent.ACTION_GET_CONTENT, ContactsContract.Contacts.CONTENT_URI);
                        reportPickerIntent.setType("application/pdf");
                        startActivityForResult(reportPickerIntent, REPORT_PICKER_RESULT);
                    }
                });
                requestViewHolder.buttonVerSegundasOpinioes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        index = (int) v.getTag();
                        Request item = mAdapter.getItem(index);
                        DatabaseReference requestsUserRef = FirebaseDatabase.getInstance()
                                .getReference(getString(R.string.db_url_user_request_put))
                                .child(getUid());
                        Query recentRequestQuery = requestsUserRef
                                .orderByChild("segundaOpiniaoDe")
                                .equalTo(item.messageID) //segundas opinioes sao dadas para essa mensagem
                                .limitToFirst(10);

                        mAdapterParaRequestsPutsDeSegudnaOpiniao = new FirebaseRecyclerAdapter<Request, RequestPutSegundaOpiniaoViewHolder>(Request.class, R.layout.requests_list_content_para_segunda_opiniao, RequestPutSegundaOpiniaoViewHolder.class, recentRequestQuery) {
                            @Override
                            protected void populateViewHolder(final RequestPutSegundaOpiniaoViewHolder requestPutSegundaOpiniaoViewHolder, final Request model, final int position) {
                                final DatabaseReference certificateRef = getRef(position);
                                requestPutSegundaOpiniaoViewHolder.bindToPost(model, position);
                                requestPutSegundaOpiniaoViewHolder.buttonVerLaudo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (model.segundaOpiniaoStatus != null && model.segundaOpiniaoStatus.equals(getString(R.string.request_put_status_aguardando_segunda_opiniao))) {
                                            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                                                    .setCancelable(true)
                                                    .setDismissOnClick(false)
                                                    .setCornerRadius(20f)
                                                    .setGravity(Gravity.BOTTOM)
                                                    .setText("Aguardando laudo.");
                                            builder.show();
                                        }
                                        else {
                                            //TODO NETO implementar a visualizacao do laudo aqui
                                            Toast.makeText(v.getContext(), "NETO implementar a visualizacao do laudo aqui", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        };
                        LinearLayoutManager mManager = new LinearLayoutManager(getContext());
                        mManager.setReverseLayout(true);
                        mManager.setStackFromEnd(true);
                        new MaterialDialog.Builder(getContext())
                                .title("Segundas opiniões")
                                .positiveText("Pedir 2a opinião")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
                                    }
                                })
                                // second parameter is an optional layout manager. Must be a LinearLayoutManager or GridLayoutManager.
                                .adapter(mAdapterParaRequestsPutsDeSegudnaOpiniao, mManager)
                                .show();
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //region contact picker result para quando vai pedir segunda opiniao
        if (requestCode == CONTACT_PICKER_RESULT && resultCode == getActivity().RESULT_OK) {
            Cursor cursor = null;
            try {
                Uri result = data.getData();
                Log.v(TAG, "Got a contact result: " + result.toString());


                String id = result.getLastPathSegment();// get the contact id from the Uri

                // query for everything email
                cursor = getContext().getContentResolver().query( ContactsContract.CommonDataKinds.Email.CONTENT_URI,  null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                int nameId = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME); //o nome do contato
                int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA); //o email

                // let's just get the first email
                if (cursor.moveToFirst()) {
                    String email = cursor.getString(emailIdx);
                    solicitarSegundaOpiniao(email, getView());
                } else {
                    Toast.makeText(getContext(), "Nenhum resultado retornado", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Failed to get email data", Toast.LENGTH_SHORT).show();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        //endregion

        if (requestCode == REPORT_PICKER_RESULT && resultCode == getActivity().RESULT_OK) {
            if (index >= 0) {
                final Request r =  mAdapter.getItem(index);
                DatabaseReference ref = DatabaseUtil.getService(getUid(), r.messageID, getContext());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {

                            Map<String, Object> paramsDaRequestPut = (Map<String, Object>) dataSnapshot.getValue();


                            FactoryDicomFlowObjects factoryDicomFlowObjects = FactoryDicomFlowObjects.getInstance();

                            //region preparando o Completed.java
                            Map<String, Object> paramsParaCompleted = new HashMap<>();
                            paramsParaCompleted.put("status", Completed.Status.SUCCESS.name());
                            paramsParaCompleted.put("completedMessage", "completedMessage...");
//                            Completed completed = (Completed) factoryDicomFlowObjects.getDicomFlowObjects(Completed.class, paramsParaCompleted);

                            //endregion

                            //region preparando o Data.java
                            Uri uri = data.getData();
                            String filePath = FileUtil.getPath(getContext(), uri);
                            String fileName = FileUtil.getFileNameFromFilePath(filePath);

                            Map<String, Object> paramsParaData = new HashMap<>();
                            paramsParaData.put("bytes", FileUtil.encodeFileToBase64Binary(filePath));
                            paramsParaData.put("filename", fileName);
//                            Data data = (Data) factoryDicomFlowObjects.getDicomFlowObjects(Data.class, paramsParaData);
                            //endregion

                            //region preparando a lista de urls que tem os pacientes a partir da mensagem original
                            Map<String, Object> paramsParaUrl = (Map<String, Object>) paramsDaRequestPut.get("url");
                            List<Map<String, Object>> urls = new ArrayList<>();
                            urls.add(paramsParaUrl);
                            //endregion

                            //region preparando a lista de results
                            Map<String, Object> paramsParaResult = new HashMap<>();
//                            paramsParaResult.put("completed", completed.toMap());
                            paramsParaResult.put("completed", paramsParaCompleted);
//                            paramsParaResult.put("data", data.toMap());
                            paramsParaResult.put("data", paramsParaData);
                            paramsParaResult.put("originalMessageID", r.messageID);
                            paramsParaResult.put("timestamp",  new SimpleDateFormat("yyyy-MM-DD hh:mm:ssZ").format(new Date()));
                            paramsParaResult.put("urls", urls);
//                            Result result = (Result) factoryDicomFlowObjects.getDicomFlowObjects(Result.class, paramsParaResult);
                            List<Map<String, Object>> results = new ArrayList<>();
                            results.add(paramsParaResult);
                            //endregion

                            //region preparando o servico request result
                            Map<String, Object> paramsParaRequestResult =  new HashMap<>();
                            paramsParaRequestResult.put("results", results);
                            paramsParaRequestResult.put("from", getEmail());
                            paramsParaRequestResult.put("originalMessageID", r.messageID);
                            FactoryService factoryService = FactoryService.getInstance();
                            final Service service = factoryService.getService(RequestResult.class, paramsParaRequestResult);
                            //endregion

                            //region enviando o email
                            MessageServiceSender.newBuilder(getContext())
                                    .withService(service)
                                    .withMailto(r.from) //TODO trocar pelo from
                                    .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                        @Override
                                        public void onSuccess() {
                                            Map<String, Object> params = new HashMap<>();
                                            DatabaseUtil.writeNewService( getUid() , service, params);
                                            Snackbar.make(getView(), "Laudo enviado. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
                                    })
                                    .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                        @Override
                                        public void onFail() {
                                            Snackbar.make(getView(), "Não conseguimos enviar o email. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
                                    })
                                    .send("[from app] Request Result");
                            //endregion
                        } catch (Exception e) {
                            e.printStackTrace();
                            Snackbar.make(getView(), "Ocorreu um erro no envio. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        } catch (FactoryService.ServiceObjectException e) {
                            e.printStackTrace();
                            Snackbar.make(getView(), "Ocorreu um erro na fabrica de servico. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Snackbar.make(getView(), "Cancelled. sOcorreu um erro no envio. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                });

            }
        }
    }

    private void solicitarSegundaOpiniao(final String email, final View view) {

        final Request request = mAdapter.getItem(index);

        //TODO FABRICA AQUI - pedir a fabrica um request
        DatabaseReference ref = DatabaseUtil.getService(getUid(), request.messageID, getContext());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> params = (Map<String, Object>) dataSnapshot.getValue();
                params.put("from", getEmail());
                try {
                    final Service requestPutSegundaOpiniao = FactoryService.getInstance().getService(RequestPut.class, params);

                    // Verifica se já existe um par de chaves, caso contrário gera-se as chaves..
                    if (!EncriptaDecriptaRSA.verificaSeExisteChavesNoSO(getContext())) {
                        // Método responsável por gerar um par de chaves usando o algoritmo RSA e
                        // armazena as chaves nos seus respectivos arquivos.
                        EncriptaDecriptaRSA.geraChave(getContext());
                    }
                    PrivateKey privateKey = EncriptaDecriptaRSA.getMyPrivateKey(getContext());
                    final DecoradorServicoAssinado decoradorServicoAssinado = new DecoradorServicoAssinado(requestPutSegundaOpiniao, privateKey);

                    MessageServiceSender.newBuilder(getContext())
                            .withService(requestPutSegundaOpiniao)
                            .withMailto(email)
                            .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                @Override
                                public void onSuccess() {
                                    //do some magic
                                    String userId = getUid();
                                    Map<String, Object> params = new HashMap<>();

                                    params.put("segundaOpiniaoDe", request.messageID);
                                    params.put("segundaOpiniaoPara", email);
                                    params.put("segundaOpiniaoStatus", getString(R.string.request_put_status_aguardando_segunda_opiniao));

                                    DatabaseUtil.writeNewService(userId, decoradorServicoAssinado, params);

                                    Snackbar.make(view, "Segunda opinião solicitada. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            })
                            .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                @Override
                                public void onFail() {
                                    Snackbar.make(view, "Não conseguimos enviar o email. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            })
                            .send("Request Put");
                    Snackbar.make(view, "Houve um erro na assinatura do seriço. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } catch (FactoryService.ServiceObjectException e) {
                    e.printStackTrace();
                    Snackbar.make(getView(), "Erro na na fabrica de servico.", Snackbar.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Snackbar.make(view, "Houve um erro na assinatura do serviço. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Snackbar.make(view, "Houve um erro na assinatura do serviço. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Snackbar.make(view, "Houve um erro na assinatura do serviço. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(getView(), "Algo deu errado na solicitacao de segunda opiniao", Snackbar.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(view.getContext(), "Errrooo", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public abstract Query getQuery(FirebaseDatabase databaseReference);

}