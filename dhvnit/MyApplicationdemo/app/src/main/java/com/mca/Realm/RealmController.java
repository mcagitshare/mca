package com.mca.Realm;

import android.app.Activity;
import android.app.Application;

import com.mca.Model.Group;
import com.mca.Model.Event;
import com.mca.Model.Item;
import com.mca.Model.JsonData;
import com.mca.Model.Message;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {
    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public Realm getRealm() {
        realm.where(Item.class).findAllSorted("name");
        return realm;
    }

    public void destroy() {
        realm.close();
    }

    public RealmResults<Item> getItems() {
        return realm.where(Item.class).findAllSorted("name");
    }

    public RealmResults<Event> getEvents() {
        return realm.where(Event.class).findAllSorted("DateFrom");
    }

    public void editEvent(final String eventId, final Boolean readStatus, final Boolean accRej) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Event contact = realm.where(Event.class).equalTo("Id", eventId).findFirst();
                contact.setReadStatus(readStatus);
                contact.setAccRej(accRej);
            }
        });
    }

    public RealmResults<JsonData> getJson() {
        return realm.where(JsonData.class).findAllSorted("JsonString");
    }

    public RealmResults<Message> getMessages() {
        return realm.where(Message.class).findAllSorted("DisplayMessage");
    }

    public void editMessages(final String messageId, final Boolean readStatus, final Boolean accRej) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Message contact = realm.where(Message.class).equalTo("Id", messageId).findFirst();
                contact.setReadStatus(readStatus);
                contact.setAccRej(accRej);
            }
        });
    }

    public RealmResults<Group> getGroups() {
        return realm.where(Group.class).findAllSorted("Name");
    }

    public void editGroup(final String contactId, final Boolean readStatus, final boolean accRej) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Group group = realm.where(Group.class).equalTo("Id", contactId).findFirst();
                group.setReadStatus(readStatus);
                group.setAccRej(accRej);
            }
        });
    }

    public long getItemsCount() {
        return realm.where(Item.class).count();
    }

    public void editItem(final String itemId, final String name, final String number) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Item item_ = realm.where(Item.class).equalTo("id", itemId).findFirst();
                item_.setName(name);
                item_.setPhone(number);
            }
        });
    }

    public void deleteItem(final String itemId) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Item.class).equalTo("id", itemId).findFirst()
                        .deleteFromRealm();
            }
        });
    }

    public void deleteEventData(final String itemId) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Event.class).equalTo("Id", itemId).findFirst()
                        .deleteFromRealm();
            }
        });
    }

    public void deleteMessageData(final String itemId) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Message.class).equalTo("Id", itemId).findFirst()
                        .deleteFromRealm();
            }
        });
    }

    public void deleteGroupData(final String itemId) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Group.class).equalTo("Id", itemId).findFirst()
                        .deleteFromRealm();
            }
        });
    }
}