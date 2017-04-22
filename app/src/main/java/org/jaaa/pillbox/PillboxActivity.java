package org.jaaa.pillbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PillboxActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_box);
    }

    public void sundayClicked(View v)
    {
        Intent i = new Intent(this, MedicationsActivity.class);
        i.putExtra("day", 0);
        startActivity(i);
    }

    public void mondayClicked(View v)
    {
        Intent i = new Intent(this, MedicationsActivity.class);
        i.putExtra("day", 1);
        startActivity(i);
    }

    public void tuesdayClicked(View v)
    {
        Intent i = new Intent(this, MedicationsActivity.class);
        i.putExtra("day", 2);
        startActivity(i);
    }

    public void wednesdayClicked(View v)
    {
        Intent i = new Intent(this, MedicationsActivity.class);
        i.putExtra("day", 3);
        startActivity(i);
    }

    public void thursdayClicked(View v)
    {
        Intent i = new Intent(this, MedicationsActivity.class);
        i.putExtra("day", 4);
        startActivity(i);
    }

    public void fridayClicked(View v)
    {
        Intent i = new Intent(this, MedicationsActivity.class);
        i.putExtra("day", 5);
        startActivity(i);
    }

    public void saturdayClicked(View v)
    {
        Intent i = new Intent(this, MedicationsActivity.class);
        i.putExtra("day", 6);
        startActivity(i);
    }
}
