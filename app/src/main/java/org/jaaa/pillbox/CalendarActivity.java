package org.jaaa.pillbox;

import android.app.Activity;

import android.os.Bundle;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.CalendarView;
import android.widget.ExpandableListView;
import android.widget.Toast;


/**
 * Created by iceho on 4/21/2017.
 */

public class CalendarActivity extends Activity {

    CalendarView calendar;

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendar = (CalendarView) findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Toast.makeText(getBaseContext(), "Selected date " + dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
            }
        });
    }

}