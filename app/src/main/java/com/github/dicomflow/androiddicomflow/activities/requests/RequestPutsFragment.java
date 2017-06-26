package com.github.dicomflow.androiddicomflow.activities.requests;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

public class RequestPutsFragment extends RequestListFragment {

    public RequestPutsFragment() {}

    @Override
    public Query getQuery(FirebaseDatabase databaseReference) {
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference requestsUserRef = databaseReference.getReference("user-requests").child(user.getUid()).child("puts");
        Query recentRequestQuery = requestsUserRef.limitToFirst(100);
        return recentRequestQuery;
    }
}