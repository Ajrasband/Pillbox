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

public class CreateAccountActivity extends Activity
{
    public static final String TAG = "CreateAccountActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void onCreateAccountButtonClicked(View v)
    {
        String email = ((EditText)findViewById(R.id.new_email_box)).getText().toString();
        String password = ((EditText)findViewById(R.id.new_password_box)).getText().toString();
        createAccount(email, password);
    }

    public void createAccount(String email, String password)
    {
        FirebaseHelper.AUTH.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    // TODO update ui
                    Log.d(TAG, "Successfully created account.");
                    FirebaseHelper.user = FirebaseHelper.AUTH.getCurrentUser();
                }
                else
                {
                    Log.d(TAG, "Unsuccessfully created account.");
                    FirebaseHelper.user = null;
                }
            }
        });
    }
}
