package com.project.xr.contactsync;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DemoApplication extends Application {
    private static DemoApplication instance;
    JobManager jobManager;

    public DemoApplication() {
        instance = this;
    }

    public static DemoApplication getInstance() {
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();

        setDailyTask(getApplicationContext());

        Realm.init(this);
        setupUncaughtException();
        final RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("myContactdemo.realm")
                .schemaVersion(10)
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        Configuration configuration = new Configuration.Builder(this)
                .build();
        jobManager = new JobManager(configuration);
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    private void setupUncaughtException() {

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, final Throwable e) {

                Intent intent = new Intent(getInstance(), ExceptionHandlerActivity.class);
                intent.putExtra("Exception", e + "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);

            }
        });
    }

    public static void setDailyTask(Context context) {
        //TEMP FILE
        Intent intentTempFile = new Intent(context, DailyTaskReceiver.class);
        intentTempFile.setAction(Constants.RECEIVER_DAILY_TASK_ACTION_SYNC_DATA);
        boolean isTempFileWorking = (PendingIntent.getBroadcast(context, Constants.RECEIVER_DAILY_TASK, intentTempFile, PendingIntent.FLAG_NO_CREATE) != null);//just changed the flag
        if (!isTempFileWorking) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 4);
            calendar.set(Calendar.MINUTE, 0);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Constants.RECEIVER_DAILY_TASK, intentTempFile, 0);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY / 8, pendingIntent);
        }
    }

}