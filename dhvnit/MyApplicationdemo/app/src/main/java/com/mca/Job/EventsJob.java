package com.mca.Job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.mca.Model.Event;
import com.mca.Realm.RealmClass;

import java.util.UUID;

import io.realm.Realm;

public class EventsJob extends Job {

    String id;
    String EventName, Icon, DateFrom, DateTo, Option;
    Boolean ReadStatus;
    Boolean AccRej;

    public EventsJob(String id, String eventName, String icon, String dateFrom, String dateTo, String option, Boolean readStatus, Boolean accRej) {
        super(new Params(1).requireNetwork());
        this.EventName = eventName;
        this.Icon = icon;
        ReadStatus = readStatus;
        AccRej = accRej;
        this.DateFrom = dateFrom;
        this.DateTo = dateTo;
        this.Option = option;
        this.id = id;
    }

    @Override
    public void onAdded() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            Event events = new Event(id, EventName, Icon, DateFrom, DateTo, Option, ReadStatus, AccRej);
            RealmClass.InsertEvent(realm, events);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    @Override
    public void onRun() throws Throwable {
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
