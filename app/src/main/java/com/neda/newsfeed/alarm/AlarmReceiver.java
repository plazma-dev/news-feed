package com.neda.newsfeed.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.neda.newsfeed.Configuration;
import com.neda.newsfeed.db.AppDatabase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AlarmReceiver extends BroadcastReceiver {
    private final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Alarm.alarmReceiverAction)) {
            //delete expired posts
            CompositeDisposable mDisposable = new CompositeDisposable();
            mDisposable.add(AppDatabase.getInstance(context).postDao().deleteExpiredPosts(System.currentTimeMillis() - Configuration.postLifetime)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> Log.d(TAG, String.format("Deleted %d expired posts", result)),
                            Throwable::printStackTrace));
        }
    }
}
