package com.mca.Job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.mca.Model.Contact;
import com.mca.Model.Event;
import com.mca.Realm.RealmClass;

import java.util.UUID;

import io.realm.Realm;

public class ContactJob extends Job {

    String id = UUID.randomUUID() + "";
    String GroupId, ContactId, Name, Image, Phone;
    Boolean ReadStatus;

    public ContactJob(String groupId, String contactId, String name, String image, String phone, Boolean readStatus) {
        super(new Params(1).requireNetwork());
        GroupId = groupId;
        ContactId = contactId;
        Name = name;
        ReadStatus = readStatus;
        Image = image;
        Phone = phone;
    }


    @Override
    public void onAdded() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            Contact contact = new Contact(id, GroupId, ContactId, Name, Image, Phone, ReadStatus);
            RealmClass.InsertContact(realm, contact);
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
