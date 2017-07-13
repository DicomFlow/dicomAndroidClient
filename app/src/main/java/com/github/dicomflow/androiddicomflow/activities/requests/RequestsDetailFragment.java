package com.github.dicomflow.androiddicomflow.activities.requests;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.dicomflow.androiddicomflow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A fragment representing a single Requests detail screen.
 * This fragment is either contained in a {@link RequestsListActivity}
 * in two-pane mode (on tablets) or a {@link RequestsDetailActivity}
 * on handsets.
 */
public class RequestsDetailFragment extends Fragment {

    private static final String TAG = "RequestsDetailFragment";

    private DatabaseReference userRef;
    private DatabaseReference mReference;
    private ValueEventListener mListener;

    private String mKey;

    private TextView mAuthorView;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy email this fragment is presenting.
     */
    private Request mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RequestsDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy email specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load email from a email provider.
            // Get post key from intent
            mKey = getArguments().getString(ARG_ITEM_ID);
            if (mKey == null) {
                throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
            }

            // Initialize Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            userRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
            mReference = userRef.child("requests").child(mKey);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.messageID);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.requests_detail, container, false);

        // Show the dummy email as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.requests_detail)).setText(mItem.messageID);
            // Initialize Views
            mAuthorView = (TextView) rootView.findViewById(R.id.requests_detail);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Request post = dataSnapshot.getValue(Request.class);

//                ((TextView) rootView.findViewById(R.id.requests_detail)).setText(post.certificateFilePath);
                mAuthorView.setText(post.messageID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Model failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Snackbar.make(getView(), "Failed to load post.", Snackbar.LENGTH_SHORT).show();
            }
        };
        mReference.addValueEventListener(mListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mListener != null) {
            mReference.removeEventListener(mListener);
        }
    }
}
