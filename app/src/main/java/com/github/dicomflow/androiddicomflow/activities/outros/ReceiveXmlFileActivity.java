package com.github.dicomflow.androiddicomflow.activities.outros;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.requests.DatabaseUtil;
import com.github.dicomflow.dicomflowjavalib.utils.DicomFlowXmlSerializer;
import com.github.dicomflow.dicomflowjavalib.services.Service;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.Map;

public class ReceiveXmlFileActivity extends BaseActivity {

    public static final String TAG = "ReceiveXmlFileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_xml_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String xmlPath = getIntent().getStringExtra("filePath");
                    File xmlFile = new File(xmlPath);
                    Service service = deserializeXML(xmlFile);
                    save(service);
                    Snackbar.make(view, "Serviço salvo.", Snackbar.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void save(final Service service) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        final String userId = getUid();
        database.child("users").child(userId).child("info").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Map<String, Object> userInfo = (Map<String, Object>) dataSnapshot.getValue();
                        if (userInfo.isEmpty()) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(ReceiveXmlFileActivity.this, "Error: could not fetch user.", Toast.LENGTH_SHORT).show();
                        } else {
                            DatabaseUtil.writeNewService(userId, service, null);
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

    }

    private Service deserializeXML(File xmlFile) throws Exception {
        Service service = DicomFlowXmlSerializer.getInstance().deserialize(xmlFile.getAbsolutePath());
        return service;
    }

}
