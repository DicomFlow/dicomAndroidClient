package com.github.dicomflow.androiddicomflow.activities.requests;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.login.GoogleSignInActivity;
import com.github.dicomflow.androiddicomflow.activities.login.GoogleSignInActivity2;
import com.github.dicomflow.androiddicomflow.activities.outros.BaseActivity;
import com.github.dicomflow.androiddicomflow.activities.outros.FileChooser;
import com.github.dicomflow.androiddicomflow.protocolo.DicomFlowXmlSerializer;
import com.github.dicomflow.androiddicomflow.protocolo.services.Service;
import com.github.dicomflow.androiddicomflow.util.FileUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.net.URISyntaxException;

public class RequestsListActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private static final int REPORT_PICKER_RESULT_FOR_REQUEST_PUT = 2000;

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLaunchReportPicker();
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
            startActivity(new Intent(this, GoogleSignInActivity2.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REPORT_PICKER_RESULT_FOR_REQUEST_PUT && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                String filePath = FileUtil.getPath(getBaseContext(), uri);

                Log.d(TAG, "File Uri: " + uri.toString());
                Log.d(TAG, "File Path: " + filePath);

                Service service = DicomFlowXmlSerializer.deserialize(filePath);
                DatabaseUtil.writeNewService(getUid(), service, null);
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar.make(getWindow().getDecorView().getRootView(), "Erro.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void doLaunchReportPicker() {
        Intent reportPickerIntent = new Intent(Intent.ACTION_GET_CONTENT, ContactsContract.Contacts.CONTENT_URI);
        reportPickerIntent.setType("text/xml");
        startActivityForResult(reportPickerIntent, REPORT_PICKER_RESULT_FOR_REQUEST_PUT);
    }
}