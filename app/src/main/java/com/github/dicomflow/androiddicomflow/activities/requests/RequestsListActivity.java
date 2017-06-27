package com.github.dicomflow.androiddicomflow.activities.requests;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.login.GoogleSignInActivity;
import com.github.dicomflow.androiddicomflow.activities.outros.BaseActivity;
import com.github.dicomflow.androiddicomflow.activities.outros.ReceiveXmlFileActivity;
import com.github.dicomflow.androiddicomflow.protocolo.DicomFlowXmlSerializer;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Credentials;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Patient;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Serie;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Study;
import com.github.dicomflow.androiddicomflow.protocolo.dicomobjects.Url;
import com.github.dicomflow.androiddicomflow.protocolo.services.Service;
import com.github.dicomflow.androiddicomflow.protocolo.services.request.RequestPut;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class RequestsListActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Button launches NewPostActivity
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        startActivity(new Intent(RequestsListActivity.this, NewPostActivity.class));
                //TODO remover esse mock
//                processarXMl();
                //TODO remover esse mock
                enviarEmailWithGmailBackground(v);
            }
        });

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            RequestPutsFragment fragment = new RequestPutsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    private void processarXMl() {
        Intent intent = new Intent(this, ReceiveXmlFileActivity.class);
        startActivity(intent);
    }

    private void enviarEmailWithGmailBackground(final View v) {
        Service service = createAService();
        String filePath = DicomFlowXmlSerializer.serialize(service);

        BackgroundMail.newBuilder(this)
                .withUsername("dicomflow@gmail.com")
                .withPassword("pr0t0c0l0ap1d1c0m")
                .withMailto("rbrico@gmail.com")
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("this is the subject")
                .withBody("this is the body")
                .withAttachments(filePath)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                        Snackbar.make(v, "Email Enviado ... ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                        Snackbar.make(v, "Email nao Enviado ... ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                })
                .send();
    }

    private com.github.dicomflow.androiddicomflow.protocolo.services.Service createAService() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, GoogleSignInActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}