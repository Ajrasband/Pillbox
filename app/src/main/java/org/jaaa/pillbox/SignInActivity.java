package org.jaaa.pillbox;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignInActivity extends Activity
{
    public static final String TAG = "SignInActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void onSignInClicked(View v)
    {
        String email = ((EditText)findViewById(R.id.email_box)).getText().toString();
        String password = ((EditText)findViewById(R.id.password_box)).getText().toString();
        signIn(email, password);
    }

    public void signIn(String email, String password)
    {
        FirebaseHelper.AUTH.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    Log.d(TAG, "Sign in successful.");
                    // TODO update ui
                    FirebaseHelper.user = FirebaseHelper.AUTH.getCurrentUser();
                }
                else
                {
                    Log.d(TAG, "Sign in unsuccessful.\n" + Log.getStackTraceString(task.getException()));
                    FirebaseHelper.user = null;
                }
            }
        });
    }
}
