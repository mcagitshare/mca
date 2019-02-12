package com.mca.Job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.mca.Model.Event;
import com.mca.Model.Message;
import com.mca.Realm.RealmClass;

import java.util.UUID;

import io.realm.Realm;

public class MessagesJob extends Job {

    String DisplayMessage, Icon, Option;
    String id = UUID.randomUUID() + "";

    public MessagesJob(String displayMessage, String icon, String option) {
        super(new Params(1).requireNetwork());
        DisplayMessage = displayMessage;
        Icon = icon;
        Option = option;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            Message message = new Message(id, DisplayMessage, Icon, Option);
            RealmClass.InsertMessage(realm, message);
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
