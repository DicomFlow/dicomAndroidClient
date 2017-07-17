package com.github.dicomflow.androiddicomflow.activities.requests;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.dicomflowjavalib.utils.DicomFlowXmlSerializer;
import com.github.dicomflow.dicomflowjavalib.services.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ricardobarbosa on 27/06/17.
 */

public class MessageServiceSender {
    private Context context;
    private BackgroundMail.OnSuccessCallback onSuccessCallback;
    private BackgroundMail.OnFailCallback onFailCallback;
    private String mailto;
    private Service service;
    private ArrayList<String> attachments = new ArrayList<>();

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

    public MessageServiceSender withAttachments(@NonNull ArrayList<String> attachments) {
        this.attachments.addAll(attachments);
        return this;
    }

    public MessageServiceSender withAttachments(String... attachments) {
        this.attachments.addAll(Arrays.asList(attachments));
        return this;
    }


    public BackgroundMail send(String subject) throws Exception {
        File root = new File(Environment.getExternalStorageDirectory(), "DicomFiles");

        //Esse anexo sempre vai
        String filePath = DicomFlowXmlSerializer.getInstance().serialize(service, root);
        attachments.add(filePath);

        return BackgroundMail.newBuilder(context)
                .withUsername(R.string.gmail_from_configuracao)
                .withPassword(R.string.gmail_pass_configuracao)
                .withMailto(mailto)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("[from app] " + subject)
                .withBody("this is the body")
                .withAttachments(attachments)
                .withOnSuccessCallback(onSuccessCallback)
                .withOnFailCallback(onFailCallback)
                .send();
    }
}
