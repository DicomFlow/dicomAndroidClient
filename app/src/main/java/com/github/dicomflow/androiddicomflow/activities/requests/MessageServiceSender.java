package com.github.dicomflow.androiddicomflow.activities.requests;

import android.content.Context;
import android.support.annotation.NonNull;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.protocolo.DicomFlowXmlSerializer;
import com.github.dicomflow.androiddicomflow.protocolo.services.Service;

/**
 * Created by ricardobarbosa on 27/06/17.
 */

public class MessageServiceSender {
    private Context context;
    private BackgroundMail.OnSuccessCallback onSuccessCallback;
    private BackgroundMail.OnFailCallback onFailCallback;
    private String mailto;
    private Service service;

    private MessageServiceSender(Context context) {
        this.context = context;
    }

    public static MessageServiceSender newBuilder(Context context) {
        return new MessageServiceSender(context);
    }

    public MessageServiceSender withService(Service service) {
        this.service = service;
        return this;
    }

    public MessageServiceSender withMailto(@NonNull String mailto) {
        this.mailto = mailto;
        return this;
    }

    public MessageServiceSender withOnSuccessCallback(BackgroundMail.OnSuccessCallback onSuccessCallback) {
        this.onSuccessCallback = onSuccessCallback;
        return this;
    }

    public MessageServiceSender withOnFailCallback(BackgroundMail.OnFailCallback onFailCallback) {
        this.onFailCallback = onFailCallback;
        return this;
    }

    public BackgroundMail send() throws Exception {
        String filePath = DicomFlowXmlSerializer.serialize(service);
        return BackgroundMail.newBuilder(context)
                .withUsername(R.string.gmail_from_configuracao)
                .withPassword(R.string.gmail_pass_configuracao)
                .withMailto(mailto)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("Request Put")
                .withBody("this is the body")
                .withAttachments(filePath)
                .withOnSuccessCallback(onSuccessCallback)
                .withOnFailCallback(onFailCallback)
                .send();
    }
}
