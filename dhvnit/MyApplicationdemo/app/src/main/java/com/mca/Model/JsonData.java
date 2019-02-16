package com.mca.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class JsonData extends RealmObject {
    @PrimaryKey
    private String Id;

    @Required
    private String JsonString;

    public JsonData() {
    }

    public JsonData(String id, String jsonString) {
        Id = id;
        JsonString = jsonString;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getJsonString() {
        return JsonString;
    }

    public void setJsonString(String jsonString) {
        JsonString = jsonString;
    }
}
