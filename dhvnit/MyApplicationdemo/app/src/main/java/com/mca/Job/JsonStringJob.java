package com.mca.Job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.mca.Model.JsonData;
import com.mca.Model.Message;
import com.mca.Realm.RealmClass;

import java.util.UUID;

import io.realm.Realm;

public class JsonStringJob extends Job {

    String id = UUID.randomUUID() + "";
    String JsonString;

    public JsonStringJob(String jsonString) {
        super(new Params(1).requireNetwork());
        JsonString = jsonString;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            JsonData jsonData = new JsonData(id, JsonString);
            RealmClass.InsertJson(realm, jsonData);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
