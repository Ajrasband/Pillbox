package org.jaaa.pillbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CalendarEventActivity extends Activity
{
    private int day, month, year;
    private String dateString;
    private ExpandableListView.OnChildClickListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_event);

        if (FirebaseHelper.user == null)
        {
            return;
        }

        day = getIntent().getIntExtra("day", 1);
        month = getIntent().getIntExtra("month", 1);
        year = getIntent().getIntExtra("year", 2000);

        dateString = day + "/" + month + "/" + year;

        listener = new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                if (childPosition == 0)
                {
                    Intent i = new Intent(CalendarEventActivity.this, AddEventActivity.class);
                    i.putExtra("day", day);
                    i.putExtra("month", month);
                    i.putExtra("year", year);
                    i.putExtra("edit", true);
                    i.putExtra("pos", groupPosition);
                    startActivity(i);
                }
                else if (childPosition == 1)
                {
                    final DatabaseReference ref = FirebaseHelper.USER.child(FirebaseHelper.user.getUid()).child("data").child("cal-info-" + day + "-" + month + "-" + year);
                    Events.events.get(dateString).remove(groupPosition);
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
                                    push.child("desc").setValue(e.getEventDescription());
                                    push.child("time").setValue(e.getTime());
                                }
                            }
                            refreshList();
                        }
                    });
                }

                return false;
            }
        };
    }

    @Override
    public void onActivityResult(int request, int result, Intent data)
    {
        if (request == 1001)
        {
            refreshList();
        }
    }

    public void addEventClicked(View v)
    {
        Intent i = new Intent(CalendarEventActivity.this, AddEventActivity.class);
        i.putExtra("day", day);
        i.putExtra("month", month);
        i.putExtra("year", year);
        startActivityForResult(i, 1001);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        refreshList();
    }

    public void refreshList()
    {
        if (!Events.events.containsKey(dateString))
        {
            Events.events.put(dateString, new ArrayList<Events>());
        }

        DatabaseReference ref = FirebaseHelper.USER.child(FirebaseHelper.user.getUid()).child("data").child("cal-info-" + day + "-" + month + "-" + year);
        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                NotificationService.setLatestEvent(dataSnapshot);

                if (Events.events.containsKey(dateString))
                {
                    Events.events.get(dateString).clear();
                }

                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    String name = d.child("name").getValue().toString();
                    String desc = d.child("desc").getValue().toString();
                    String time = d.child("time").getValue().toString();
                    Events events = new Events(name, time, desc);

                    Events.events.get(dateString).add(events);
                }

                ExpandableListView expandableList = (ExpandableListView)findViewById(R.id.expandableListView2);
                EventsExpandableListAdapter adapter = new EventsExpandableListAdapter(CalendarEventActivity.this, Events.events.get(dateString));
                expandableList.setAdapter(adapter);
                expandableList.setOnChildClickListener(listener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d("CalendarEventActivity", Log.getStackTraceString(databaseError.toException()));
            }
        });
    }
}
