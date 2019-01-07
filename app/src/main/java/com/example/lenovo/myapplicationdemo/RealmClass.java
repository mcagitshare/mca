package com.example.lenovo.myapplicationdemo;

import com.example.lenovo.myapplicationdemo.Model.DataList;
import com.example.lenovo.myapplicationdemo.Model.Item;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmClass {
    private static String name="name";
    private static String number="number";
    public void addDate(Realm realm, final String edt_name, final String edt_number, final String edt_grade) {
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

    public void addDate(Realm realm, final String edt_name, final String edt_number) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String key = UUID.randomUUID().toString();
                Item item = realm.createObject(Item.class, key);
                item.setName(edt_name);
                item.setPhone(edt_number);
//                item.setImage(image);
                DataList dataList = realm.createObject(DataList.class);
                item.setDataList(dataList);
            }
        });
    }

    public void addDate(Realm realm,final String id, final String image, final String edt_name, final String edt_number) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String key = UUID.randomUUID().toString();
                Item item = realm.createObject(Item.class, key);
                item.setName(edt_name);
                item.setPhone(edt_number);
                item.setId(id);
                item.setImage(image);

//                item.setImage(image);
                DataList dataList = realm.createObject(DataList.class);
                item.setDataList(dataList);
            }
        });
    }

    public static RealmResults<Item> searchData(String search) {
        RealmResults<Item> list = null;
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            list = realm.where(Item.class)
                    .contains(name, search)
                    .or()
                    .contains(number, search)
                    .findAll();
        } catch (Exception ex) {
        } finally {

        }
        return list;
    }

    public void editData(Realm realm, final String itemId, final String name, final String number, final String grade) {
        realm.beginTransaction();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Item item_ = realm.where(Item.class).equalTo("id", itemId).findFirst();
                item_.setName(name);
                item_.setPhone(number);
                item_.getDataList().setGrade(grade);
            }
        });
        realm.commitTransaction();
    }

    public void deleteData(Realm realm, final String itemId) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Item.class).equalTo("id", itemId).findFirst()
                        .deleteFromRealm();
            }
        });
    }
}