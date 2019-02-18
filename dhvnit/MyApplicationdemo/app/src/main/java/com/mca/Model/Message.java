package com.mca.Model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Message extends RealmObject {

    @PrimaryKey
    private String Id;

    @Required
    private String DisplayMessage;
    private String Icon;
    private String Option;
    private Boolean ReadStatus;
    private Boolean AccRej;
    private String MessageBody;
    private int UnReadCount = 1;
    private long time;
    private String type;


    public Message() {
    }

    public Message(String id, String displayMessage, String icon, String option, Boolean readStatus,
                   Boolean accRej, String messageBody, int unReadCount, long Time, String type) {

        Id = id;
        UnReadCount = unReadCount;
        DisplayMessage = displayMessage;
        MessageBody = messageBody;
        Icon = icon;
        Option = option;
        ReadStatus = readStatus;
        AccRej = accRej;
        time = Time;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getUnReadCount() {
        return UnReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        UnReadCount = unReadCount;
    }

    public String getMessageBody() {
        return MessageBody;
    }

    public void setMessageBody(String messageBody) {
        MessageBody = messageBody;
    }

    public Boolean getAccRej() {
        return AccRej;
    }

    public void setAccRej(Boolean accRej) {
        AccRej = accRej;
    }

    public Boolean getReadStatus() {
        return ReadStatus;
    }

    public void setReadStatus(Boolean readStatus) {
        ReadStatus = readStatus;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDisplayMessage() {
        return DisplayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        DisplayMessage = displayMessage;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getOption() {
        return Option;
    }

    public void setOption(String option) {
        Option = option;
    }
}
