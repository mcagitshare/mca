package com.example.lenovo.myapplicationdemo.Realm;

import android.app.Activity;
import android.app.Application;

import com.example.lenovo.myapplicationdemo.Model.DataList;
import com.example.lenovo.myapplicationdemo.Model.Item;

import java.util.UUID;

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

    public static RealmController getInstance() {
        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    public void destroy() {
        realm.close();
    }

    public RealmResults<Item> getItems() {
        return realm.where(Item.class).findAll();
    }

    public void addItem(final String edt_name, final String edt_number, final String edt_grade) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String key = UUID.randomUUID().toString();
                Item item = realm.createObject(Item.class, key);
                item.setName(edt_name);
                item.setPhone(edt_number);
                DataList dataList = realm.createObject(DataList.class);
                dataList.setGrade(edt_grade);
                item.setDataList(dataList);
            }
        });
    }

//    public void changeInDatabase(RealmSchema realmSchema) {
//        //add new field and change all mobile number
//        final RealmObjectSchema itemObjectSchema = realmSchema.get("Item");
//        itemObjectSchema.addField("surname", String.class);
//
//        Realm realm_edit = Realm.getDefaultInstance();
//        realm_edit.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                RealmResults<Item> items = realm.where(Item.class).findAll();
//                for (Item item : items) {
//                    item.setNumber("999999");
//                }
//            }
//        });
//    }

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