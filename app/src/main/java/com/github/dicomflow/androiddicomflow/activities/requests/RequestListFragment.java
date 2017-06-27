package com.github.dicomflow.androiddicomflow.activities.requests;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.dicomflow.androiddicomflow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public abstract class RequestListFragment extends Fragment {

    private static final String TAG = "RequestListFragment";
    private static final int CONTACT_PICKER_RESULT = 1000;

    private FirebaseDatabase mDatabase;

    private FirebaseRecyclerAdapter<Request, RequestPutViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

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
                requestViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), RequestsDetailActivity.class);
                        intent.putExtra(RequestsDetailFragment.ARG_ITEM_ID, postKey);
                        startActivity(intent);
                    }
                });

                requestViewHolder.bindToPost(model);

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
                        doLaunchContactPicker(v);
                    }
                });
                requestViewHolder.iconicsImageViewReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO implementar codigo de RESULT aqui
                        Toast.makeText(v.getContext(), "Implement", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    public void doLaunchContactPicker(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        contactPickerIntent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTACT_PICKER_RESULT && resultCode == getActivity().RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS};
            Cursor cursor = getContext().getContentResolver().query(contactUri, projection, null, null, null);

            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                String number = cursor.getString(numberIndex);
                // Do something with the phone number
                Toast.makeText(getContext(), number, Toast.LENGTH_SHORT).show();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
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