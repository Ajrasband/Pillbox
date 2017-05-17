package org.jaaa.pillbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RestartReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d(RestartReceiver.class.getSimpleName(), "Service stopped!");
        context.startService(new Intent(context, NotificationService.class));
    }
}
