package com.github.dicomflow.androiddicomflow.activities;

import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by ricardobarbosa on 03/07/17.
 */

public class GenericFragment extends Fragment{
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    public String getEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }
}
