package com.github.dicomflow.androiddicomflow.activities.certificate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.dicomflow.androiddicomflow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.nex3z.notificationbadge.NotificationBadge;

/**
 * An activity representing a list of Certificates. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CertificateDetailActivity} representing
 * item certificateFilePath. On tablets, the activity presents the list of items and
 * item certificateFilePath side-by-side using two vertical panes.
 */
public class CertificateListActivity extends AppCompatActivity {
    private static final int PROFILE_SETTING = 1;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    NotificationBadge mBadge;
    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate_list);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mBadge = (NotificationBadge) findViewById(R.id.badge);
        mCount = 9;
        mBadge.setNumber(mCount);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Query query = getQuery(mDatabase);
        final FirebaseRecyclerAdapter<Certificate, CertificateViewHolder> mAdapter = new FirebaseRecyclerAdapter<Certificate , CertificateViewHolder>(Certificate.class, R.layout.certificate_list_content, CertificateViewHolder.class, query) {
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

            }
        };
//        mRecycler.setAdapter(mAdapter);
        ImageButton imageButton = (ImageButton) findViewById(R.id.messages);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO open p7m file here
//                doLaunchReportPicker();
                Snackbar.make(view, "open p7m file here", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(CertificateListActivity.this, NewCertificateReceiverFullscreenActivity.class);
                startActivity(intent);
            }
        });

        if (findViewById(R.id.certificate_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

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
            CertificateListFragment fragment = new CertificateListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    public Query getQuery(FirebaseDatabase databaseReference) {
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference requestsUserRef = databaseReference.getReference("user-certificate-request").child(user.getUid());
        Query recentRequestQuery = requestsUserRef.orderByChild("status").limitToFirst(100);
        return recentRequestQuery;
    }






}
