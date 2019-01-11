package com.project.xr.contactsync.Realm;

import android.util.Log;

import com.project.xr.contactsync.Model.Item;
import com.project.xr.contactsync.Model.ReqResp;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmClass {
    private static String name = "name";
    private static String phone = "phone";

    /*public void addData(Realm realm, final String id, final String image, final String edt_name, final String edt_number) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Item item = realm.createObject(Item.class,id);
                item.setName(edt_name);
                item.setPhone(edt_number);
                item.setImage(image);

//                DataList dataList = realm.createObject(DataList.class);
//                item.setDataList(dataList);
            }
        });
    }*/

    public static void Insertdata(Realm realm, final Item item) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(item);
            }
        });
    }

    public static void InsertReqResp(Realm realm, final ReqResp reqResp) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(reqResp);
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
//                    .or()
//                    .contains(phone, search)
                    .findAll();
        } catch (Exception ex) {
            Log.e("TAG", ex.getLocalizedMessage());
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return list;
    }

    public static ReqResp getBatchdata(int batchData) {
        ReqResp reqResps = null;
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            reqResps = realm.where(ReqResp.class)
                    .equalTo("batchId", batchData)
                    .findFirst();
        } catch (Exception ex) {
            Log.e("TAG", ex.getLocalizedMessage());
        } finally {
        }
        return reqResps;
    }

   /* public void addCount(Realm realm, int count) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Item item = realm.createObject(Item.class);

//                DataList dataList = realm.createObject(DataList.class);
//                item.setDataList(dataList);
            }
        });
    }*/
}