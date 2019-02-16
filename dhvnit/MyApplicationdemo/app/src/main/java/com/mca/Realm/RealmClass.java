package com.mca.Realm;

import android.util.Log;

import com.mca.Model.Group;
import com.mca.Model.Event;
import com.mca.Model.Item;
import com.mca.Model.JsonData;
import com.mca.Model.Message;
import com.mca.Model.ReqResp;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmClass {
    private static String name = "name";

    public static void InsertItem(Realm realm, final Item item) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(item);
            }
        });
    }

    public static void InsertEvent(Realm realm, final Event event) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(event);
            }
        });
    }

    public static void InsertMessage(Realm realm, final Message message) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(message);
            }
        });
    }

    public static void InsertJson(Realm realm, final JsonData jsonData) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(jsonData);
            }
        });
    }

    public static void InsertGroup(Realm realm, final Group group) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(group);
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

    public static RealmResults<Item> searchItemData(String search) {
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

    public static RealmResults<Group> searchGroupData(String search) {
        RealmResults<Group> list = null;
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            list = realm.where(Group.class)
                    .contains("Name", search, Case.INSENSITIVE)
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

    public static RealmResults<Message> searchMessagesData(String search) {
        RealmResults<Message> list = null;
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            list = realm.where(Message.class)
                    .contains("DisplayMessage", search, Case.INSENSITIVE)
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

    public static RealmResults<Event> searchEventData(String search) {
        RealmResults<Event> list = null;
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            list = realm.where(Event.class)
                    .contains("EventName", search, Case.INSENSITIVE)
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