package org.jaaa.pillbox;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.CalendarView;
import android.widget.ExpandableListView;
import android.widget.Toast;



public class CalendarActivity extends Activity {

    CalendarView calendar;

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendar = (CalendarView) findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getBaseContext(), "Selected date " + (month + 1) + "/" + dayOfMonth + "/" + year, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(CalendarActivity.this, CalendarEventActivity.class);
                i.putExtra("day", dayOfMonth);
                i.putExtra("month", month + 1);
                i.putExtra("year", year);
                startActivity(i);
            }
        });
    }

}