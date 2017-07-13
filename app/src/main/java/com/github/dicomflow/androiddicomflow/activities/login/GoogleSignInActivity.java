/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dicomflow.androiddicomflow.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.certificate.CertificateListActivity;
import com.github.dicomflow.androiddicomflow.activities.outros.BaseActivity;
import com.github.dicomflow.androiddicomflow.activities.requests.RequestsListActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class GoogleSignInActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GoogleSignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private TextView mStatusTextView;
    private TextView mDetailTextView;

    /** firebase auth **/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    /** google **/
    private GoogleApiClient mGoogleApiClient;
    /** facebook **/
    private CallbackManager mCallbackManager;

    //region Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //region Views
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.sign_in_button) {
                    signInWithGoogle();
                } else if (i == R.id.sign_out_button) {
                    signOut();
                } else if (i == R.id.disconnect_button) {
                    revokeAccess();
                }
            }
        };

        findViewById(R.id.sign_in_button).setOnClickListener(listener);
        findViewById(R.id.sign_out_button).setOnClickListener(listener);
        findViewById(R.id.disconnect_button).setOnClickListener(listener);
        //endregion

        //region Google configuration
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //endregion

        //region Facebook configuration
        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions("email", "public_profile");

        mCallbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // for while I dont do anything
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                showSnackbarError("Authentication error with facebook");
            }
        });
        //endregion

        //region Firebase configuration
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    updateUI(currentUser);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    updateUI(currentUser);
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        //endregion

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                updateUI(null);
            }
        }
    }
    //endregion

    //region OnConnectionFailedListener
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        showSnackbarError("Google Play Services error.");
    }
    //endregion

    //region Private methods
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        authWithCredentials(credential);
    }

    private void authWithCredentials(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        View defaultView = getWindow().getDecorView().getRootView();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential:failed", task.getException());
                            showSnackbarError("Authentication failed.");
                        } else  {
                            Log.w(TAG, "signInWithCredential:success", task.getException());
                            showSnackbarError("Authentication success.");
                            verifyUserOnDatabase();
                        }
                        // ...
                        hideProgressDialog();
                    }
                });
    }

    private void verifyUserOnDatabase() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference("users");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild(user.getUid())) {
                    Map<String, String> info = new HashMap<>();
                    info.put("Display Name", user.getDisplayName());
                    info.put("Email", user.getEmail());
                    info.put("Provider Id", user.getProviderId());
                    snapshot.getRef().child(user.getUid()).child("info").setValue(info);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Snackbar.make(getWindow().getDecorView().getRootView(), "Failed to load user.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        authWithCredentials(credential);
    }

    private void showSnackbarError(String string) {
        View view = getWindow().getDecorView().getRootView();
        Snackbar.make(view, string, Snackbar.LENGTH_LONG).show();
    }

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        // Facebook sign out
        LoginManager.getInstance().logOut();
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();
        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(getString(R.string.email_fmt, user.getEmail()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);

            intentForAnotherActivity();
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    private void intentForAnotherActivity() {
//        Intent intent = new Intent(this, RequestsListActivity.class);
        Intent intent = new Intent(this, CertificateListActivity.class);
        startActivity(intent);
    }
    //endregion

}