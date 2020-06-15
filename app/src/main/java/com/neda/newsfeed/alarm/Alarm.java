package com.neda.newsfeed.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.neda.newsfeed.Configuration;

public class Alarm {
    private static Alarm instance;
    public static final String alarmReceiverAction = "alarmReceiverAction";
    private final int alarmRequestCode = 1234;
    private final PendingIntent alarmPendingIntent;
    private final AlarmManager alarmMgr;
    private final String TAG = "Alarm";

    public static Alarm getInstance(Context context) {
        if(instance == null || instance.alarmMgr == null)
            instance = new Alarm(context);
        return instance;
    }

    private Alarm(Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(alarmReceiverAction);
        alarmPendingIntent = PendingIntent.getBroadcast(context, alarmRequestCode, intent, 0);
    }

    public void setAlarm() {
        Log.d(TAG, "Set alarm");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + Configuration.postLifetime, alarmPendingIntent);
        } else {
            alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + Configuration.postLifetime, alarmPendingIntent);
        }
    }
}
