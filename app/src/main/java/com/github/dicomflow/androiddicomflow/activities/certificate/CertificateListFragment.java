package com.github.dicomflow.androiddicomflow.activities.certificate;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.GenericFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class CertificateListFragment extends GenericFragment {

    private static final String TAG = "CertificateListFragment";
    private static final int REPORT_PICKER_RESULT = 2000;

    private FirebaseDatabase mDatabase;

    private FirebaseRecyclerAdapter<Certificate, CertificateViewHolder> mAdapter;
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

            }
        };
        mRecycler.setAdapter(mAdapter);
    }


    public void doLaunchReportPicker(View view) {
        int index = (int) view.getTag();

        Intent reportPickerIntent = new Intent(Intent.ACTION_GET_CONTENT, ContactsContract.Contacts.CONTENT_URI);
        this.index = index;
        reportPickerIntent.setType("application/pdf");
        startActivityForResult(reportPickerIntent, REPORT_PICKER_RESULT);
    }
//tem um icone de receber certificado quando se esta aguardando certificado
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
        Query recentRequestQuery = requestsUserRef.limitToFirst(100);
        return recentRequestQuery;
    }

}