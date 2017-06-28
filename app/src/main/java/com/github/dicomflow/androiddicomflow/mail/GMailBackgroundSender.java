package com.github.dicomflow.androiddicomflow.mail;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.github.dicomflow.androiddicomflow.protocolo.DicomFlowXmlSerializer;
import com.github.dicomflow.androiddicomflow.protocolo.services.Service;

/**
 * Created by Neto on 28/06/2017.
 */

public class GMailBackgroundSender {

    public static void enviarEmailWithGmailBackground(final View v, Service service) {

        String filePath = DicomFlowXmlSerializer.serialize(service);
        BackgroundMail.newBuilder(v.getContext())
                .withUsername("dicomflow@gmail.com")
                .withPassword("pr0t0c0l0ap1d1c0m")
                .withMailto("juracylucena@gmail.com")
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("this is the subject")
                .withBody("this is the body")
                .withAttachments(filePath)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                        Snackbar.make(v, "Email Enviado ... ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                        Snackbar.make(v, "Email nao Enviado ... ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                })
                .send();
    }
}
