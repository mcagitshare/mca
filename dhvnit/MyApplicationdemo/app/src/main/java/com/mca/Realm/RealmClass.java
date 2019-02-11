package com.mca.Realm;

import android.util.Log;

import com.mca.Model.Item;
import com.mca.Model.ReqResp;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmClass {
    private static String name = "name";

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
                    .contains(name, search, Case.INSENSITIVE)
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
            if (realm != null) {
//                realm.close();
            }
        }
        return reqResps;
    }
}