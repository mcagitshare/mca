package com.mca.Model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class MessageDetails extends RealmObject {
    @PrimaryKey
    private String Id = UUID.randomUUID().toString();

    @Required
    private String message;
    private long time;
    private String from;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public MessageDetails(String from, String message, long time) {
        this.message = message;
        this.time = time;
        this.from = from;
    }

    public MessageDetails() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
