package com.github.dicomflow.androiddicomflow.activities.certificate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.activity.MainActivity;
import com.github.dicomflow.androiddicomflow.activities.outros.BaseActivity;
import com.github.dicomflow.androiddicomflow.activities.requests.DatabaseUtil;
import com.github.dicomflow.androiddicomflow.activities.requests.MessageServiceSender;
import com.github.dicomflow.androiddicomflow.activities.requests.MessageServiceSenderCriptografado;
import com.github.dicomflow.androiddicomflow.util.FileUtil;
import com.github.dicomflow.androiddicomflow.util.criptografia.EncriptaDecriptaRSA;
import com.github.dicomflow.dicomflowjavalib.FactoryService;
import com.github.dicomflow.dicomflowjavalib.services.Service;
import com.github.dicomflow.dicomflowjavalib.services.ServiceIF;
import com.github.dicomflow.dicomflowjavalib.services.certificate.CertificateRequest;
import com.github.dicomflow.dicomflowjavalib.services.certificate.CertificateResult;
import com.github.dicomflow.dicomflowjavalib.utils.DicomFlowXmlSerializer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class NewCertificateReceiverFullscreenActivity extends BaseActivity {

    private static final int REPORT_PICKER_CER_CERTIFICATE_REQUEST = 1000;
    private final Map<String, Object> params = new HashMap<>();
    private Service service = null;

    //region controle de full screen
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private TextView mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    //endregion



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //region controle de full screen
        setContentView(R.layout.activity_new_certificate_receiver_fullscreen);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = (TextView) findViewById(R.id.message_from);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        //endregion

        if (getIntent().getExtras().containsKey("filePath"))  {

            try {
                String filePath = getIntent().getStringExtra("filePath");
                service = DicomFlowXmlSerializer.getInstance().deserialize(filePath);
                prepareUI();
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar.make(getWindow().getDecorView().getRootView(), "2 Erro.", Snackbar.LENGTH_SHORT).show();
            }
        }
        else {
            finish();
        }

    }

    private void prepareUI() {

        switch (service.type) {
            case ServiceIF.CERTIFICATE_REQUEST:
                params.put("status", getString(R.string.certificate_status_aguardando_enviar_confirmacao_minha));
                params.put("pendente", true);
                mContentView.setText(service.from);
                ((TextView) findViewById(R.id.info)).setText(String.format(getString(R.string.info_certificate_request), service.from));
                break;
            case ServiceIF.REQUEST_PUT:
                mContentView.setText(service.from);
                ((TextView) findViewById(R.id.info)).setText("Você tem uma nova solicitação de laudo");
//                ((TextView) findViewById(R.id.info)).setText(String.format(getString(R.string.info_certificate_request), serviceDecorado.from));
                break;
            case ServiceIF.REQUEST_RESULT:
                mContentView.setText(service.from);
                ((TextView) findViewById(R.id.info)).setText("O laudo chegou");
//                ((TextView) findViewById(R.id.info)).setText(String.format(getString(R.string.info_certificate_request), serviceDecorado.from));
                break;
        }

        findViewById(R.id.buttonAceitar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (service.type) {
                    case ServiceIF.CERTIFICATE_REQUEST:
                        //metodo enviando certificado .cer
                        if (false) {
                            //TODO abrir o diretorio e selecionar certificado dele
                            Intent reportPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            reportPickerIntent.setType("*/*");
                            startActivityForResult(reportPickerIntent, REPORT_PICKER_CER_CERTIFICATE_REQUEST);
                        }
                        //metodo que envia a minha chave publica
                        else {
                            enviarCertificateResultComMinhaChavePublica((CertificateRequest) service);
                        }

                        return;
                    case ServiceIF.REQUEST_PUT:
                        DatabaseUtil.writeNewService(getUid(), service, params);
                        onBackPressed();
                        return;
                    case ServiceIF.REQUEST_RESULT:
                        DatabaseUtil.writeNewService(getUid(), service, params);
                        onBackPressed();
                        return;

                }

            }
        });
    }

    //region Enviando um certificate result com chave publica
    private void enviarCertificateResultComMinhaChavePublica(CertificateRequest certificateRequestOriginal) {
        try {

            Map<String, Object> params = new HashMap<>();
            params.put("from", getEmail());
            params.put("mail", getEmail());
            params.put("port", getPortDefault());
            params.put("domain", getDomainDefault());
            params.put("publickey", Base64.encodeToString(EncriptaDecriptaRSA.getMyPublicKey(this).getEncoded(), Base64.DEFAULT));

            final CertificateResult certificateResult = (CertificateResult) FactoryService.getInstance().getService(CertificateResult.class, params);
            MessageServiceSender o = MessageServiceSender.newBuilder(this)
                    .withMailto(certificateRequestOriginal.from) //email de quem pediu
                    .withService(certificateResult)
                    .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(NewCertificateReceiverFullscreenActivity.this, "Resposta a troca de certificado enviado com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                        @Override
                        public void onFail() {
                            Toast.makeText(NewCertificateReceiverFullscreenActivity.this, "Falha no envio do email", Toast.LENGTH_SHORT).show();
                        }
                    });

            MessageServiceSenderCriptografado messageServiceSenderCriptografado = new MessageServiceSenderCriptografado(this, o);
            messageServiceSenderCriptografado.send("CERTIFICATE REQUEST");

            DatabaseUtil.writeNewService(getUid(), certificateResult, null);
        } catch (FactoryService.ServiceObjectException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro na fabrica de servico", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao anexar minha chave publica", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao anexar minha chave publica", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(NewCertificateReceiverFullscreenActivity.this, "Erro ao enviar o email", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    //endregion

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REPORT_PICKER_CER_CERTIFICATE_REQUEST && resultCode == RESULT_OK) {
            //TODO falta a criptografia

            //TODO preciso validar o certificado aqui
            Uri uri = data.getData();
            String filePath = FileUtil.getPath(this, uri);
            params.put("fileCertificatePath", filePath);

            DatabaseUtil.writeNewService(getUid(), service, params);
            onBackPressed();
        }
    }

    //region Controle de full screen
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    //endregion
}
