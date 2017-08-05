package com.github.dicomflow.androiddicomflow.mail.tasks;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.GenericFragment;
import com.github.dicomflow.androiddicomflow.activities.outros.BaseActivity;
import com.github.dicomflow.androiddicomflow.activities.requests.DatabaseUtil;
import com.github.dicomflow.androiddicomflow.util.FileUtil;
import com.github.dicomflow.dicomflowjavalib.services.Service;
import com.github.dicomflow.dicomflowjavalib.services.certificate.CertificateRequest;
import com.github.dicomflow.dicomflowjavalib.utils.DicomFlowXmlSerializer;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {

    public interface IMakeRequestTask {
        String getQuery();
        String getUid();
    }
    public final IMakeRequestTask iMakeRequestTask;
    public final ProgressDialog mProgressDialog;
    private final Context context;
    private com.google.api.services.gmail.Gmail mService = null;
    private Exception mLastError = null;

    public MakeRequestTask(Context context, IMakeRequestTask iMakeRequestTask, GoogleAccountCredential credential, ProgressDialog mProgressDialog) {
        this.iMakeRequestTask = iMakeRequestTask;
        this.context = context;
        this.mProgressDialog = mProgressDialog;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.gmail.Gmail.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Gmail API Android Quickstart")
                .build();
    }

    /**
     * Background task to call Gmail API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected List<String> doInBackground(Void... params) {
        try {
            return listMessagesMatchingQuery();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Fetch a list of Gmail labels attached to the specified account.
     * @return List of Strings labels.
     * @throws IOException
     */
    public List<String> listMessagesMatchingQuery() throws IOException, JSONException {
        String userId = "me";
        final List<String> lid = new ArrayList<>();
        ListMessagesResponse response = mService.users().messages().list(userId)
                .setQ(iMakeRequestTask.getQuery()).setLabelIds(lid).execute();

        List<Message> messages = new ArrayList<Message>();
        messages.addAll(response.getMessages());

        BatchRequest b = mService.batch();
        JsonBatchCallback<Message> bc = new JsonBatchCallback<Message>() {
            @Override
            public void onSuccess(Message mes, HttpHeaders responseHeaders)
                    throws IOException {
                try {
                    getAttachments(mes.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders)
                    throws IOException {
                System.out.println("Error Message: " + e.getMessage());
            }
        };

        for (Message message : messages) {
            mService.users().messages().get(userId, message.getId()).setFormat("metadata").queue(b,bc);
        }
        b.execute();

        return lid;
    }

    /**
     * Get the attachments in a given email.
     *
     * can be used to indicate the authenticated user.
     * @param messageId ID of Message containing attachment..
     * @throws IOException
     */
    public void getAttachments(String messageId)
            throws Exception {

        String userId = "me";
        Message message = mService.users().messages().get(userId, messageId).execute();
        List<MessagePart> parts = message.getPayload().getParts();
        for (MessagePart part : parts) {
            if (part.getFilename() != null && part.getFilename().length() > 0) {
                String filename = part.getFilename();
                String attId = part.getBody().getAttachmentId();
                MessagePartBody attachPart = mService.users().messages().attachments().
                        get(userId, messageId, attId).execute();


                File file = new File(context.getFilesDir(), filename);
                file.createNewFile();

                Base64 base64Url = new Base64(true);
                byte[] fileByteArray = base64Url.decodeBase64(attachPart.getData());
                FileOutputStream fileOutFile = new FileOutputStream(file);
                fileOutFile.write(fileByteArray);
                fileOutFile.close();


                CertificateRequest service = (CertificateRequest) DicomFlowXmlSerializer.getInstance().deserialize(file.getAbsolutePath());
                Map<String, Object> params = new HashMap<>();
                params.put("status", context.getString(R.string.certificate_status_aguardando_enviar_confirmacao_minha));
                params.put("pendente", true);
                DatabaseUtil.writeNewService(iMakeRequestTask.getUid(), service, params);


//                // Salva a Chave Pública no arquivo
//                ObjectOutputStream chavePublicaOS = new ObjectOutputStream(new FileOutputStream(file));
//                chavePublicaOS.writeObject(key.getPublic());
//                chavePublicaOS.close();
            }
        }
    }


    @Override
    protected void onPreExecute() {
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(List<String> output) {
        mProgressDialog.hide();
        if (output == null || output.size() == 0) {
            Toast.makeText(context, "No results returned.", Toast.LENGTH_SHORT).show();
        } else {
            output.add(0, "Data retrieved using the Gmail API:");
            // TODO: 25/07/17 Atualizar a lista aqui
        }
    }

    @Override
    protected void onCancelled() {
        mProgressDialog.hide();
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                showGooglePlayServicesAvailabilityErrorDialog(
                        ((GooglePlayServicesAvailabilityIOException) mLastError)
                                .getConnectionStatusCode());
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                ((Activity) context).startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        GenericFragment.REQUEST_AUTHORIZATION);
            } else {
                Toast.makeText(context, "The following error occurred:\n"
                        + mLastError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Request cancelled.", Toast.LENGTH_SHORT).show();
        }
    }

    protected static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    protected void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                (Activity) context,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }
}