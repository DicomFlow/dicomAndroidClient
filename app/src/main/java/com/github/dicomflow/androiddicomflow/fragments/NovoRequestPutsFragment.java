package com.github.dicomflow.androiddicomflow.fragments;

import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.requests.RequestListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class NovoRequestPutsFragment extends NovoRequestListFragment {

    public NovoRequestPutsFragment() {}

    @Override
    public Query getQuery(FirebaseDatabase databaseReference) {
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference requestsUserRef = databaseReference.getReference(getString(R.string.db_url_user_request_put)).child(user.getUid());
        Query recentRequestQuery = requestsUserRef
                .orderByChild("segundaOpiniaoDe")
                .equalTo(null) //garanto que nao tras as segundas opinioes
                .limitToFirst(10);
        return recentRequestQuery;
    }
}