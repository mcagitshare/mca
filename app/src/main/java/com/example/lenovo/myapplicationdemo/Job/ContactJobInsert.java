package com.example.lenovo.myapplicationdemo.Job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.lenovo.myapplicationdemo.RealmClass;

import io.realm.Realm;

public class ContactJobInsert extends Job {
    private String edt_title, edt_number;

    public ContactJobInsert(String edtTitle, String edtNumber) {
        super(new Params(1));
        this.edt_title = edtTitle;
        this.edt_number = edtNumber;
    }

    @Override
    public void onAdded() {
        //when job is added in the queue for start
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmClass realmClass = new RealmClass();
            realmClass.addDate(realm, edt_title, edt_number);
        } catch (Exception ex) {
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }


    @Override
    public void onRun() throws Throwable {
        //in here required all parameters which will given in super method and then it's start run
        //in start then job start working like insert database or upload data
        //if in here get any error then goes to shouldReRunOnThrowable()

    }


    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        // when job is canceled then goes here
        // in cancelReason given reason for why job cancel @ THIS CLASS WILL GIVEN ERROR RESULT = (JobHolder.java)
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        // if onRun method getting error then call this method

        return null;
    }

}
