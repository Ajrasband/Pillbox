package org.jaaa.pillbox;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseHelper
{
    private FirebaseHelper () {}

    /**
     * Public auth constant.
     */
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();

    /**
     * Public user variable.
     */
    public static FirebaseUser user;
}
