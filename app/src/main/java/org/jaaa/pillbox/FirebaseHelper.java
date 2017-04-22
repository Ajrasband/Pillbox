package org.jaaa.pillbox;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper
{
    private FirebaseHelper () {}

    private static FirebaseAuth.AuthStateListener listener = new FirebaseAuth.AuthStateListener()
    {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
        {
            user = firebaseAuth.getCurrentUser();
        }
    };

    /**
     * Public auth constant.
     */
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();

    /**
     * Public user variable.
     */
    public static FirebaseUser user;

    /**
     * Public database variable.
     */
    public static final DatabaseReference ROOT = FirebaseDatabase.getInstance().getReference("data");

    /**
     * Public database variable.
     */
    public static final DatabaseReference USER = ROOT.child("user");

    static
    {
        AUTH.addAuthStateListener(listener);
    }

}
