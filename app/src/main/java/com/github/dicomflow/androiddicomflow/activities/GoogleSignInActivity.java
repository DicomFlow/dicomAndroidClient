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

package com.github.dicomflow.androiddicomflow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
<<<<<<< HEAD
import android.support.design.widget.Snackbar;
=======
>>>>>>> login com google e facebook
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
<<<<<<< HEAD
import com.facebook.login.LoginManager;
=======
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
>>>>>>> login com google e facebook
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.dicomflow.androiddicomflow.R;
import com.google.android.gms.auth.api.Auth;
<<<<<<< HEAD
import com.google.android.gms.auth.api.credentials.Credential;
=======
>>>>>>> login com google e facebook
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
<<<<<<< HEAD
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
=======
>>>>>>> login com google e facebook

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class GoogleSignInActivity extends BaseActivity implements
<<<<<<< HEAD
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GoogleSignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private TextView mStatusTextView;
    private TextView mDetailTextView;

    /** firebase auth **/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    /** firebase database **/
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usuarioRef = database.getReference("message");
    /** google **/
    private GoogleApiClient mGoogleApiClient;
    /** facebook **/
    private CallbackManager mCallbackManager;

    //region Lifecycle
=======
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;

    private CallbackManager mCallbackManager;
    private FirebaseAuth.AuthStateListener mAuthListener;

>>>>>>> login com google e facebook
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

<<<<<<< HEAD
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
=======
        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START config_signin]
        // Configure Google Sign In
>>>>>>> login com google e facebook
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
<<<<<<< HEAD
=======
        // [END config_signin]
>>>>>>> login com google e facebook

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
<<<<<<< HEAD
        //endregion

        //region Facebook configuration
        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions("email", "public_profile");

        mCallbackManager = CallbackManager.Factory.create();
=======

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        //***** FACEBOOK

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions("email", "public_profile");
>>>>>>> login com google e facebook
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
<<<<<<< HEAD
                // for while I dont do anything
=======
                // ...
>>>>>>> login com google e facebook
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
<<<<<<< HEAD
                showSnackbarError("Authentication error with facebook");
            }
        });
        //endregion

        //region Firebase configuration
        mAuth = FirebaseAuth.getInstance();
=======
                // ...
            }
        });

>>>>>>> login com google e facebook
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
<<<<<<< HEAD
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    updateUI(currentUser);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    updateUI(currentUser);
=======
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
>>>>>>> login com google e facebook
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
<<<<<<< HEAD
        //endregion

    }

=======

    }



    // [START on_start_check_user]
>>>>>>> login com google e facebook
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
<<<<<<< HEAD
=======
    // [END on_start_check_user]

>>>>>>> login com google e facebook

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

<<<<<<< HEAD
=======
    // [START onactivityresult]
>>>>>>> login com google e facebook
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
<<<<<<< HEAD
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
=======
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
>>>>>>> login com google e facebook
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

<<<<<<< HEAD
                        View defaultView = getWindow().getDecorView().getRootView();
=======
>>>>>>> login com google e facebook
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
<<<<<<< HEAD
                            Log.w(TAG, "signInWithCredential:failed", task.getException());
                            showSnackbarError("Authentication failed.");
                        } else  {
                            Log.w(TAG, "signInWithCredential:success", task.getException());
                            showSnackbarError("Authentication success.");
                        }
                        // ...
                        hideProgressDialog();
=======
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(GoogleSignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        } else  {
                            Toast.makeText(GoogleSignInActivity.this, "Authentication succed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
>>>>>>> login com google e facebook
                    }
                });
    }

<<<<<<< HEAD
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
=======
    // [START signin]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]
>>>>>>> login com google e facebook

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
<<<<<<< HEAD
        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        // Facebook sign out
        LoginManager.getInstance().logOut();
=======

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
>>>>>>> login com google e facebook
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();
<<<<<<< HEAD
        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
=======

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
>>>>>>> login com google e facebook
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
<<<<<<< HEAD
            mStatusTextView.setText(getString(R.string.email_fmt, user.getEmail()));
=======
            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
>>>>>>> login com google e facebook
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }
<<<<<<< HEAD
    //endregion

=======

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.disconnect_button) {
            revokeAccess();
        }
    }
>>>>>>> login com google e facebook
}