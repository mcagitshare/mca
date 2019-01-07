package com.example.lenovo.myapplicationdemo.Realm;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class Migrat implements RealmMigration {


    @Override
    public void migrate(DynamicRealm dynamicRealm, long oldVersion, long newVersion) {

        RealmSchema realmSchema = dynamicRealm.getSchema();

        if (oldVersion == 1) {
            RealmObjectSchema objectSchema = realmSchema.get("Item");
            objectSchema.addField("number", String.class);
            oldVersion++;
        }

        if (oldVersion == 2) {
            realmSchema.create("Users")
                    .addField("grade", String.class);
            RealmObjectSchema objectSchema = realmSchema.get("Item");
            objectSchema.addRealmObjectField("users", realmSchema.get("Users"));
            oldVersion++;
        }

        if (oldVersion == 3) {
            realmSchema.create("Students")
                    .addField("grade", String.class);
            RealmObjectSchema objectSchema = realmSchema.get("Item");
            objectSchema.addRealmObjectField("students", realmSchema.get("Students"));
            oldVersion++;
        }

        if (oldVersion == 4) {
            realmSchema.create("Teachers")
                    .addField("grade", String.class);
            RealmObjectSchema objectSchema = realmSchema.get("Item");
            objectSchema.addRealmObjectField("teachers", realmSchema.get("Teachers"));
            oldVersion++;
        }

        if (oldVersion == 5) {
            realmSchema.create("Users")
                    .addField("grade", String.class);
            RealmObjectSchema objectSchema = realmSchema.get("Item");
            objectSchema.addRealmObjectField("users", realmSchema.get("Users"));
            oldVersion++;
        }

        if (oldVersion == 6) {
            realmSchema.create("DataList")
                    .addField("grade", String.class);
            RealmObjectSchema itemObjectSchema = realmSchema.get("Item");
            itemObjectSchema.addRealmObjectField("dataList", realmSchema.get("DataList"));
            oldVersion++;

        }

        if (oldVersion == 7) {
            //add new field and change all mobile number
            RealmObjectSchema item = realmSchema.get("Item");
            item.transform(new RealmObjectSchema.Function() {
                @Override
                public void apply(DynamicRealmObject dynamicRealmObject) {
                    dynamicRealmObject.set("number", "999999");
                }
            });

            RealmObjectSchema datalist = realmSchema.get("DataList");
            datalist.addField("address", String.class)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("grade", "B");
                        }
                    });

            oldVersion++;
        }

        if (oldVersion == 8) {
            //add new field and change all mobile number
            RealmObjectSchema item = realmSchema.get("Item");
            item.transform(new RealmObjectSchema.Function() {
                @Override
                public void apply(DynamicRealmObject dynamicRealmObject) {
                    dynamicRealmObject.set("number", "888888");
                }
            });

            RealmObjectSchema datalist = realmSchema.get("DataList");
            datalist.addField("address", String.class)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("grade", "B");
                        }
                    });

            oldVersion++;
        }

        if (oldVersion == 9) {

            // update Item table in number value
            RealmObjectSchema item = realmSchema.get("Item");
            item.transform(new RealmObjectSchema.Function() {
                @Override
                public void apply(DynamicRealmObject dynamicRealmObject) {
                    dynamicRealmObject.set("number", "888888");
                }
            });

            // add field DataList table of city
            RealmObjectSchema dataList = realmSchema.get("DataList");
            dataList.addField("city", String.class)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("city", "Surat");
                        }
                    });

            // set address value and grade
            if (!dataList.hasField("address")) {

                dataList.addField("address", String.class)
                        .transform(new RealmObjectSchema.Function() {
                            @Override
                            public void apply(DynamicRealmObject obj) {
                                obj.set("address", "Gujarat");
                                obj.set("grade", "B");
                            }
                        });
            } else {
                dataList.transform(new RealmObjectSchema.Function() {
                    @Override
                    public void apply(DynamicRealmObject obj) {
                        obj.set("address", "Gujarat");
                        obj.set("grade", "B");
                    }
                });
            }

            oldVersion++;
        }
        if (oldVersion == 10) {

//            Realm realm_edit = Realm.getDefaultInstance();
//
//            realm_edit.executeTransactionAsync(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    Item item_ = realm.where(Item.class).equalTo("name", "thanks").findFirst();
//                    item_.setName("thanks for");
//                }
//            });
            oldVersion++;
        }


    }

}
