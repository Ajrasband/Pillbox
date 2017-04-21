package org.jaaa.pillbox;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        setContentView(R.layout.activity_create_account);
    }

    public void onCreateAccountButtonClicked(View v)
    {
        String email = ((EditText)findViewById(R.id.editText)).getText().toString();
        String password = ((EditText)findViewById(R.id.editText2)).getText().toString();
        String confirmPassword = ((EditText)findViewById(R.id.editText3)).getText().toString();

        if (password.equals(confirmPassword))
        {
            createAccount(email, password);
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pillbox");
            builder.setMessage("The passwords must match.");
            builder.setNeutralButton("Ok", null);
            builder.create().show();
        }
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
                    Log.d(TAG, "Successfully created account.");
                    Toast.makeText(CreateAccountActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                    FirebaseHelper.user = FirebaseHelper.AUTH.getCurrentUser();
                }
                else
                {
                    Log.d(TAG, "Unsuccessfully created account.");
                    Toast.makeText(CreateAccountActivity.this, "Could not create account.", Toast.LENGTH_SHORT).show();
                    FirebaseHelper.user = null;
                }
            }
        });
    }
}
