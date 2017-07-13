package com.github.dicomflow.androiddicomflow.activities.requests;

import com.github.dicomflow.androiddicomflow.protocolo.services.Service;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricardobarbosa on 28/06/17.
 */

public class DatabaseUtil {

    public static void writeNewService(String userId, Service service, Map<String, Object> params) {
        String where = String.format("user-%ss%s", service.name.toLowerCase(), service.action.toUpperCase());

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child(where);
        String messageID = service.messageID;
        String path1 = String.format("/%s/%s", userId, messageID);

        Map<String, Object> serviceValues = service.toMap();

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                serviceValues.put(key, params.get(key));
            }
        }

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(path1, serviceValues);
        rootRef.updateChildren(childUpdates);
    }

    public static DatabaseReference getService(String userId, String messageID) {
//        String where = String.format("user-%ss%s", service.name.toLowerCase(), service.action.toUpperCase());
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("user-requestsPUT");
        return rootRef.child(userId).child(messageID);
    }

    public static DatabaseReference getRequestResultService(String userId, String messageID) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("user-requestsRESULT");
        return rootRef.child(userId);
    }

}
