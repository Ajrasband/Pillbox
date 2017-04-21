package org.jaaa.pillbox;


import android.content.DialogInterface;

import android.content.Intent;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Check if user is signed in.
        if (FirebaseHelper.AUTH.getCurrentUser() == null)
        {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
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
}
