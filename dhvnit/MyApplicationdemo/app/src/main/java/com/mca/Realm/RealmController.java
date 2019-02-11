package com.mca.Realm;

import android.app.Activity;
import android.app.Application;

import com.mca.Model.Item;

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
}