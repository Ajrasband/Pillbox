package org.jaaa.pillbox;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddEventActivity extends Activity
{
    private int dayInt;
    private int monthInt;
    private int yearInt;
    private String day;
    private String month;
    private String year;
    private SimpleDateFormat format = new SimpleDateFormat("MM dd hh:mm yyyy");

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_subpage);
        dayInt = getIntent().getIntExtra("day", 1);
        day = dayInt < 10 ? "0" + dayInt : dayInt + "";
        monthInt = getIntent().getIntExtra("month", 1);
        month = monthInt < 10 ? "0" + monthInt : monthInt + "";
        yearInt = getIntent().getIntExtra("year", 2000);
        year = yearInt + "";
    }

    public void onCancelClick(View v)
    {
        finish();
    }

    public void onSaveClick(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View root = getLayoutInflater().inflate(R.layout.time_picker, null);
        builder.setView(root);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String name = ((EditText)findViewById(R.id.editText5)).getText().toString();
                String desc = ((EditText)findViewById(R.id.editText6)).getText().toString();
                TimePicker picker = (TimePicker)root.findViewById(R.id.timePicker2);
                int hourInt = picker.getCurrentHour();
                String hour = hourInt < 10 ? "0" + hourInt : hourInt + "";
                int minuteInt = picker.getCurrentMinute();
                String minute = minuteInt < 10 ? "0" + minuteInt : minuteInt + "";

                String date = month + " " + day + " " + hour + ":" + minute + " " + year;

                try
                {
                    Events e = new Events(name, format.parse(date), desc);
                    Events.events.add(e);
                }
                catch (ParseException e)
                {
                    Log.d("AddEventActivity", Log.getStackTraceString(e));
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
}
