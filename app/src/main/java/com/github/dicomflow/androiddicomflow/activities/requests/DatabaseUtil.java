package com.github.dicomflow.androiddicomflow.activities.requests;

import android.content.Context;

import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.util.criptografia.DecoradorServicoAssinado;
import com.github.dicomflow.dicomflowjavalib.services.Service;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricardobarbosa on 28/06/17.
 */

public class DatabaseUtil {

    public static void writeNewService(String userId, Service service, Map<String, Object> params) {
        String where = String.format("user-%s-%s", service.name.toLowerCase(), service.action.toLowerCase());

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

    public static DatabaseReference getService(String userId, String messageID, Context context) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.db_url_user_request_put));
        return rootRef.child(userId).child(messageID);
    }

    public static DatabaseReference getServiceCertificateRequest(String userId, String messageID, Context context) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.db_url_user_certificate_request));
        return rootRef.child(userId).child(messageID);
    }
}
