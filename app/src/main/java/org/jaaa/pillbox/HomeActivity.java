package org.jaaa.pillbox;


import android.support.annotation.NonNull;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

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
}
