package com.github.dicomflow.androiddicomflow.activities.outros;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.protocolo.DicomFlowXmlSerializer;
import com.github.dicomflow.androiddicomflow.protocolo.services.Service;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ReceiveXmlFileActivity extends BaseActivity {

    public static final String TAG = "ReceiveXmlFileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_xml_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Service service = deserializeXML();
        salveNobanco(service);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deserializeXML();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void salveNobanco(final Service service) {
        //root
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        final String userId = getUid();
        database.child("users").child(userId).child("info").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Map<String, Object> userInfo = (Map<String, Object>) dataSnapshot.getValue();

                        // [START_EXCLUDE]
                        if (userInfo.isEmpty()) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(ReceiveXmlFileActivity.this, "Error: could not fetch user.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewService(userId, service);
                        }

                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });


    }

    private void writeNewService(String userId, Service service) {

        // Create new request at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        //root
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        String key = service.messageID;
        String path1 = String.format("/%s/%s/%s/%s", service.name.toLowerCase(), service.action.toLowerCase(), userId, key);
        String path2 = String.format("/user-%s/%s/%s/%s", service.name.toLowerCase(), userId, service.action.toLowerCase(), key);

        Map<String, Object> postValues = service.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(path1, postValues);
        childUpdates.put(path2, postValues);
        rootRef.updateChildren(childUpdates);

    }

    private Service deserializeXML() {
        File root = new File(Environment.getExternalStorageDirectory(), "DicomFiles");
        //TODO remover o hardcode daqui nos atibutos de serivce e action
        File xmlFile = new File(root, String.format("%s_%s.xml", "request", "PUT"));

        Service service = DicomFlowXmlSerializer.deserialize(xmlFile.getAbsolutePath());
        return service;
    }

}
