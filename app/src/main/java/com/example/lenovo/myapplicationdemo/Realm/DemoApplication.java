package com.example.lenovo.myapplicationdemo.Realm;

import android.app.Application;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.example.lenovo.myapplicationdemo.Class.Utils;

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

        final RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("myContactdemo.realm")
                .schemaVersion(10)
                .encryptionKey(Utils.getEncryptKey())
                .migration(new Migrat())
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        Configuration configuration = new Configuration.Builder(this)
                .build();
        jobManager = new JobManager(configuration);
    }

    public JobManager getJobManager() {
        return jobManager;
    }
}