package com.github.dicomflow.androiddicomflow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.login.LoginManager;
import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.certificate.CertificateListActivity;
import com.github.dicomflow.androiddicomflow.activities.login.GoogleSignInActivity2;
import com.github.dicomflow.androiddicomflow.activities.requests.RequestsListActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class MainActivity extends AppCompatActivity {

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private MiniDrawer miniResult = null;
    private Crossfader crossFader;

    private GoogleApiClient mGoogleApiClient = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Sair").withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(0)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {


                            if (drawerItem.getIdentifier() == 1) {
                                startActivity(new Intent(MainActivity.this, CertificateListActivity.class));
                                return true;
                            }

                            if (drawerItem.getIdentifier() == 2) {
                                startActivity(new Intent(MainActivity.this, RequestsListActivity.class));
                                return true;
                            }
                            if (drawerItem.getIdentifier() == 0) {
                                signOut();
                                startActivity(new Intent(MainActivity.this, GoogleSignInActivity2.class));
                                finish();
                                return true;
                            }
                        }

                        return false;
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

        // set the selection to the item with the identifier 5
        if (savedInstanceState == null) {
//            result.setSelection(1, true);
        }
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
}
