package org.jaaa.pillbox;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class NotificationService extends Service
{
    private static HashMap<String, Boolean> sentMeds = new HashMap<>();
    private static HashMap<String, Boolean> sentEvents = new HashMap<>();
    private Timer syncTimer;
    private Timer checkTimer;
    private static DataSnapshot latestEvent;
    private static DataSnapshot latestMed;
    private int dayOfWeek;
    private int dayOfMonth;
    private int month;
    private int year;
    private static DateFormat format = new SimpleDateFormat("HH:mm");
    private static DateFormat format1 = new SimpleDateFormat("hh:mm a");
    private String time;
    private boolean syncing;
    private boolean checking;
    private Vibrator v;

    public static void setLatestMed(DataSnapshot latest)
    {
        latestMed = latest;
    }

    public static void setLatestEvent(DataSnapshot latest)
    {
        latestEvent = latest;
    }

    public NotificationService()
    {
        Log.d("Service", "Service started!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int id)
    {
        super.onStartCommand(intent, flags, id);
        v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        dayOfWeek = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK);
        dayOfMonth = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH);
        month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1;
        year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("Service", "Destroyed!");
        Intent i = new Intent("org.jaaa.pillbox.ActivityRecognition.RestartService");
        sendBroadcast(i);
        stopTimers();
    }

    public void startTimer()
    {
        syncTimer = new Timer();
        TimerTask syncTask = new TimerTask()
        {
            @Override
            public void run()
            {
                sync();
            }
        };

        syncTimer.schedule(syncTask, 100, 600000);

        checkTimer = new Timer();
        TimerTask checkTask = new TimerTask()
        {
            @Override
            public void run()
            {
                check();
            }
        };

        checkTimer.schedule(checkTask, 5000, 30000);
    }

    public void reset()
    {
        sentMeds.clear();
        sentEvents.clear();
    }

    public void stopTimers()
    {
        if (syncTimer != null)
        {
            syncTimer.cancel();
            syncTimer = null;
        }

        if (checkTimer != null)
        {
            checkTimer.cancel();
            checkTimer = null;
        }
    }

    public synchronized void sync()
    {
        if (FirebaseHelper.AUTH.getCurrentUser() != null && !syncing)
        {
            Log.d("Service", "Syncing!");
            syncing = true;
            final AtomicBoolean doneMeds = new AtomicBoolean(false);
            final AtomicBoolean doneEvents = new AtomicBoolean(false);

            DatabaseReference medRef = FirebaseHelper.USER.child(FirebaseHelper.AUTH.getCurrentUser().getUid()).child("data").child("med-info-" + (dayOfWeek - 1));
            DatabaseReference calRef = FirebaseHelper.USER.child(FirebaseHelper.AUTH.getCurrentUser().getUid()).child("data").child("cal-info-" + dayOfMonth + "-" + month + "-" + year);

            medRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    doneMeds.set(true);
                    latestMed = dataSnapshot;
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    doneMeds.set(true);
                    Log.d("Service", Log.getStackTraceString(databaseError.toException()));
                }
            });

            calRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    doneEvents.set(true);
                    latestEvent = dataSnapshot;
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    doneEvents.set(true);
                    Log.d("Service", Log.getStackTraceString(databaseError.toException()));
                }
            });

            while (!doneEvents.get() && !doneMeds.get());

            syncing = false;
        }
    }

    public synchronized void check()
    {
        if (FirebaseHelper.AUTH.getCurrentUser() != null && latestMed != null && latestEvent != null && !checking)
        {
            Log.d("Service", "Checking!");
            checking = true;

            dayOfWeek = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK);
            dayOfMonth = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH);
            month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1;
            year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
            long hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
            String hourString = hour < 10 ? "0" + hour : hour + "";
            long minute = java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE);
            String minuteString = minute < 10 ? "0" + minute : minute + "";

            try
            {
                Date date = format.parse(hourString + ":" + minuteString);
                time = format1.format(date);
            }
            catch (ParseException e)
            {
                Log.d("MedicationList", Log.getStackTraceString(e));
            }

            for (DataSnapshot d : latestMed.getChildren())
            {
                try
                {
                    if (sentMeds.get(d.getKey()) == null)
                    {
                        sentMeds.put(d.getKey(), false);
                    }

                    long storedHour = (long) d.child("hour").getValue();
                    long storedMinute = (long) d.child("minute").getValue();

                    if (storedHour == hour && storedMinute == minute)
                    {
                        if (!sentMeds.get(d.getKey()))
                        {
                            String name = d.child("name").getValue().toString();

                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(NotificationService.this)
                                            .setSmallIcon(R.mipmap.not_icon)
                                            .setContentTitle("Take Medication")
                                            .setContentText("It's time to take your " + name + ".");

                            NotificationManager mNotifyMgr =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                            Intent resultIntent = new Intent(NotificationService.this, MedicationsActivity.class);
                            resultIntent.putExtra("day", dayOfWeek - 1);
                            PendingIntent resultPendingIntent =
                                    PendingIntent.getActivity(
                                            NotificationService.this,
                                            0,
                                            resultIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );

                            mBuilder.setContentIntent(resultPendingIntent);

                            mNotifyMgr.notify(1, mBuilder.build());
                            v.vibrate(3000);

                            sentMeds.put(d.getKey(), true);
                        }
                    }
                    else
                    {
                        sentMeds.put(d.getKey(), false);
                    }
                }
                catch (Exception e)
                {
                    Log.d("Service", Log.getStackTraceString(e));
                }
            }

            for (DataSnapshot d : latestEvent.getChildren())
            {
                try
                {
                    if (sentEvents.get(d.getKey()) == null)
                    {
                        sentEvents.put(d.getKey(), false);
                    }

                    if (time.equals(d.child("time").getValue().toString()))
                    {
                        if (!sentEvents.get(d.getKey()))
                        {
                            String name = d.child("name").getValue().toString();
                            String desc = d.child("desc").getValue().toString();

                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(NotificationService.this)
                                            .setSmallIcon(R.mipmap.not_icon)
                                            .setContentTitle("Calendar: " + name)
                                            .setContentText(desc);

                            NotificationManager mNotifyMgr =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                            Intent resultIntent = new Intent(NotificationService.this, CalendarEventActivity.class);
                            resultIntent.putExtra("day", dayOfMonth);
                            resultIntent.putExtra("month", month);
                            resultIntent.putExtra("year", year);
                            PendingIntent resultPendingIntent =
                                    PendingIntent.getActivity(
                                            NotificationService.this,
                                            0,
                                            resultIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );

                            mBuilder.setContentIntent(resultPendingIntent);

                            mNotifyMgr.notify(2, mBuilder.build());
                            v.vibrate(3000);

                            sentEvents.put(d.getKey(), true);
                        }
                    }
                    else
                    {
                        sentEvents.put(d.getKey(), false);
                    }
                }
                catch (Exception e)
                {
                    Log.d("Service", Log.getStackTraceString(e));
                }
            }

            checking = false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
