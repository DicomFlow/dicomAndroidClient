package com.github.dicomflow.androiddicomflow.activities.requests;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class RequestPutsFragment extends RequestListFragment {

    public RequestPutsFragment() {}

    @Override
    public Query getRequestPutQuery(FirebaseDatabase databaseReference) {
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference requestsUserRef = databaseReference.getReference("user-requestsPUT").child(user.getUid());
        Query recentRequestQuery = requestsUserRef.limitToFirst(100);
        return recentRequestQuery;
    }

    @Override
    public Query getRequestResultQuery(FirebaseDatabase databaseReference) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference requestsUserRef = databaseReference.getReference("user-requestsRESULT").child(user.getUid());
        Query recentRequestQuery = requestsUserRef.limitToFirst(100);
        return recentRequestQuery;
    }
}