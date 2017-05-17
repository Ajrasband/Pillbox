package org.jaaa.pillbox;


import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private Intent serviceIntent;
    private NotificationService service;
    private FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        service = new NotificationService();
        serviceIntent = new Intent(this, service.getClass());

        listener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                service.reset();
            }
        };

        //Check if user is signed in.
        if (FirebaseHelper.AUTH.getCurrentUser() == null)
        {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

        if (!serviceRunning(service.getClass()))
        {
            startService(serviceIntent);
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();

        if (listener != null)
        {
            FirebaseHelper.AUTH.removeAuthStateListener(listener);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseHelper.AUTH.addAuthStateListener(listener);
    }

    @Override
    protected void onDestroy()
    {
        stopService(serviceIntent);
        super.onDestroy();
    }

    public void infoButtonClicked(View v)
    {
        startActivity(new Intent(this, DoctorActivity.class));
    }

    public void calendarButtonClicked(View v)
    {
        startActivity(new Intent(this, CalendarActivity.class));
    }

    public void pillboxButtonClicked(View v)
    {
        startActivity(new Intent(this, PillboxActivity.class));
    }

    public void logOutClicked(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setTitle("Pillbox");
        builder.setMessage("Are you sure you want to sign out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                FirebaseHelper.AUTH.signOut();
                FirebaseHelper.user = null;
                Doc_List.docList.clear();
                startActivity(new Intent(HomeActivity.this, SignInActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    private boolean serviceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo s : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(s.service.getClassName()))
            {
                return true;
            }
        }

        return false;
    }
}
