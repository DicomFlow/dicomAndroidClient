package com.github.dicomflow.androiddicomflow.activities.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.facebook.login.LoginManager;
import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.MainActivityFragment;
import com.github.dicomflow.androiddicomflow.activities.certificate.CertificateListActivity;
import com.github.dicomflow.androiddicomflow.activities.certificate.CertificateListFragment;
import com.github.dicomflow.androiddicomflow.activities.certificate.NewCertificateReceiverFullscreenActivity;
import com.github.dicomflow.androiddicomflow.activities.login.GoogleSignInActivity2;
import com.github.dicomflow.androiddicomflow.activities.outros.BaseActivity;
import com.github.dicomflow.androiddicomflow.activities.requests.DatabaseUtil;
import com.github.dicomflow.androiddicomflow.activities.requests.MessageServiceSender;
import com.github.dicomflow.androiddicomflow.activities.requests.Request;
import com.github.dicomflow.androiddicomflow.fragments.NovoRequestPutsFragment;
import com.github.dicomflow.androiddicomflow.util.FileUtil;
import com.github.dicomflow.androiddicomflow.util.criptografia.DecoradorServicoAssinado;
import com.github.dicomflow.androiddicomflow.util.criptografia.EncriptaDecriptaRSA;
import com.github.dicomflow.dicomflowjavalib.FactoryService;
import com.github.dicomflow.dicomflowjavalib.services.Service;
import com.github.dicomflow.dicomflowjavalib.services.certificate.CertificateRequest;
import com.github.dicomflow.dicomflowjavalib.services.request.RequestPut;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import org.spongycastle.asn1.bc.EncryptedSecretKeyData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {
    private static String TAG = "[MainActivity]";

    private static final int REPORT_PICKER_RESULT_FOR_REQUEST_PUT = 2000;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;

    private GoogleApiClient mGoogleApiClient = null;
    private Handler mHandler;


    // tags used to attach the fragments
    private static String CURRENT_TAG = "home";
    private static final String TAG_HOME = "home";
    private static final String TAG_CERTIFICATES = "CERTIFICATES";
    private static final String TAG_REQUESTS = "REQUESTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reportPickerIntent = new Intent(Intent.ACTION_GET_CONTENT, ContactsContract.Contacts.CONTENT_URI);
                reportPickerIntent.setType("text/xml");
                startActivityForResult(reportPickerIntent, REPORT_PICKER_RESULT_FOR_REQUEST_PUT);
                Toast.makeText(MainActivity.this, "okok", Toast.LENGTH_SHORT).show();
            }
        });

        mHandler = new Handler();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        /** drawer **/

        // Create a few sample profile
        final IProfile profile = new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail());
//                .withIcon(FontAwesome.Icon.faw_user);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profile)
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Contatos").withIcon(GoogleMaterial.Icon.gmd_nature_people).withIdentifier(1),
                        new PrimaryDrawerItem().withName("Requisições").withIcon(GoogleMaterial.Icon.gmd_account_box_mail).withIdentifier(2),
                        new PrimaryDrawerItem().withName("Ver Arquivo").withIcon(GoogleMaterial.Icon.gmd_account_box_mail).withIdentifier(3),
                        new SectionDrawerItem().withDivider(true).withName("Minhas chaves"),
                        new PrimaryDrawerItem().withName("Private key").withIcon(GoogleMaterial.Icon.gmd_lock).withIdentifier(4),
                        new PrimaryDrawerItem().withName("Public key").withIcon(GoogleMaterial.Icon.gmd_lock_open).withIdentifier(5),
                        new PrimaryDrawerItem().withName("Trocar certificados").withIcon(GoogleMaterial.Icon.gmd_lock).withIdentifier(6),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Sair").withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(0)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            switch ( (short)drawerItem.getIdentifier()) {
                                case 0:
                                    signOut();
                                    startActivity(new Intent(MainActivity.this, GoogleSignInActivity2.class));
                                    finish();
                                    return true;
                                case 1:
                                    CURRENT_TAG = TAG_CERTIFICATES;
                                    loadHomeFragment(drawerItem);
                                    return true;
                                case 2:
                                    CURRENT_TAG = TAG_REQUESTS;
                                    loadHomeFragment(drawerItem);
                                    return true;
                                case 3:
                                    String arquivoBase64 = "RGljb21GbG93";
                                    abrirArquivoPDF(arquivoBase64);
                                    return true;
                                case 4:

                                    try {
                                        PrivateKey privateKey = EncriptaDecriptaRSA.getMyPrivateKey(MainActivity.this);
                                        new MaterialDialog.Builder(MainActivity.this)
                                                .title("Private Key")
                                                .content(privateKey.toString())
                                                .show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(MainActivity.this, "Erro ao tentar obter chave", Toast.LENGTH_SHORT).show();
                                    }

                                    return true;
                                case 5:
                                    try {
                                        PublicKey publicKey = EncriptaDecriptaRSA.getMyPublicKey(MainActivity.this);
                                        new MaterialDialog.Builder(MainActivity.this)
                                                .title("Public Key")
                                                .content(publicKey.toString())
                                                .show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(MainActivity.this, "Erro ao tentar obter chave", Toast.LENGTH_SHORT).show();
                                    }
                                    return true;
                                case 6:
                                    abrirContactPickerParaIniciarCertificateRequest();
                                    return true;
                                case 10:
                                    // launch new intent instead of loading fragment
                                    startActivity(new Intent(MainActivity.this, CertificateListActivity.class));
                                    result.closeDrawer();
                                    return true;
                            }
                        }

                        return false;
                    }

                    public void abrirArquivoPDF(String base64) {
                        FileUtil.base64ToFile(base64, getBaseContext(), "teste.txt");
                    }

                    private void signOut() {
                        // Firebase sign out
                        FirebaseAuth.getInstance().signOut();
                        // Google sign out
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                        // Facebook sign out
                        LoginManager.getInstance().logOut();
                    }

                })
                .withSavedInstance(savedInstanceState)
                .build();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REPORT_PICKER_RESULT_FOR_REQUEST_PUT && resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String filePath = FileUtil.getPath(getBaseContext(), uri);
                Intent intent = new Intent(this, NewCertificateReceiverFullscreenActivity.class);
                intent.putExtra("filePath", filePath);
                startActivity(intent);
        }


        //region contact picker result para quando vai iniciar uma troca de certificados
        if (requestCode == CONTACT_PICKER_RESULT && resultCode == RESULT_OK) {
            Cursor cursor = null;
            try {
                Uri result = data.getData();
                Log.v(TAG, "Got a contact result: " + result.toString());

                String id = result.getLastPathSegment();// get the contact id from the Uri

                // query for everything email
                cursor = this.getContentResolver().query( ContactsContract.CommonDataKinds.Email.CONTENT_URI,  null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                int nameId = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME); //o nome do contato
                int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA); //o email

                // let's just get the first email
                if (cursor.moveToFirst()) {
                    String email = cursor.getString(emailIdx);
                    solicitarCertificateRequest(email);
                } else {
                    Toast.makeText(this, "Nenhum resultado retornado", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Failed to get email data", Toast.LENGTH_SHORT).show();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        //endregion
    }



    /***
     * Returns respected fragment that user
     * selected from navigation menu
     * @param drawerItem
     */
    private void loadHomeFragment(final IDrawerItem drawerItem) {

        if (drawerItem instanceof Nameable) {
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(((Nameable) drawerItem).getName().getText(MainActivity.this));
        }

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            result.closeDrawer();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment(drawerItem.getIdentifier());
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        result.closeDrawer();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        switch (CURRENT_TAG) {
            case TAG_CERTIFICATES:
                getMenuInflater().inflate(R.menu.menu_certificates, menu);
                break;
            case TAG_REQUESTS:
//                getMenuInflater().inflate(R.menu.menu_requests, menu);
                break;
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
    }

    private Fragment getHomeFragment(Long item) {
        switch (item.shortValue()) {
            case 1:
                CertificateListFragment homeFragment = new CertificateListFragment();
                return homeFragment;
            case 2:
                NovoRequestPutsFragment requestPutsFragment = new NovoRequestPutsFragment();
                return requestPutsFragment;
            default:
                return new MainActivityFragment();
        }
    }


    //region inicar um certificate request
    private static final int CONTACT_PICKER_RESULT = 1000;
    private void abrirContactPickerParaIniciarCertificateRequest() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    private void solicitarCertificateRequest(String mailDoDestinatario) {
        try {

            Map<String, Object> params = new HashMap<>();
            params.put("from", getEmail());
            params.put("mail", getEmail());
            params.put("port", getPortDefault());
            params.put("domain", getDomainDefault());
            params.put("publickey", Base64.encodeToString(EncriptaDecriptaRSA.getMyPublicKey(this).getEncoded(), Base64.DEFAULT));

            final CertificateRequest certificateRequest = (CertificateRequest) FactoryService.getInstance().getService(CertificateRequest.class, params);
            MessageServiceSender.newBuilder(this)
                    .withMailto(mailDoDestinatario)
                    .withService(certificateRequest)
                    .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MainActivity.this, "Solicitacao de troca de certificado enviado com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                        @Override
                        public void onFail() {
                            Toast.makeText(MainActivity.this, "Falha no envio do email", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .send("CERTIFICATE REQUEST");
            Map<String, Object> outros = new HashMap<>();
            outros.put("status", "aguardando-certificado");
            outros.put("to", mailDoDestinatario);
            DatabaseUtil.writeNewService(getUid(), certificateRequest, outros);
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
            Toast.makeText(MainActivity.this, "Erro ao enviar o email", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    //endregion
}
