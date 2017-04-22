package org.jaaa.pillbox;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddEventActivity extends Activity
{
    private int dayInt;
    private int monthInt;
    private int yearInt;
    private String day;
    private String month;
    private String year;
    private String dateString;
    private boolean edit;
    private int pos;
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

        dateString = dayInt + "/" + monthInt + "/" + yearInt;
        pos = getIntent().getIntExtra("pos", 0);
        edit = getIntent().getBooleanExtra("edit", false);
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
                String name = ((EditText) findViewById(R.id.editText5)).getText().toString();
                String desc = ((EditText) findViewById(R.id.editText6)).getText().toString();
                TimePicker picker = (TimePicker) root.findViewById(R.id.timePicker2);
                int hourInt = picker.getCurrentHour();
                String hour = hourInt < 10 ? "0" + hourInt : hourInt + "";
                int minuteInt = picker.getCurrentMinute();
                String minute = minuteInt < 10 ? "0" + minuteInt : minuteInt + "";

                String time = "";

                DateFormat format = new SimpleDateFormat("HH:mm");
                DateFormat format1 = new SimpleDateFormat("hh:mm a");
                Date date;

                try
                {
                    date = format.parse(hour + ":" + minute);
                    time = format1.format(date);
                }
                catch (ParseException e)
                {
                    Log.d("MedicationList", Log.getStackTraceString(e));
                }

                if (!Events.events.containsKey(dateString))
                {
                    Events.events.put(dateString, new ArrayList<Events>());
                }

                if (edit)
                {
                    final DatabaseReference ref = FirebaseHelper.USER.child(FirebaseHelper.user.getUid()).child("data").child("cal-info-" + dayInt + "-" + monthInt + "-" + yearInt);
                    Events.events.get(dateString).remove(pos);
                    Events.events.get(dateString).add(pos, new Events(name, time, desc));

                    ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                for (Events e : Events.events.get(dateString))
                                {
                                    DatabaseReference push = ref.push();
                                    push.child("name").setValue(e.getEventName());
                                    push.child("time").setValue(e.getTime());
                                    push.child("desc").setValue(e.getEventDescription()).addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            finish();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
                else
                {
                    Events e = new Events(name, time, desc);
                    Events.events.get(dateString).add(e);

                    DatabaseReference ref = FirebaseHelper.USER.child(FirebaseHelper.user.getUid()).child("data").child("cal-info-" + dayInt + "-" + monthInt + "-" + yearInt);
                    DatabaseReference push = ref.push();
                    push.child("name").setValue(name);
                    push.child("desc").setValue(desc);
                    push.child("time").setValue(time);
                    finish();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
}
