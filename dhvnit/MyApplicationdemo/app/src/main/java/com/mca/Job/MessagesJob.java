package com.mca.Job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.mca.Model.Event;
import com.mca.Model.Message;
import com.mca.Model.MessageDetails;
import com.mca.Realm.RealmClass;
import com.mca.Realm.RealmController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;

public class MessagesJob extends Job {

    String DisplayMessage, Icon, Option;
    String id;
    Boolean ReadStatus;
    Boolean AccRej;
    String MessageBody;
    int UnReadCount;

    public MessagesJob(String id, String displayMessage, String icon, String option, Boolean readStatus, Boolean accRej, String messageBody, int unReadCount) {
        super(new Params(1).requireNetwork());
        DisplayMessage = displayMessage;
        UnReadCount = unReadCount;
        AccRej = accRej;
        Icon = icon;
        MessageBody = messageBody;
        ReadStatus = readStatus;
        Option = option;
        this.id = id;
    }

    @Override
    public void onAdded() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();

            try {
                JSONObject messageObj = new JSONObject(MessageBody);

                if (messageObj.getInt("type") == 100) {

                    //  add the event to Event List
                    DisplayMessage = messageObj.getString("displaymessage");
                    Icon = messageObj.getString("icon");
                }
                if (messageObj.getInt("type") == 101) {

                    //  add the event to Event List
                    DisplayMessage = messageObj.getString("eventname");
                    Icon = messageObj.getString("icon");
                }
                if (messageObj.getInt("type") == 103) {

                    //  add the event to Event List
                    DisplayMessage = messageObj.getString("name");
                    Icon = messageObj.getString("image");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Adding the message details.
            MessageDetails messageDetails = new MessageDetails(id, DisplayMessage, new Date().getTime());
            RealmClass.InsertMessageDetail(realm, messageDetails);

            // Adding parent.
            
            Message message1 = RealmClass.getMessageDetail(realm, id);
            if (message1 == null) {
                Message message = new Message(id, DisplayMessage, Icon, Option, ReadStatus, AccRej, MessageBody, UnReadCount, new Date().getTime());
                RealmClass.InsertMessage(realm, message);
            } else {
                RealmController.updateLastMessageDetail(realm, id, DisplayMessage);
            }


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
