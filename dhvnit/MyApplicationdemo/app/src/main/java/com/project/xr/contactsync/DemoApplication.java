package com.project.xr.contactsync;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;

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
}