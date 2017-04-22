package org.jaaa.pillbox;

import android.app.Activity;
import android.os.Bundle;

public class CalendarEventActivity extends Activity
{
    private int day, month, year;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_event);


    }
}
